package com.comcast.test.citf.core.setup.cima;


public class SetupXmlLoaderTest {
	
	private final static String CORE_CONTEXT_FILE = "Core-Spring-Context.xml";
	private final static String COMMON_CONTEXT_FILE = "Common-Spring-Context.xml";
	private static final String FILE_TOLOAD = "CITF_setup.xml";
	private static final int paramsToBeLoaded = 199;
	
	//TODO: Need to change this file according to new structure 
	//based on XCIMA-2992 (Moving all configuration parameters to properties file and removing XML setup file)
	/*@Before
	public void setup() {
		
		CoreContextInitilizer.initializeContext(COMMON_CONTEXT_FILE);
		CoreContextInitilizer.initializeContext(CORE_CONTEXT_FILE);
	}
	
	
	@Test
	public void testLoadDefaultXml() throws Exception {
		
		int noOfParamsLoaded = SetupXmlLoader.loadDefaultXml(FILE_TOLOAD);
		
		assertThat(
				noOfParamsLoaded,
				is(paramsToBeLoaded));
	}*/

}
