package gov.nist.tk.stubs


import gov.nist.tk.simCommon.TestSession
import gov.nist.tk.siteManagement.Site
import gov.nist.tk.siteManagement.TransactionBean
import groovy.transform.TypeChecked
import org.apache.axiom.om.OMElement

import javax.xml.namespace.QName

@TypeChecked
 abstract class SiteLoader {
	protected TestSession testSession;

	protected HashMap<String, Site> siteMap = new HashMap<String, Site>();

	 SiteLoader(TestSession testSession) {
		 assert testSession : "TestSession is null"
		this.testSession = testSession;
	}

	 Site parseSite(OMElement ele) throws Exception {
		String site_name = ele.getAttributeValue(new QName("name"));
		if (site_name == null || site_name.equals(""))
			throw new Exception("Cannot parse Site with empty name from actors config file");
//		if (sites.containsKey(site_name))
//			throw new Exception("Site " + site_name + " is multiply defined in configuration file");
		Site site = new Site(site_name, testSession);
		parseSite(site, ele);
		putSite(site);
		return site;
	}

	@SuppressWarnings("unchecked")
	protected
	void parseSite(Site s, OMElement conf) throws Exception {
	}

	 OMElement siteToXML(Site s) {
		 return null
	}

	void addTransactionXML(OMElement site_ele, TransactionBean tb) {
	}


	protected void putSite(Site s) {
	}

	protected String withoutSuffix(String inp, String suffix) {
		if (inp.endsWith(suffix))
			return inp.substring(0, inp.length() - suffix.length());
		return inp;
	}

}
