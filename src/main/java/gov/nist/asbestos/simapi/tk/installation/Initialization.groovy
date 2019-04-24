package gov.nist.asbestos.simapi.tk.installation

import groovy.transform.TypeChecked
import org.apache.commons.io.FileUtils

@TypeChecked
class Initialization {

    static initTypes(File ec) {
        assert ec.exists() && ec.isDirectory() && ec.canWrite() : "External Cache ${ec} must exist, be a directory, and be writeable"

        File registryDef = new File(new Initialization().getClass().getResource('/types/actors/reg.json').toURI())
        assert registryDef : "Cannot find definition of Registry actor in /types/actors/reg.json in JAR file"
        File types = registryDef.parentFile.parentFile
        assert types : "Cannot find types resource in Initialization constructor"

        File internalTransactionTypesDir = new File(types, 'transactions')
        assert internalTransactionTypesDir : "No internal TransactionTypes"
        File internalActorTypesDir = new File(types, 'actors')
        assert internalActorTypesDir : "No internal ActorTypes"

        FileUtils.copyDirectoryToDirectory(internalTransactionTypesDir, typesFile(ec))

        FileUtils.copyDirectoryToDirectory(internalActorTypesDir, typesFile(ec))

    }

    static File typesFile(File ec) {
       new File(ec, 'types')
    }
}
