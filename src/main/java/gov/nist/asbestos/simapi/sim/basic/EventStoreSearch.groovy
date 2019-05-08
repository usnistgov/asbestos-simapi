package gov.nist.asbestos.simapi.sim.basic

import gov.nist.asbestos.simapi.tk.simCommon.SimId

class EventStoreSearch {
    File externalCache
    SimStore simStore
    File simDir
    Map<String, EventStoreItem> eventItems = [:]  // eventId ->

    EventStoreSearch(File externalCache, SimId channelId) {
        this.externalCache = externalCache
        simStore = new SimStore(externalCache)
        simStore.channelId = channelId
        simDir = simStore.simDir
    }



    void loadAllEventsItems() {
        eventItems = [:]

        List<File> actorFiles = simDir.listFiles() as List<File>
        actorFiles = actorFiles.findAll { File file ->
            file.isDirectory() && !file.name.startsWith('.') && !file.name.startsWith('_')
        }
        actorFiles.each { File actorFile ->
            List<File> resourceFiles = actorFile.listFiles() as List<File>
            resourceFiles = resourceFiles.findAll { File resourceFile ->
                resourceFile.isDirectory() && !resourceFile.name.startsWith('.') && !resourceFile.name.startsWith('_')
            }
            resourceFiles.each { File resourceFile ->
                List<File> eventFiles = resourceFile.listFiles() as List<File>
                eventFiles = resourceFile.findAll { File eventFile ->
                    eventFile.isDirectory() && !eventFile.name.startsWith('.') && !eventFile.name.startsWith('_')
                }
                eventFiles.each { File eventFile ->
                    EventStoreItem item = new EventStoreItem()
                    item.file = eventFile
                    item.eventId = eventFile.name
                    item.transaction = actorFile.name
                    item.resource = resourceFile

                    eventItems[eventFile.name] = item
                }

            }
        }
    }
}
