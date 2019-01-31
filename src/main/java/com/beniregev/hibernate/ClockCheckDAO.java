package com.beniregev.hibernate;

import com.beniregev.model.Machine;

import java.util.Set;

 public class ClockCheckDAO {

    /**
     * Check the clock for this machine against the DB clock.
     * If the check passes return <code>true</code>,
     * If we are out of sync then return <code>false</code>.
     * @return true if this machine is in sync with DB.
     */
    boolean doClockCheck();

    /**
     * @return the set of {@link Machine}s who have reported being out of sync with the DB.
     */
    Set<Machine> getWackedMachines();

    /**
     * Remove the Macheen from the set of machines that are considered out of sync.
     * @param machine to remove
     */
    void removeMachine(Machine machine);

    /**
     * Add the machine to the set of out of sync machines.
     * @param host the identifier of the machine to add
     * @return true if the machine was added, false it if was already contained in the collection.
     */
    boolean addMachine(String host);

}
