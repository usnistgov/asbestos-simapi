package gov.nist.tk.actors

import gov.nist.tk.installation.Initialization
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class InitializationTest  extends Specification {

    @Rule
    TemporaryFolder temporaryFolder

    def 'types init' () {
        when:
        File ec = temporaryFolder.newFolder()

        then:
        ec.exists()
        ec.isDirectory()

        when:
        Initialization.initTypes(ec)

        then:
        new File(ec, 'types').exists()
        new File(new File(ec, 'types'), 'transactions').exists()
        new File(new File(new File(ec, 'types'), 'transactions'), 'rb.json').exists()

        new File(ec, 'types').exists()
        new File(new File(ec, 'types'), 'actors').exists()
        new File(new File(new File(ec, 'types'), 'actors'), 'reg.json').exists()

    }
}
