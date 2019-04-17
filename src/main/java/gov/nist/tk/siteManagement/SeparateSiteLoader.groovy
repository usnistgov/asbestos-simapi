package gov.nist.tk.siteManagement

import gov.nist.tk.simCommon.TestSession;
import groovy.transform.TypeChecked;

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
		if (sites == null)
			sites = new Sites(testSession);
		if (!actorsDir.isDirectory())
			throw new Exception("Cannot load actor descriptions: " +
					actorsDir + " is not a directory");
		for (File file : actorsDir.listFiles()) {
			if (!file.getName().endsWith("xml"))
				continue;
			XmlFileStream xmlFs = XmlFileStream.parse_xml(file);
			sites = load(xmlFs.getOmElement(), sites);

			// Cleanup after using the stream
			if (xmlFs.getParser()!=null)
				xmlFs.getParser().close();
			if (xmlFs.getFr()!=null)
				xmlFs.getFr().close();
		}
		return sites;
	}

	 void saveToFile(File actorsDir, Sites sites) throws Exception {
		for (Site s : sites.asCollection()) {
			saveToFile(actorsDir, s);
		}
	}

	 void saveToFile(File actorsDir, Site site) throws Exception {
		StringBuffer errs = new StringBuffer();
		site.validate(errs);
		if (errs.length() != 0)
			throw new Exception("Validation Errors: " + errs.toString());
		OMElement xml = siteToXML(site);
		String siteName = site.getName();
		actorsDir.mkdirs();
		Io.xmlToFile(new File(actorsDir + File.separator + siteName + ".xml"), xml);
	}
	
	 void delete(File actorsDir, String siteName) {
		for (String fileName : actorsDir.list()) {
			if (!fileName.startsWith(siteName))
				continue;
			new File(actorsDir + File.separator + fileName).delete();
		}
	}

}
