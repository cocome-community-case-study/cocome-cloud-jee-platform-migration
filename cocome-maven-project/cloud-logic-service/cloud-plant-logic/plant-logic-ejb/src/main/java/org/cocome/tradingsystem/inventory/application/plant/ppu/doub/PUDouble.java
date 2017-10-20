package org.cocome.tradingsystem.inventory.application.plant.ppu.doub;

import org.cocome.tradingsystem.inventory.application.plant.ppu.iface.HistoryAction;
import org.cocome.tradingsystem.inventory.application.plant.ppu.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.ppu.iface.IPickAndPlaceUnit;
import org.cocome.tradingsystem.inventory.application.plant.ppu.iface.OperationEntry;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.validation.constraints.NotNull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * This class is merely used as double for the Pick and Place Unit.
 * Every e
 *
 * @author Rudolf Biczok, rudolf.biczok@student.kit.edu
 */
public class PUDouble implements IPickAndPlaceUnit, AutoCloseable {

    private static final int RESULT_CODE_DEFAULT = -1;
    private static final int RESULT_CODE_COMPLETE = 1;

    private final Document instance = loadInstanceFromFile();

    private final Map<String, OperationDoubleEntry> operations;

    private ExecutionMode mode = ExecutionMode.MANUAL;

    private ExecutionState state = ExecutionState.ACTIVE;

    //Need concurrent (not "only" synchronized") list here
    private final List<HistoryEntry> history = new CopyOnWriteArrayList<>();

    private JobData currentOp;

    private Thread workerThread;

    PUDouble(@NotNull final PUOperationDoubleMeta[] operations) {
        this.operations = Arrays.stream(operations).collect(Collectors.toMap(
                PUOperationDoubleMeta::getOperationId,
                entry -> {
                    final OperationDoubleEntry operationEntry = new OperationDoubleEntry();
                    operationEntry.setName(entry.getName());
                    operationEntry.setOperationId(entry.getOperationId());
                    operationEntry.setExecutionDurationInMillis(
                            operationEntry.getExecutionDurationInMillis());
                    return operationEntry;
                },
                (e1, e2) -> {
                    throw new IllegalArgumentException(
                            String.format("Operation IDs are duplicated: %s and %s", e1, e2));
                }));
        this.workerThread = new Thread(this::processQueue);
        workerThread.start();
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
        return history;
    }

    @Override
    public List<HistoryEntry> getHistoryByExecutionId(String executionId) {
        return this.history.stream()
                .filter(e ->
                        e.getExecutionId() != null && e.getExecutionId().equals(executionId)
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryEntry> getHistoryByModuleName(String name) {
        return this.history.stream()
                .filter(e ->
                        e.getResolvedOperationPath() != null
                                && e.getResolvedOperationPath().contains(name)
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryEntry> getHistoryByOperationId(String operationId) {
        return this.history.stream()
                .filter(e ->
                        e.getOperationId() != null
                                && e.getOperationId().contains(operationId)
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryEntry> getHistoryByTimeStemp(String timestamp) {
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
        checkIfNotTerminated();
        checkActiveExecutionId(executionId);
        state = ExecutionState.HOLD;
        final String operationId = currentOp.getCommandString();

        state = ExecutionState.ABORT;
        while (currentOp != null) {
            Thread.yield();
        }

        final HistoryEntry historyEntry = new HistoryEntry();
        if (this.mode != ExecutionMode.AUTOMATIC) {
            historyEntry.setOperationId(operationId);
        }
        historyEntry.setExecutionId(executionId);
        historyEntry.setTimestamp(Instant.now().toString());
        historyEntry.setAction(HistoryAction.ABORT);
        historyEntry.setResultCode(RESULT_CODE_DEFAULT);
        this.history.add(historyEntry);

        return historyEntry;
    }

    private void checkActiveExecutionId(String executionId) {
        final JobData entry = currentOp;
        if (entry == null || !entry.getExecutionId().equals(executionId)) {
            throw new IllegalStateException(String.format("Operation with execution id `%s` is not active", executionId));
        }
    }

    @Override
    public synchronized HistoryEntry holdOperation(String executionId) {
        checkIfNotTerminated();
        checkActiveExecutionId(executionId);
        if (this.state == ExecutionState.HOLD) {
            throw new IllegalStateException("Device is already in halting state");
        }
        this.state = ExecutionState.HOLD;

        final HistoryEntry historyEntry = new HistoryEntry();
        if (this.mode == ExecutionMode.MANUAL) {
            historyEntry.setOperationId(currentOp.getCommandString());
        }
        historyEntry.setExecutionId(executionId);
        historyEntry.setTimestamp(Instant.now().toString());
        historyEntry.setAction(HistoryAction.HOLD);
        historyEntry.setResultCode(RESULT_CODE_DEFAULT);
        this.history.add(historyEntry);

        return historyEntry;
    }

    @Override
    public synchronized HistoryEntry restartOperation(String executionId) {
        checkIfNotTerminated();
        checkActiveExecutionId(executionId);
        if (this.state != ExecutionState.HOLD) {
            throw new IllegalStateException("Cannot restart from a running operation");
        }
        this.state = ExecutionState.ACTIVE;

        final HistoryEntry historyEntry = new HistoryEntry();
        if (this.mode == ExecutionMode.MANUAL) {
            historyEntry.setOperationId(currentOp.getCommandString());
        }
        historyEntry.setExecutionId(executionId);
        historyEntry.setTimestamp(Instant.now().toString());
        historyEntry.setAction(HistoryAction.RESTART);
        historyEntry.setResultCode(RESULT_CODE_DEFAULT);
        this.history.add(historyEntry);

        return historyEntry;
    }

    @Override
    public synchronized HistoryEntry switchToManualMode() {
        checkIfNotTerminated();
        if (this.mode == ExecutionMode.AUTOMATIC && this.currentOp != null) {
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
        checkIfNotTerminated();
        if (this.mode == ExecutionMode.MANUAL && this.currentOp != null) {
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
        checkIfNotTerminated();
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
        queueEntry.setCommandString(operationId);
        queueEntry.setExecutionId(executionId);
        currentOp = queueEntry;

        return entry;
    }

    @Override
    public synchronized HistoryEntry startOperationsInBatch(String operationIds) {
        checkIfNotTerminated();
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

        final JobData queueEntry = new JobData();
        queueEntry.setCommandString(operationIds);
        queueEntry.setExecutionId(executionId);
        currentOp = queueEntry;

        return entry;
    }

    private void checkIfIdle() {
        if (this.currentOp != null) {
            throw new IllegalStateException("Another operation is already in progress");
        }
    }

    @Override
    public void close() throws InterruptedException {
        this.state = ExecutionState.TERMINATE;
        this.workerThread.join();
    }

    private void processQueue() {
        while (this.state != ExecutionState.TERMINATE) {
            if (currentOp != null) {
                final JobData entry = currentOp;
                if (this.mode == ExecutionMode.MANUAL) {
                    final String operationId = entry.getCommandString();
                    final OperationDoubleEntry operationEntry = this.operations.get(operationId);
                    execOperation(entry.getExecutionId(), operationEntry);
                } else {
                    execBatchOperations(entry);
                }
                currentOp = null;
            }
            Thread.yield();
        }
    }

    private void execBatchOperations(JobData entry) {
        final String[] operationIds = entry.getCommandString().split(";");
        for (final String operationId : operationIds) {
            if (this.state == ExecutionState.ABORT) {
                this.state = ExecutionState.ACTIVE;
                return;
            } else if (this.state == ExecutionState.TERMINATE) {
                return;
            }
            final OperationDoubleEntry operationEntry = this.operations.get(operationId);
            execOperation(entry.getExecutionId(), operationEntry);
        }
        final HistoryEntry historyEntry = new HistoryEntry();
        //TODO: Due to a bug in current xPPU, the execution ID is not added when the batch has finished
        //historyEntry.setExecutionId(entry.getExecutionId());
        historyEntry.setTimestamp(Instant.now().toString());
        historyEntry.setAction(HistoryAction.BATCH_COMPLETE);
        historyEntry.setResultCode(RESULT_CODE_COMPLETE);
        this.history.add(historyEntry);
    }

    private void execOperation(final String executionId,
                               final OperationDoubleEntry operationEntry) {
        final long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        do {
            if (this.state == ExecutionState.ACTIVE) {
                currentTime = System.currentTimeMillis();
            } else if (this.state == ExecutionState.ABORT) {
                this.state = ExecutionState.ACTIVE;
                return;
            } else if (this.state == ExecutionState.TERMINATE) {
                return;
            } else {
                Thread.yield();
            }
        } while (currentTime - startTime > operationEntry.getExecutionDurationInMillis());
        final HistoryEntry historyEntry = new HistoryEntry();
        historyEntry.setExecutionId(executionId);
        historyEntry.setOperationId(operationEntry.getOperationId());
        historyEntry.setTimestamp(Instant.now().toString());
        historyEntry.setAction(HistoryAction.COMPLETE);
        historyEntry.setResultCode(RESULT_CODE_COMPLETE);
        this.history.add(historyEntry);
    }

    private void checkIfNotTerminated() {
        if (this.state == ExecutionState.TERMINATE) {
            throw new IllegalStateException("Dummy interface has already been terminated");
        }
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
