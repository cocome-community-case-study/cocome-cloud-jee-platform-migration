package org.cocome.tradingsystem.inventory.application.plant.ppu.iface;

/**
 * Represents a status of an operation in the history list.
 * 
 * @author Rudolf Biczok, rudolf.biczok@student.kit.edu
 */
public enum HistoryAction {
	ABORT, BATCH_COMPLETE, BATCH_START, COMPLETE, HOLD, RESET, RESTART, SET_AUTOMATIC_MODE, SET_MANUAL_MODE, START, STOP;
}
