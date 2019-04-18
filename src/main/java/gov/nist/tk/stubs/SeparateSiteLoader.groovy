package gov.nist.tk.stubs

import gov.nist.tk.simCommon.TestSession
import gov.nist.tk.siteManagement.Site
import gov.nist.tk.siteManagement.Sites
import gov.nist.tk.stubs.SiteLoader;
import groovy.transform.TypeChecked
import org.apache.axiom.om.OMElement;

@TypeChecked
 class SeparateSiteLoader extends SiteLoader {

	 SeparateSiteLoader(TestSession testSession) {
		super(testSession);
	}

	Sites load(OMElement conf, Sites sites) throws Exception {
		parseSite(conf);

		if (sites == null)
			sites = new Sites(testSession);

		sites.setSites(siteMap);
		sites.buildRepositoriesSite(testSession);

		return sites;
	}

	 Sites load(File actorsDir, Sites sites) throws Exception {
		 return null
	}

	 void saveToFile(File actorsDir, Sites sites) throws Exception {
		for (Site s : sites.asCollection()) {
			saveToFile(actorsDir, s);
		}
	}

	 void saveToFile(File actorsDir, Site site) throws Exception {
	}

	 void delete(File actorsDir, String siteName) {
	}

}
