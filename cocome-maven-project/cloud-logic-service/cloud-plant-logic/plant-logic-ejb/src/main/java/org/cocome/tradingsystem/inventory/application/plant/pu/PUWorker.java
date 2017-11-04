package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryAction;
import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.iface.IPUInterface;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * The scheduler class for processing operations for one specific production unit
 * It is a wrapper class for {@link IPUInterface} that allows the use of changeable queues
 * and an event-based interaction.
 *
 * @param <T> the payload data (used for event handling)
 * @author Rudolf Biczok
 */
public class PUWorker<T> implements AutoCloseable {

    private class Job implements Runnable {

        //Poll interval in milliseconds
        private static final long DEFAULT_POLL_INTERVAL = 1000 * 5;

        private final T payload;
        private final List<String> operations;

        /**
         * Canonical constructor
         *
         * @param payload    the payload data associated with this job.
         *                   It will passed to the callback routine every time an event occurs
         * @param operations the list of operations to execute for this job
         */
        Job(final T payload, final List<String> operations) {
            this.payload = payload;
            this.operations = operations;
        }

        @Override
        public void run() {
            HistoryEntry startEntry = PUWorker.this.iface.startOperationsInBatch(
                    String.join(";", this.operations));
            PUWorker.this.callback.onStart(PUWorker.this.unit, payload, startEntry);
            for (final String operationId : this.operations) {
                observeNextHistoryEntry(
                        startEntry.getExecutionId(),
                        () -> {
                            final List<HistoryEntry> history = PUWorker.this.iface.getHistoryByExecutionId(startEntry.getExecutionId());
                            return filterComplete(operationId, history);
                        },
                        (unit, entry) -> PUWorker.this.callback.onProgress(unit, payload, entry));
            }
            observeNextHistoryEntry(
                    startEntry.getExecutionId(),
                    () -> {
                        final List<HistoryEntry> history = PUWorker.this.iface.getHistoryByTimeStemp(startEntry.getTimestamp());
                        return filterBatchComplete(history);
                    },
                    (unit, entry) -> PUWorker.this.callback.onFinish(unit, payload, entry));
        }

        private void observeNextHistoryEntry(
                final String executionId,
                final Supplier<Optional<HistoryEntry>> filter,
                final BiConsumer<IProductionUnit, HistoryEntry> eventConsumer) {
            while (true) {
                final Optional<HistoryEntry> completeEntry = filter.get();
                if (completeEntry.isPresent()) {
                    //TODO current xPPU does not guarantee the existence of an executionId
                    final HistoryEntry entry = completeEntry.get();
                    entry.setExecutionId(executionId);
                    eventConsumer.accept(PUWorker.this.unit, entry);
                    break;
                }
                try {
                    Thread.sleep(DEFAULT_POLL_INTERVAL);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
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

    private final IProductionUnit unit;
    private final IPUInterface iface;
    private final IPUCallback<T> callback;

    private final ThreadPoolExecutor observerThread;

    /**
     * Canonical constructor
     *
     * @param unit     the production unit data structure from the database
     * @param iface    the plain interface
     * @param callback the callback interface used to inform the caller about job status
     */
    public PUWorker(final IProductionUnit unit,
                    final IPUInterface iface,
                    final IPUCallback<T> callback) {
        this.unit = unit;
        this.iface = iface;
        this.callback = callback;
        this.observerThread = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        this.iface.switchToAutomaticMode();
    }

    /**
     * @return the corresponding production unit
     */
    public IProductionUnit getProductionUnit() {
        return unit;
    }

    public void submitJob(final T payload, final List<String> operations) {
        if (this.observerThread.isTerminated()) {
            throw new IllegalStateException("Worker has already been terminated");
        }
        this.observerThread.execute(new Job(payload, operations));
    }

    @Override
    public void close() {
        this.observerThread.shutdown();
    }

    /**
     * Blocks the calling thread until the thread pool as completed
     *
     * @param timeout the amount of time to wait
     * @param unit    the time unit
     * @return {@code true} if the thread pool completed before the timeout elapsed,
     * {@code false} otherwise
     * @throws InterruptedException if an interruption happened while waiting
     */
    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.observerThread.awaitTermination(timeout, unit);
    }

    /**
     * @return the total number of pending instructions in the queue.
     */
    @SuppressWarnings("unchecked")
    public long getWorkLoad() {
        return this.observerThread.getQueue().stream().mapToLong(job -> ((Job) job).operations.size()).sum();
    }
}
