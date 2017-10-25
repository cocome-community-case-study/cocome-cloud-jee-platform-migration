package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryAction;
import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.iface.IPUInterface;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * The scheduler class for processing operations for one specific production unit
 * It is a wrapper class for {@link IPUInterface} that allows the use of changeable queues
 * and an event-based interaction.
 *
 * @author Rudolf Bicozok
 */
public class PUWorker implements AutoCloseable {

    //Poll interval in milliseconds
    private static final long DEFAULT_POLL_INTERVAL = 1000 * 5;
    private final IProductionUnit unit;
    private final IPUInterface iface;
    private final IPUCallback callback;

    private final Queue<PUJob> jobQueue = new ConcurrentLinkedQueue<>();
    private Thread observerThread;

    private boolean terminate;

    /**
     * Canonical constructor
     *
     * @param unit     the production unit data structure from the database
     * @param iface    the plain interface
     * @param callback the callback interface used to inform the caller about job status
     */
    public PUWorker(final IProductionUnit unit,
                    final IPUInterface iface,
                    final IPUCallback callback) {
        this.unit = unit;
        this.iface = iface;
        this.callback = callback;
        this.observerThread = new Thread(this::processQueue);

        this.iface.switchToAutomaticMode();
        this.observerThread.start();
    }

    public void submitJob(final List<String> operations) {
        if (this.terminate) {
            throw new IllegalStateException("Worker has already been terminated");
        }
        jobQueue.add(new PUJob(operations));
    }

    @Override
    public void close() throws Exception {
        this.terminate = true;
        this.observerThread.join();
    }

    private void processQueue() {
        while (!(terminate && this.jobQueue.isEmpty())) {
            if (this.jobQueue.isEmpty()) {
                Thread.yield();
                continue;
            }
            processJob(jobQueue.poll());
        }
    }

    private void processJob(final PUJob job) {
        HistoryEntry startEntry = this.iface.startOperationsInBatch(String.join(";", job.getOperations()));
        this.callback.onStart(this.unit, startEntry);
        for (final String operationId : job.getOperations()) {
            observeNextHistoryEntry(
                    startEntry.getExecutionId(),
                    () -> {
                        final List<HistoryEntry> history = this.iface.getHistoryByExecutionId(startEntry.getExecutionId());
                        return filterComplete(operationId, history);
                    },
                    this.callback::onProgress);
        }
        observeNextHistoryEntry(
                startEntry.getExecutionId(),
                () -> {
                    final List<HistoryEntry> history = this.iface.getHistoryByTimeStemp(startEntry.getTimestamp());
                    return filterBatchComplete(history);
                },
                this.callback::onFinish);
    }

    private void observeNextHistoryEntry(
            final String executionId,
            final Supplier<Optional<HistoryEntry>> filter,
            final BiConsumer<IProductionUnit, HistoryEntry> eventConsumer) {
        long lastSeen = System.currentTimeMillis() - DEFAULT_POLL_INTERVAL;
        while (true) {
            if (System.currentTimeMillis() - lastSeen >= DEFAULT_POLL_INTERVAL) {
                lastSeen = System.currentTimeMillis();
                final Optional<HistoryEntry> completeEntry = filter.get();
                if (completeEntry.isPresent()) {
                    //TODO current xPPU does not guarantee the existence of
                    //an executionId
                    final HistoryEntry entry = completeEntry.get();
                    entry.setExecutionId(executionId);
                    eventConsumer.accept(this.unit, entry);
                    break;
                }
            } else {
                Thread.yield();
            }
        }
    }

    private Optional<HistoryEntry> filterBatchComplete(final List<HistoryEntry> history) {
        return history
                .stream()
                .filter(e -> e.getAction() == HistoryAction.BATCH_COMPLETE)
                .findFirst();
    }

    private Optional<HistoryEntry> filterComplete(final String operationId,
                                                  final List<HistoryEntry> history) {
        return history
                .stream()
                .filter(e -> e.getOperationId() != null
                        && e.getOperationId().equals(operationId)
                        && e.getAction() == HistoryAction.COMPLETE)
                .findFirst();
    }
}
