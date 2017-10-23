package org.cocome.tradingsystem.inventory.application.plant.iface;

import org.w3c.dom.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Low-level REST interface for the Pick and Place Unit (PPU).
 *
 * @author Rudolf Biczok, rudolf.biczok@student.kit.edu
 */
public interface IPUInterface {

    /*** Instance Methods ***/

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/instance")
    Document getInstance();

    /**
     * Returns all available operations
     *
     * @return all available operations
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/operations")
    List<OperationEntry> getOperations();

    /**
     * Returns an {@link OperationEntry} for the given operation id
     *
     * @param operationId the id of the target operation
     * @return an {@link OperationEntry} for the given operation id
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/operations/{operationId}")
    OperationEntry getOperation(@PathParam("operationId") String operationId);

    /* History Methods */

    /**
     * Returns the complete history
     *
     * @return the complete history
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/history/complete")
    List<HistoryEntry> getCompleteHistory();

    /**
     * Returns the history filtered by execution id
     *
     * @param executionId the execution id
     * @return the history filtered by execution id
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/history/executionid/{executionid}")
    List<HistoryEntry> getHistoryByExecutionId(@PathParam("executionid") String executionId);

    /**
     * Returns the history filtered by module name
     *
     * @param name the module name. This is usually an "/"-separated element of
     *             <code>resolvedPath</code> of {@link OperationEntry}.
     * @return the history filtered by module name
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/history/module/{name}")
    List<HistoryEntry> getHistoryByModuleName(@PathParam("name") String name);

    /**
     * Returns the history filtered by operation id
     *
     * @param operationId the target operation id
     * @return the history filtered by operation id
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/history/operationid/{operationid}")
    List<HistoryEntry> getHistoryByOperationId(@PathParam("operationid") String operationId);

    /**
     * Returns the history filtered by time stamp
     *
     * @param timestamp the time stamp used to filter the history
     * @return the history filtered by time stamp
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/history/timestamp/{timestamp}")
    List<HistoryEntry> getHistoryByTimeStemp(@PathParam("timestamp") String timestamp);

    /* Operation Execution Methods */

    /**
     * Aborts the operation denoted by the given execution id
     *
     * @param executionId the execution id of the target operation
     * @return the new history entry resulting from this operation
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("/abort")
    HistoryEntry abortOperation(@QueryParam("executionid") String executionId);

    /**
     * Holds the operation denoted by the given execution id
     *
     * @param executionid the execution id of the target operation
     * @return the new history entry resulting from this operation
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("/hold")
    HistoryEntry holdOperation(@QueryParam("executionid") String executionid);

    /**
     * Restarts the operation denoted by the given execution id
     *
     * @param executionid the execution id of the target operation
     * @return the new history entry resulting from this operation
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("/restart")
    HistoryEntry restartOperation(@QueryParam("executionid") String executionid);

    /**
     * Switches the device to manual mode
     *
     * @return the new history entry resulting from this operation
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("/mode/manual")
    HistoryEntry switchToManualMode();

    /**
     * Switches the device to automatic mode
     *
     * @return the new history entry resulting from this operation
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("/mode/automatic")
    HistoryEntry switchToAutomaticMode();

    /**
     * Holds the given operation
     *
     * @param operationId the target operation id
     * @return the new history entry resulting from this operation
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("/start/{operationid}")
    HistoryEntry startOperation(@PathParam("operationid") String operationId);

    /**
     * Holds the given operation
     *
     * @param operationIds a semicolon (;) separated list of operation ids
     * @return the new history entry resulting from this operation
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("/startbatch")
    HistoryEntry startOperationsInBatch(@QueryParam("operationids") String operationIds);

}
