package gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.resource;

import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.LeafClassList;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlRootElement
public class LeafClassListResource implements LeafClassList {
    List<String> leafClasses = new ArrayList<String>();

    @Override
    public List<String> getLeafClasses() {
        return leafClasses;
    }

    public void setLeafClasses(List<String> leafClasses) { this.leafClasses = leafClasses; }

    public void add(String leafClass) { leafClasses.add(leafClass); }
}
