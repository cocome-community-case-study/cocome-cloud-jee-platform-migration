package org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryAction;
import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.iface.IPUInterface;
import org.cocome.tradingsystem.inventory.application.plant.iface.OperationEntry;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.PUCOperationMeta;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.validation.constraints.NotNull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Dummy implementation of an interface which operates entirely in a single-threaded, thread-save environment.
 *
 * @author Rudolf Biczok, rudolf.biczok@student.kit.edu
 */
public class PUDouble implements IPUInterface {

    private static final int RESULT_CODE_DEFAULT = -1;
    private static final int RESULT_CODE_COMPLETE = 1;

    private final Document instance = loadInstanceFromFile();

    private final Map<String, OperationDoubleEntry> operations;

    private ExecutionMode mode = ExecutionMode.MANUAL;

    private ExecutionState state = ExecutionState.ACTIVE;

    private final List<HistoryEntry> history = new CopyOnWriteArrayList<>();

    private Queue<JobData> batchQueue = new LinkedList<>();

    private long lastAccessDate = System.currentTimeMillis();

    public PUDouble(@NotNull final List<IProductionUnitOperation> operations, final long timingFactor) {
        this.operations = operations.stream().collect(Collectors.toMap(
                IProductionUnitOperation::getOperationId,
                entry -> {
                    final OperationDoubleEntry operationEntry = new OperationDoubleEntry();
                    operationEntry.setName(entry.getName());
                    operationEntry.setOperationId(entry.getOperationId());
                    operationEntry.setExecutionDurationInMillis(entry.getExecutionDurationInMillis() * timingFactor);
                    return operationEntry;
                },
                (e1, e2) -> {
                    throw new IllegalArgumentException(
                            String.format("Operation IDs are duplicated: %s and %s", e1, e2));
                }));
    }

    //TODO Only for testing, maybe needs to be removed
    PUDouble(@NotNull final PUCOperationMeta[] operations, final long timingFactor) {
        this.operations = Arrays.stream(operations).collect(Collectors.toMap(
                PUCOperationMeta::getOperationId,
                entry -> {
                    final OperationDoubleEntry operationEntry = new OperationDoubleEntry();
                    operationEntry.setName(entry.getName());
                    operationEntry.setOperationId(entry.getOperationId());
                    operationEntry.setExecutionDurationInMillis(entry.getExecutionDurationInMillis() * timingFactor);
                    return operationEntry;
                },
                (e1, e2) -> {
                    throw new IllegalArgumentException(
                            String.format("Operation IDs are duplicated: %s and %s", e1, e2));
                }));
    }

    @Override
    public Document getInstance() {
        return instance;
    }

    @Override
    public List<OperationEntry> getOperations() {
        return this.operations.entrySet().stream().map(e -> (OperationEntry) e.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public OperationEntry getOperation(String operationId) {
        return this.operations.get(operationId);
    }

    @Override
    public List<HistoryEntry> getCompleteHistory() {
        processQueue();
        return history;
    }

    @Override
    public List<HistoryEntry> getHistoryByExecutionId(String executionId) {
        processQueue();
        return this.history.stream()
                .filter(e ->
                        e.getExecutionId() != null && e.getExecutionId().equals(executionId)
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryEntry> getHistoryByModuleName(String name) {
        processQueue();
        return this.history.stream()
                .filter(e ->
                        e.getResolvedOperationPath() != null
                                && e.getResolvedOperationPath().contains(name)
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryEntry> getHistoryByOperationId(String operationId) {
        processQueue();
        return this.history.stream()
                .filter(e ->
                        e.getOperationId() != null
                                && e.getOperationId().contains(operationId)
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryEntry> getHistoryByTimeStemp(String timestamp) {
        processQueue();
        final Instant searchTime = Instant.parse(timestamp);
        return this.history.stream()
                .filter(e ->
                        e.getTimestamp() != null
                                && !Instant.parse(e.getTimestamp()).isBefore(searchTime)
                )
                .collect(Collectors.toList());
    }

    @Override
    public synchronized HistoryEntry abortOperation(String executionId) {
        processQueue();
        checkActiveExecutionId(executionId);

        final HistoryEntry historyEntry = new HistoryEntry();
        if (this.mode != ExecutionMode.AUTOMATIC) {
            historyEntry.setOperationId(batchQueue.peek().getOperationId());
        }
        historyEntry.setExecutionId(executionId);
        historyEntry.setTimestamp(Instant.now().toString());
        historyEntry.setAction(HistoryAction.ABORT);
        historyEntry.setResultCode(RESULT_CODE_DEFAULT);
        this.history.add(historyEntry);

        this.batchQueue = new LinkedList<>();
        this.state = ExecutionState.ACTIVE;

        return historyEntry;
    }

    @Override
    public synchronized HistoryEntry holdOperation(String executionId) {
        processQueue();
        checkActiveExecutionId(executionId);
        if (this.state == ExecutionState.HOLD) {
            throw new IllegalStateException("Device is already in halting state");
        }

        final HistoryEntry historyEntry = new HistoryEntry();
        if (this.mode == ExecutionMode.MANUAL) {
            historyEntry.setOperationId(this.batchQueue.peek().getOperationId());
        }
        historyEntry.setExecutionId(executionId);
        historyEntry.setTimestamp(Instant.now().toString());
        historyEntry.setAction(HistoryAction.HOLD);
        historyEntry.setResultCode(RESULT_CODE_DEFAULT);
        this.history.add(historyEntry);

        this.state = ExecutionState.HOLD;

        return historyEntry;
    }

    @Override
    public synchronized HistoryEntry restartOperation(String executionId) {
        processQueue();
        checkActiveExecutionId(executionId);
        if (this.state != ExecutionState.HOLD) {
            throw new IllegalStateException("Cannot restart from a running operation");
        }
        final HistoryEntry historyEntry = new HistoryEntry();
        if (this.mode == ExecutionMode.MANUAL) {
            historyEntry.setOperationId(this.batchQueue.peek().getOperationId());
        }
        historyEntry.setExecutionId(executionId);
        historyEntry.setTimestamp(Instant.now().toString());
        historyEntry.setAction(HistoryAction.RESTART);
        historyEntry.setResultCode(RESULT_CODE_DEFAULT);
        this.history.add(historyEntry);

        this.state = ExecutionState.ACTIVE;

        return historyEntry;
    }

    @Override
    public synchronized HistoryEntry switchToManualMode() {
        processQueue();
        if (this.mode == ExecutionMode.AUTOMATIC && !this.batchQueue.isEmpty()) {
            throw new IllegalStateException("Cannot switch mode while unit is busy");
        }
        this.mode = ExecutionMode.MANUAL;

        final HistoryEntry entry = new HistoryEntry();
        entry.setTimestamp(Instant.now().toString());
        entry.setAction(HistoryAction.SET_MANUAL_MODE);
        entry.setResultCode(RESULT_CODE_DEFAULT);
        this.history.add(entry);

        return entry;
    }

    @Override
    public synchronized HistoryEntry switchToAutomaticMode() {
        processQueue();
        if (this.mode == ExecutionMode.MANUAL && !this.batchQueue.isEmpty()) {
            throw new IllegalStateException("Cannot switch mode while unit is busy");
        }
        this.mode = ExecutionMode.AUTOMATIC;

        final HistoryEntry entry = new HistoryEntry();
        entry.setTimestamp(Instant.now().toString());
        entry.setAction(HistoryAction.SET_AUTOMATIC_MODE);
        entry.setResultCode(RESULT_CODE_DEFAULT);
        this.history.add(entry);

        return entry;
    }

    @Override
    public synchronized HistoryEntry startOperation(String operationId) {
        processQueue();
        checkIfIdle();
        if (this.mode == ExecutionMode.AUTOMATIC) {
            throw new IllegalStateException("Not allowed in automatic mode");
        }
        final String executionId = UUID.randomUUID().toString();

        final HistoryEntry entry = new HistoryEntry();
        entry.setExecutionId(executionId);
        entry.setOperationId(operationId);
        entry.setTimestamp(Instant.now().toString());
        entry.setAction(HistoryAction.START);
        entry.setResultCode(RESULT_CODE_DEFAULT);
        this.history.add(entry);

        final JobData queueEntry = new JobData();
        queueEntry.setOperationId(operationId);
        queueEntry.setExecutionId(executionId);
        this.batchQueue.add(queueEntry);

        return entry;
    }

    @Override
    public synchronized HistoryEntry startOperationsInBatch(String operationIds) {
        processQueue();
        checkIfIdle();
        if (this.mode == ExecutionMode.MANUAL) {
            throw new IllegalStateException("Only allowed in automatic mode");
        }

        final String executionId = UUID.randomUUID().toString();

        final HistoryEntry entry = new HistoryEntry();
        entry.setExecutionId(executionId);
        entry.setTimestamp(Instant.now().toString());
        entry.setAction(HistoryAction.BATCH_START);
        entry.setResultCode(RESULT_CODE_DEFAULT);
        this.history.add(entry);

        for (final String operationId : operationIds.split(";")) {
            final JobData queueEntry = new JobData();
            queueEntry.setOperationId(operationId);
            queueEntry.setExecutionId(executionId);
            this.batchQueue.add(queueEntry);
        }

        return entry;
    }

    private void checkActiveExecutionId(String executionId) {
        if (this.batchQueue.isEmpty() || !this.batchQueue.peek().getExecutionId().equals(executionId)) {
            throw new IllegalStateException(String.format("Operation with execution id `%s` is not active", executionId));
        }
    }

    private void checkIfIdle() {
        if (!this.batchQueue.isEmpty()) {
            throw new IllegalStateException("Another operation is already in progress");
        }
    }

    private synchronized void processQueue() {
        final long newAccessTime = System.currentTimeMillis();

        if (this.state == ExecutionState.ACTIVE) {
            final long timeDiff = newAccessTime - this.lastAccessDate;
            processProgress(timeDiff, newAccessTime);
        }

        this.lastAccessDate = newAccessTime;
    }

    private void processProgress(final long timeDiff, final long newAccessTime) {
        if (this.batchQueue.isEmpty() || newAccessTime <= 0) {
            return;
        }

        final JobData job = this.batchQueue.peek();
        final OperationDoubleEntry operationEntry = this.operations.get(job.getOperationId());
        final long progressInMillis = job.getProgressInMillis();
        final long remainingMillis = operationEntry.getExecutionDurationInMillis() - progressInMillis;

        if (remainingMillis > timeDiff) {
            job.setProgressInMillis(progressInMillis + timeDiff);
            return;
        }

        final long restDiff = timeDiff - remainingMillis;
        final Instant finishInstant = Instant.ofEpochMilli(newAccessTime - restDiff);

        this.batchQueue.poll();

        final HistoryEntry historyEntry = new HistoryEntry();
        historyEntry.setExecutionId(job.getExecutionId());
        historyEntry.setOperationId(operationEntry.getOperationId());
        historyEntry.setTimestamp(finishInstant.toString());
        historyEntry.setAction(HistoryAction.COMPLETE);
        historyEntry.setResultCode(RESULT_CODE_COMPLETE);
        this.history.add(historyEntry);

        if (this.batchQueue.isEmpty() && this.mode == ExecutionMode.AUTOMATIC) {
            final HistoryEntry batchHistoryEntry = new HistoryEntry();
            //TODO: Due to a bug in current xPPU, the execution ID is not added when the batch has finished
            //historyEntry.setExecutionId(entry.getExecutionId());
            batchHistoryEntry.setTimestamp(finishInstant.toString());
            batchHistoryEntry.setAction(HistoryAction.BATCH_COMPLETE);
            batchHistoryEntry.setResultCode(RESULT_CODE_COMPLETE);
            this.history.add(batchHistoryEntry);
        }

        this.processProgress(restDiff, newAccessTime);
    }

    private Document loadInstanceFromFile() {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(ClassLoader.class.getResourceAsStream("/ppu_instance_isa88.xml"));
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException("Unable to create document builder", e);
        } catch (SAXException | IOException e) {
            throw new IllegalArgumentException("Unable to parse document", e);
        }
    }
}
