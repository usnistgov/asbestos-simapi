package gov.nist.asbestos.simapi.sim.basic

class Task {
    EventStore event
    int taskIndex

    static final int REQUEST_TASK = -1

    Task(EventStore event, int index) {
        this.event = event
        this.taskIndex = index
    }

    void select() {
        event.selectTask(taskIndex)
    }
}
