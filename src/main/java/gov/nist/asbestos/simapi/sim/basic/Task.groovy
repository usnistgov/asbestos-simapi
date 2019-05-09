package gov.nist.asbestos.simapi.sim.basic

import groovy.transform.TypeChecked

@TypeChecked
class Task {
    EventStore event
    int taskIndex

    static final int CLIENT_TASK = -1

    Task(EventStore event, int index) {
        this.event = event
        this.taskIndex = index
    }

    void select() {
        event.selectTask(taskIndex)
    }
}
