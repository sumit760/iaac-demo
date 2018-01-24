import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.openqa.selenium.chrome.ChromeOptions;

import com.comcast.cima.test.controller.CimaIdmController;
import com.comcast.cima.test.dataProvider.IdmTestDataProvider;
import com.comcast.test.citf.common.crypto.EncryptionHandler;
import com.comcast.test.citf.common.crypto.KeyStoreManager;
import com.comcast.test.citf.common.http.RestHandler;
import com.comcast.test.citf.common.parser.XMLParserHelper;
import com.comcast.test.citf.common.util.CodecUtility;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.util.AsyncExecutor;
import com.comcast.test.citf.core.util.ObjectDestroyer;

import cucumber.api.CucumberOptions;
//import com.comcast.test.citf.common.util.JsonReader;



public class test extends IdmTestDataProvider{
	
	
	public static void main(String[] args) {
		//MinaServer.stopServer();
		//takeARest();
		//checkMQ();
		/*try{
			ObjectDestroyer.destroyAllCaches();
		}catch(Exception ee){}*/
		//System.exit(0);
		//CimaLoginController.main(new String[]{"qa","WIP","api","comcast","remote"});
		//removeNameSpace();
		/*ChromeOptions options = new ChromeOptions();
		options.addArguments(new String[] { "headless" });
		System.out.println(options.toString());*/
		CimaIdmController.main(new String[]{"QA","WIP","ui","comcast","remote"});
		//CimaIdmController.main(new String[]{"qa","Smoke","api","comcast"});
		//getSignature();
		//crypto();
		//testHibernate();
		//testRunner();
		//new test().cacheTest();
		//populateDatabase();
		/*try
		{
			parseLog();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		//annoTester();
		//new test().getAuthToken();
		//xacmlTester();
		//cacheTest();
		//cryptoTVE();
		//System.out.println(ApiTestDataProvider.getScopeValue("REST_API_LOGIN_CEMP"));
		//daoTest();
		//jsonTest();
		//new test().engineTest();
		//new test().getAuthToken();
		//new test().annoTester();
		//whyTest();
		//System.out.println(System.getProperty("java.io.tmpdir"));
		//strange();
		//engineTest();
		//logReadingTest();
		//deleteUser();
	}
	
	static void removeNameSpace() {
		String token = "<cima:AccessTokenResponse xmlns:cima=\"urn:comcast:login:api:v1.0\" version=\"1.0\"><ServiceToken xmlns=\"urn:comcast:login:api:v1.0\"><AuthResponse xmlns=\"urn:zimbraAccount\"><authToken>0_7c442d823b3fa5c8c143dd0</authToken><lifetime>86399390</lifetime><skin>serenity</skin></AuthResponse></ServiceToken><Status xmlns=\"urn:comcast:login:api:v1.0\" Code=\"urn:comcast:login:api:rest:1.0:status:Success\"/></cima:AccessTokenResponse>";
		
		token = token.replaceAll("cima:", "")
				     .replaceAll(":cima", "")
				     .replaceAll("xmlns=\"[a-z]+\\:[a-zA-Z]+\"", "")
					 .replaceAll("xmlns.*?\\d\\.\\d\"", "");
		System.out.println(token);
	}
	
	
	 /*static void deleteUser() {
		
		try
		{
			init("QA","comcast");
			List<String> users = new ArrayList<String>();
			users.add("mechanical_foot");
			LDAPInterface ldap = getLdapService();
			ldap.purgeUsers(users);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/
	/*static void logReadingTest() {
		try
		{
			init("QA");
			String query = "search index=cima_ndx cima_env=qa cima_domain=comcast.net sourcetype=*resetpwd* earliest=-4d \"[PH:ResetPasswordURL:AT]\" \"idmtest001@outlook.com\" | rex field=_raw \"(?<timestamp>\\d{4}\\-\\d{1,2}\\-\\d{1,2}.*?\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}).*\\[PH:ResetPasswordURL:AT\\]</ns2:Name><ns2:Value>(?<urllink>[a-zA-Z0-9\\:\\/\\.\\?\\=\\_\\%\\-\\&\\;]+)</ns2:Value></ns2:MessageAttribute>\" | table timestamp,urllink";
			
			getLogReader().readFromSplunk("spl-br-29e.idk.cable.comcast.com", 8089, "spal", "Intel@i5", query, null);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/
	
	/*private static void init(String environment,String domain) throws Exception{
        MiscUtility.setEnvironmentVariables("citf.log");
        CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.COMMON_CONTEXT_FILE_NAME);
        CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.CORE_CONTEXT_FILE_NAME);
        CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.CONTEXT_FILE_NAME);
        Map<String, String> commonStringCacheParams = new HashMap<String, String>();
        commonStringCacheParams.put(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT, environment);
        commonStringCacheParams.put(ICimaCommonConstants.STRING_CACHE_KEY_TEST_DOMAIN, domain);
        CoreCacheInitializer.initialize(commonStringCacheParams);
    }*/
	
	private static void destroy(){
        try{
            ObjectDestroyer.destroyAllCaches();
            CoreContextInitilizer.destroyContext();
        }catch(Exception e){e.printStackTrace();}
    }
	
	static void strange() {
		String email = "idm.t8@mail.com";
		System.out.println(email.substring(0, email.indexOf("@")));
	}
	
	
	public static String regularExpressionChecker(String input, String regex, int elementNo){
		
		String matchedExpr = null;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		
		if(matcher.find())
			matchedExpr = matcher.group(elementNo);
		
		return matchedExpr;
	}
	
	/*static void whyTest(){
		try{
			String[] inputs = new String[2];
			//inputs[0] = FileUtility.getFileContentAsString("C:/Rej/Projects/citf-testDev/cima-integration-test/cima-login/target/cucumberThread1Runner/cucumber-result.json");
			inputs[0] = FileUtility.getFileContentAsString("C:/Rej/Projects/citf-testDev/cima-integration-test/cima-login/target/cucumberThread2Runner/cucumber-result.json");
			inputs[1] = FileUtility.getFileContentAsString("C:/Rej/Projects/citf-testDev/cima-integration-test/cima-login/target/cucumberThread3Runner/cucumber-result.json");
			//inputs[3] = FileUtility.getFileContentAsString("C:/Rej/Projects/citf-testDev/cima-integration-test/cima-login/target/cucumberThread4Runner/cucumber-result.json");
			
			String fileContent = JsonReader.generateMergerdCucumberReport(inputs);
			FileUtility.createFile("C:/Users/arej001c/Desktop/tmpF/json/new/cucumber.json", fileContent);
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	/*static void engineTest(){
		try{
			CimaIdmContextInitializer.initializeContext(CimaIdmContextInitializer.COMMON_CONTEXT_FILE_NAME);
			CimaIdmContextInitializer.initializeContext(CimaIdmContextInitializer.CORE_CONTEXT_FILE_NAME);
			CimaIdmContextInitializer.initializeContext(CimaIdmContextInitializer.CONTEXT_FILE_NAME);
			
			FileModifier fileModifier = (FileModifier)CoreContextInitilizer.getBean(ICommonConstants.SPRING_BEAN_FILE_MODIFIER);
			AsyncExecutor asyncExecutor = (AsyncExecutor)CoreContextInitilizer.getBean(ICommonConstants.SPRING_BEAN_ASYNC_EXECUTER);
			ExecutionEngine engine = (ExecutionEngine)CoreContextInitilizer.getBean(ICommonConstants.SPRING_BEAN_EXECUTION_ENGINE);
			//parseLog();
			
			//new ExecutionEngine().executeTests(AbstractExecutionController.TestCategory.INTEGRATION, AbstractExecutionController.TestType.API, 5, "C:/temp/destFiles/", asyncExecutor, fileModifier);
			
			//FileModifier.appendAttributeInFeatureFile("Common-Spring-Context.xml", "@test", "C:/temp/destFiles/temp.feature");
			
			//FileModifier.appendAttributeInFeatureFile("src/com/comcast/cima/test/api/cucumber/feature/RestValidateSmartZoneLogin.feature", "@test123", "C:/temp/destFiles/temp.feature");
			//FileModifier.deleteAttributeFromFeatureFile("src/com/comcast/cima/test/api/cucumber/feature/RestValidateSmartZoneLogin.feature", "@test123", "C:/temp/destFiles/temp.feature");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				//CoreContextInitilizer.destroyContext();
			}catch(Exception ee){}
		}
	}*/
	
	static void jsonTest(){
		String str = "{\"startIndex\": 1,\"itemsPerPage\": 500,\"entryCount\": 500,\"entries\": [ {\"id\": \"LISTING_ID1\",\"guid\": \"IX3vdRcoZxKtLLHMZa_J2j1SSzMAyxzT\",\"updated\": 1440014667000,\"added\": 1440014667000,\"ownerId\": \"OWNER_ID1\",\"addedByUserId\": \"ABUID_ID1\",\"updatedByUserId\": \"UBUID_ID1\",\"version\": 0,\"locked\": false,\"captionType\": \"CC\",\"startTime\": 1440367800000,\"endTime\": 1440374400000,\"hdLevel\": \"720p\",\"programId\": \"http://ccpdss-br-v003-p.br.ccp.cable.comcast.com:9002/entityDataService/data/Program/5557136008814180112\",\"program\": {  \"title\": \"Spy Kids 2: The Island of Lost Dreams\",  \"year\": 2002,  \"shortSynopsis\": \"Young agents vs. a scientist on a creature-filled island.\",  \"mediumSynopsis\": \"SHORT_DESC1\",  \"longSynopsis\": \"LONG_DESC1\",  \"runtime\": 101,  \"type\": \"Movie\",  \"language\": \"eng\",  \"sortTitle\": \"spy kids 2: the island of lost dreams\",  \"starRating\": 3,  \"category\": \"Movie\",  \"adult\": false,  \"contentRating\": { \"scheme\": \"urn:mpaa\", \"rating\": \"PG\", \"subRatings\": [\"V\" ]  },  \"releaseDate\": \"2002-08-07\"},\"stationId\": \"http://mwsprod.ccp.xcal.tv:9003/linearDataService/data/Station/8218932749041878117\",\"sap\": false,\"subjectToBlackout\": false,\"subtitled\": false,\"contentRating\": {  \"scheme\": \"urn:v-chip\",  \"rating\": \"TVPG\"},\"contentRatings\": [  { \"scheme\": \"urn:v-chip\", \"rating\": \"TVPG\"  }],\"threeD\": false,\"cci\": \"CopyFreely\",\"dvrProgramId\": 3771597,\"descriptiveVideoService\": false,\"merlinResourceType\": \"AudienceAvailable\",\"tagIds\": [ ] }, {\"id\": \"LISTING_ID2\",\"guid\": \"ondpLBlwdkODqrYGBXPP1Kny1KaFc1tW\",\"updated\": 1440509803000,\"added\": 1438706549000,\"ownerId\": \"OWNER_ID2\",\"addedByUserId\": \"ABUID_ID2\",\"updatedByUserId\": \"urn:theplatform:auth:any\",\"version\": 3,\"locked\": false,\"audioType\": \"Stereo\",\"captionType\": \"CC\",\"airingType\": \"New\",\"startTime\": 1440374400000,\"endTime\": 1440376200000,\"hdLevel\": \"720p\",\"seriesId\": \"http://ccpdss-br-v003-p.br.ccp.cable.comcast.com:9002/entityDataService/data/Program/4784119468604853112\",\"programId\": \"http://ccpdss-br-v003-p.br.ccp.cable.comcast.com:9002/entityDataService/data/Program/5817015632759746112\",\"program\": {  \"title\": \"Austin & Ally\",  \"year\": 2015,  \"shortSynopsis\": \"SHORT_DESC2\",  \"longSynopsis\": \"LONG_DESC2\",  \"runtime\": 30,  \"type\": \"Episode\",  \"language\": \"eng\",  \"sortTitle\": \"austin & ally\",  \"originalAirDate\": \"2015-08-23\",  \"category\": \"Children's\",  \"adult\": false,  \"contentRating\": { \"scheme\": \"urn:v-chip\", \"rating\": \"TVG\"  },  \"seriesId\": \"http://ccpdss-br-v003-p.br.ccp.cable.comcast.com:9002/entityDataService/data/Program/4784119468604853112\",  \"tvSeasonId\": \"http://ccpdss-br-v003-p.br.ccp.cable.comcast.com:9002/entityDataService/data/TvSeason/7201385063258235114\",  \"tvSeasonNumber\": 4,  \"tvSeasonEpisodeNumber\": 13,  \"seriesEpisodeNumber\": 79,  \"episodeTitle\": \"Burdens & Boynado\"},\"stationId\": \"http://mwsprod.ccp.xcal.tv:9003/linearDataService/data/Station/8218932749041878117\",\"sap\": false,\"subjectToBlackout\": false,\"subtitled\": false,\"contentRating\": {  \"scheme\": \"urn:v-chip\",  \"rating\": \"TVG\"},\"contentRatings\": [  { \"scheme\": \"urn:v-chip\", \"rating\": \"TVG\"  }],\"threeD\": false,\"cci\": \"CopyFreely\",\"dvrProgramId\": 2930918,\"dvrSeriesId\": 3787827,\"descriptiveVideoService\": false,\"merlinResourceType\": \"AudienceAvailable\",\"tagIds\": [ ] } 	 ] }";
		try{
			//JsonReader.readAssetsFromMerlinLDSResponse(str);
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	/*static void daoTest(){
		try{
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.COMMON_CONTEXT_FILE_NAME);
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.CORE_CONTEXT_FILE_NAME);
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.CONTEXT_FILE_NAME);
			CoreCacheInitializer.initialize(null);
			
			Assets ast = getAssetDAO().findAssetByRatingName(AssetRatingTypes.TV_RATING, "TV-Y", "Disney2");
			System.out.println("Name - "+ast.getName()+"and rating - "+ast.getTvRating());
			
			Assets ast2 = getAssetDAO().findAssetByRatingPriority(AssetRatingTypes.TV_RATING, "TV-Y", 4, true, "Disney1");
			System.out.println("Name - "+ast2.getName()+"and rating - "+ast2.getTvRating());
			
			List<DesiredCapabilities> capas = getBrowserCapabilityDAO().findAllCapabilities(Platforms.WINDOWS, Types.COMPUTER);
			System.out.println(capas.size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				CoreContextInitilizer.destroyContext();
				ObjectDestroyer.destroyAllCaches();
			}catch(Exception ee){}
		}
	}*/
	
	
	/*static void xacmlTester(){
		try{
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.COMMON_CONTEXT_FILE_NAME);
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.CORE_CONTEXT_FILE_NAME);
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.CONTEXT_FILE_NAME);
			Map<String, String> commonStringCacheParams = new HashMap<String, String>();
			commonStringCacheParams.put(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT, "QA");
			CoreCacheInitializer.initialize(commonStringCacheParams);
			
			ICitfCache configCache = getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			
			String sig = ((XACMLRequestBuilder)CimaLoginContextInitializer.getBean("xacmlBuilder")).
					createChannelRequestForSAML(	"TestChannel1", 
													XACMLRequestBuilder.Actions.VIEW, 
													"40979_TVE_01", 
													"Tester123", 
													configCache.getString(ICimaCommonConstants.DB_TAB_CONFIG_KEY_SAML_RESPONSE_URL, "QA"), 
													configCache.getString(ICimaCommonConstants.DB_TAB_CONFIG_KEY_TVE_DESTINATION_SOAP_URL, "QA"), 
													configCache.getString(ICimaCommonConstants.DB_TAB_CONFIG_KEY_CLIENT_ID_TVE, "QA"), 
													configCache.getString(ICimaCommonConstants.DB_TAB_CONFIG_KEY_TVE_KEYSTORE_ALIAS, "QA"), 
													configCache.getString(ICimaCommonConstants.DB_TAB_CONFIG_KEY_TVE_KEYSTORE_PASSWORD, "QA"), 
													configCache.getString(ICimaCommonConstants.DB_TAB_CONFIG_KEY_TVE_KEYSTORE_ENTRY_PASSWORD, "QA"));
		
			sig=sig.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
			System.out.println("########### Signature - \n"+sig);
		
			
			RestHandler handler = ((RestHandler)CimaLoginContextInitializer.getBean("restHandler"));
			handler.setRequestType(MediaType.TEXT_XML);
			
			String response = handler.executeWriteRequest("https://login-qa4.comcast.net/api/xacml/soap/tve", sig, RestHandler.WriteRequestMethod.POST);
			System.out.println("\n\n########### Rest Response - \n"+response);
			
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
				CoreContextInitilizer.destroyContext();
			}catch(Exception ee){}
		}
	}*/
	
	
	void testNothing(){
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void annoTester(){
		try{
			
			
			List<String> features = new ArrayList<String>();
			features.add("C:/temp/destFiles/");
			
			List<String> glues = new ArrayList<String>();
			glues.add(ICimaCommonConstants.CUCUMBER_STEP_DEFINATION_PACKAGE_API);
			
			String tag1 = "thread1";
			String fileName1 = "Thread1Runner";
			String fullPath1 = "C:/temp/destFiles/"+fileName1+".java";
			
			String tag2 = "thread2";
			String fileName2 = "Thread2Runner";
			String fullPath2 = "C:/temp/destFiles/"+fileName2+".java";
			
			//boolean succ1 = ClassGenerator.generateRunnerClass(fileName1, fullPath1, tag1, features, glues);
			//boolean succ2 = ClassGenerator.generateRunnerClass(fileName2, fullPath2, tag2, features, glues);
			
			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("C:/temp/destFiles/").toURI().toURL() });
			
			Class runner1 = Class.forName("Thread1Runner", true, classLoader);
			System.out.println(runner1);
			//Class runner2 = cl.loadClass("C:/temp/destFiles/"+fileName2+".class");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void populateDatabase(){
		try{
			//SetupXmlLoader.load(new String[]{"C:/Users/spal004c/git/cima-integration-test/cima-integration-test/citf-common/resources/cima-setup/CITF_setup.xml"});
			   //AccountExcelLoader.load(new String[]{"C:/Users/spal004c/git/cima-integration-test/cima-integration-test/citf-common/resources/cima-setup/Test_Accounts_QA.xlsx","QA"});
			   //UserSetupExcelLoader.load(new String[]{"C:/Users/spal004c/git/cima-integration-test/cima-integration-test/citf-common/resources/cima-setup/Test_Users_TVE_QA.xlsx","QA","TVE"});
			  //UserSetupExcelLoader.load(new String[]{"C:/Users/spal004c/git/cima-integration-test/cima-integration-test/citf-common/resources/cima-setup/Test_Users_Login_QA.xlsx","QA","LOGIN"});
			   //UserSetupExcelLoader.load(new String[]{"C:/Users/spal004c/git/cima-integration-test/cima-integration-test/citf-common/resources/cima-setup/Test_Users_Idm_QA.xlsx","QA","IDM"});
			   //AccountExcelLoader.load(new String[]{"C:/Users/spal004c/git/cima-integration-test/cima-integration-test/citf-common/resources/cima-setup/Test_Accounts_STAGE.xlsx","STAGE"});
			   //UserSetupExcelLoader.load(new String[]{"C:/Users/spal004c/git/cima-integration-test/cima-integration-test/citf-common/resources/cima-setup/Test_Users_Login_STAGE.xlsx","STAGE","LOGIN"});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void parseLog() throws Exception {
		String fileName = "C:\\Users\\spal004c\\log.txt";
		String regex = "\\[PH:SMSPIN:AT\\]</ns2:Name><ns2:Value>(.*?)</ns2:Value></ns2:MessageAttribute>.*?<ns2:Address>(.*?)</ns2:Address>";
		String smscode = null;
		String phone = null;
		
		/*LogFinders searchDtls = getLogFinderDAO().findLogChecker("RESET_PASSWORD_SMS", "QA");
		String regex = searchDtls.getRegex();*/
				
		File file = new File(fileName);
        Scanner scanner;
		try {
			scanner = new Scanner(file);
			scanner.useDelimiter(System.getProperty("line.separator"));
			while(scanner.hasNext()) {
			    String nextToken = scanner.next();
			    Pattern pattern = Pattern.compile(regex);
			    Matcher matcher = pattern.matcher(nextToken);
				if(matcher.find()) {
					smscode = matcher.group(1);
					phone = matcher.group(2);
					System.out.println("SMS code = " + smscode);
					System.out.println("Phone = " + phone);
				}
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        
	}
	
    /*static void cacheTest(){
		try{
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.COMMON_CONTEXT_FILE_NAME);
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.CORE_CONTEXT_FILE_NAME);
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.CONTEXT_FILE_NAME);
			CoreCacheInitializer.initialize(null);
			
			Map<String, String> serviceFilterConditions = new HashMap<String, String>();
			serviceFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
			
			List<Object> serviceObjList = (List<Object>)getCache(ICimaCommonConstants.CACHE_SERVICES).getFilteredObjects(serviceFilterConditions, 15);
			if(serviceObjList!=null && serviceObjList.get(0)!=null){
				for(Object obj: serviceObjList){
					SeviceCache.Service service =(SeviceCache.Service)obj;
					System.out.println(service);
				}
			}
			
			Map<String, String> filterConditions = new HashMap<String, String>();
			filterConditions.put(ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, ICommonConstants.ENVIRONMENT_QA);
			filterConditions.put(ICommonConstants.CACHE_FLTR_CONDTN_CATEGORY, "LOGIN");
			filterConditions.put(ICommonConstants.CACHE_FLTR_CONDTN_TV_RATING, "TV-Y7");
			filterConditions.put(ICommonConstants.CACHE_FLTR_CONDTN_MOVIE_RATING, "PG-13");
			filterConditions.put(ICommonConstants.CACHE_FLTR_CONDTN_CHANNEL_NAME, "TestChannel2");
			filterConditions.put(ICommonConstants.CACHE_FLTR_CONDTN_CHANNEL_SUBSCRIPTION, "Permit");
			filterConditions.put(ICommonConstants.CACHE_FLTR_CONDTN_ENTITLD_SERVICE_NAME, "smartzone");
			filterConditions.put(ICommonConstants.CACHE_FLTR_CONDTN_SERVICE_ENTITLEMENT_CSV_VALUES, "CDV");
			filterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_SERVICE_AUTHORIZATION_STATUS, ICimaCommonConstants.STATUS_USER_SERVICES_SUCCESS);
			filterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_SERVICE_AUTHENTICATION_STATUS, ICimaCommonConstants.STATUS_USER_SERVICES_SUCCESS);
			
			List<Object> users = getCache(ICimaCommonConstants.CACHE_USERS).getFilteredObjects(filterConditions, 5);
			for(Object user : users){
				System.out.println(((UserCache.User)user).toString());
				System.out.println(((UserCache.User)user).getPassword());
			}
			Set<String> keySet = new HashSet<String>(Arrays.asList(	RestAPIGeneralKeys.USERID_PASSWOORD.getValue(), 
					RestAPIGeneralKeys.CLIENT_ID_CLIENT_SECRET.getValue(),	
					Scopes.REST_API_SMARTZONE.getValue(),
					ServiceNames.REST_API_SMARTZONE.getValue(),
					RestAPIGeneralKeys.SERVICE_ACCESS_END_POINT.getValue(),
					RestAPIGeneralKeys.SERVICE_LOGIN_END_POINT.getValue(),
					RestAPIGeneralKeys.APP_KEY_SIGN_KEY.getValue()));

			System.out.println("Calling data provider with keySet = "+keySet.toString());
			Map<String, Object> testData = getTestData(keySet, null, ICommonConstants.ENVIRONMENT_QA);

			if(testData!=null){ 
				if(testData.get(RestAPIGeneralKeys.USERID_PASSWOORD.getValue())!=null){
					List<Map<String, String>> lst = (List<Map<String, String>>)testData.get(RestAPIGeneralKeys.USERID_PASSWOORD.getValue());
					if(lst!=null && lst.get(0)!=null){
						Map<String, String> userPassMap = lst.get(0);
						System.out.println("userId: "+userPassMap.get(KEY_USER_ID));
						System.out.println("password: "+userPassMap.get(KEY_PASSWORD));
					}
				}

				if(testData.get(KEY_CLIENT_ID)!=null)
					System.out.println("clientId: "+testData.get(KEY_CLIENT_ID).toString());

				if(testData.get(KEY_CLIENT_SECRET)!=null)
					System.out.println("clientSecret: "+testData.get(KEY_CLIENT_SECRET).toString());

				if(testData.get(Scopes.REST_API_SMARTZONE.getValue())!=null)
					System.out.println("scope: "+testData.get(Scopes.REST_API_SMARTZONE.getValue()).toString());

				if(testData.get(RestAPIGeneralKeys.SERVICE_ACCESS_END_POINT.getValue())!=null)
					System.out.println("serviceAccessPointUrl: "+testData.get(RestAPIGeneralKeys.SERVICE_ACCESS_END_POINT.getValue()).toString());
				
				if(testData.get(RestAPIGeneralKeys.SERVICE_LOGIN_END_POINT.getValue())!=null)
					System.out.println("loginEndPointURL: "+testData.get(RestAPIGeneralKeys.SERVICE_LOGIN_END_POINT.getValue()).toString());
				
				if(testData.get(KEY_APP_KEY)!=null)
					System.out.println("appKey: "+testData.get(KEY_APP_KEY).toString());
				
				if(testData.get(KEY_SIGN_KEY)!=null)
					System.out.println("signKey: "+testData.get(KEY_SIGN_KEY).toString());
				
				if(testData.get(ServiceNames.REST_API_SMARTZONE.getValue())!=null)
					System.out.println("serviceName: "+testData.get(ServiceNames.REST_API_SMARTZONE.getValue()).toString());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				CoreContextInitilizer.destroyContext();
				ObjectDestroyer.destroyAllCaches();
			}catch(Exception ee){}
		}
	}*/
	
	static void testRunner(){
		JUnitCore junit = null;
		Result res = null;
		try{
			//new ExecutionEngine().loadAndConfigure("smoke", "api");
			ClassLoader classLoader = AsyncExecutor.class.getClassLoader();
			Class runrCls = classLoader.loadClass("com.comcast.test.citf.core.runtime.CucumberRunner");
			System.out.println(runrCls.getAnnotation(CucumberOptions.class));
			//System.out.println("");
			junit = new JUnitCore();
			res = junit.run(runrCls);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*static void testHibernate(){
		try{
			AccountsDAO accDao = (AccountsDAO)CimaLoginContextInitializer.getBean("accountDao");
			Accounts accounts = accDao.findAccountById(1);
			System.out.println("account name - "+accounts.getName());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	void takeARest(){
		try{
			byte[] b = CodecUtility.generateHMACSHA1Signature("testApp testtest B_CIMA_TRIPLE_06", "bcc89dd009ac03f54c56df4410502f756ce92b7afe547949f4a6c6211cebd27b", CodecUtility.AlgorithmType.HMAC_SHA1_ALGORITHM,false);
			String dsig = CodecUtility.encodeURL(new String(Base64.encodeBase64(b)), ICommonConstants.ENCODING_UTF8);
			System.out.println("dsig - "+ dsig);
			Map<String, String> map = new HashMap<String, String>();
			map.put("appkey", "testApp");
			map.put("u", "B_CIMA_TRIPLE_06");
			map.put("p", "testtest");
			map.put("dsig", dsig);
			System.out.println(new RestHandler().executeWriteRequest("https://login-qa4.comcast.net/api/login", map, null, RestHandler.WriteRequestMethod.POST));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void parseSatrSuccXml(){
		
		String satrSuccXml = "<cima:AccessTokenResponse xmlns:cima=\"urn:comcast:login:api:v1.0\" version=\"1.0\"><ServiceToken xmlns=\"urn:comcast:login:api:v1.0\"><AuthResponse xmlns=\"urn:zimbraAccount\"><authToken>0_e10efc5d347093ae1314f7afac5c45aab7d494ff_69643d33363a32303932636237622d646338342d343164642d386538632d3633663563303637306264323b6578703d31333a313433343231383035373139363b747970653d363a7a696d6272613b</authToken><lifetime>86399802</lifetime><skin>serenity</skin></AuthResponse></ServiceToken><Status xmlns=\"urn:comcast:login:api:v1.0\" Code=\"urn:comcast:login:api:rest:1.0:status:Success\"/></cima:AccessTokenResponse>";
		String satrFailXml = "<cima:AccessTokenResponse xmlns:cima=\"urn:comcast:login:api:v1.0\" version=\"1.0\"><cima:Status Code=\"urn:comcast:login:api:rest:1.0:status:UnknownFailure\" Message=\"Failed to acquire Plaxo token\" /></cima:AccessTokenResponse>";
		String ltrSuccXml = "<AuthnResponse xmlns=\"urn:comcast:login:api:v1.0\" version=\"1.0\"><LoginToken ExpiresOn=\"2015-06-12T01:16:55.798Z\">PHNhbWw6QXNzZXJ0aW9uIHhtbG5zOnNhbWw9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphc3NlcnRpb24iIHhtbG5zOm5zND0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyIgeG1sbnM6eGk9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hLWluc3RhbmNlIiB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEiIElEPSJJRF8yMDE1LTA2LTExVDIxLjE2LjU1Ljc5OFoiIElzc3VlSW5zdGFudD0iMjAxNS0wNi0xMVQyMToxNjo1NS44MDFaIiBWZXJzaW9uPSIyLjAiPjxzYW1sOklzc3Vlcj5odHRwczovL2xvZ2luLmNvbWNhc3QubmV0L2FwaS9sb2dpbjwvc2FtbDpJc3N1ZXI+PGRzOlNpZ25hdHVyZSB4bWxuczpkcz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+CjxkczpTaWduZWRJbmZvPgo8ZHM6Q2Fub25pY2FsaXphdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnL1RSLzIwMDEvUkVDLXhtbC1jMTRuLTIwMDEwMzE1Ii8+CjxkczpTaWduYXR1cmVNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjcnNhLXNoYTEiLz4KPGRzOlJlZmVyZW5jZSBVUkk9IiNJRF8yMDE1LTA2LTExVDIxLjE2LjU1Ljc5OFoiPgo8ZHM6VHJhbnNmb3Jtcz4KPGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNlbnZlbG9wZWQtc2lnbmF0dXJlIi8+CjxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biNXaXRoQ29tbWVudHMiLz4KPC9kczpUcmFuc2Zvcm1zPgo8ZHM6RGlnZXN0TWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI3NoYTEiLz4KPGRzOkRpZ2VzdFZhbHVlPmJZVy9lUU4wazdIVjkzRlQ3QUpDZTk0dEgvZz08L2RzOkRpZ2VzdFZhbHVlPgo8L2RzOlJlZmVyZW5jZT4KPC9kczpTaWduZWRJbmZvPgo8ZHM6U2lnbmF0dXJlVmFsdWU+CmZkdWtyZFhVUDV1T0VTK3YzaDVTWG9wQ3orMUhXZ0tzaXAya0R5Nm15TnZOOS9Ka1JTSklESUJzUUMyUEJtTEJXbndNekxDQWRRTDEKWEtpL2drT1cyL3FmMnc4ZnRBZTBHMHIzWldjVjZ4RkEvZldQaExiYVBIQXRBd1NENExqUWkvUVphcVc2d0JKLzlXZjBCZVVVVkFvLwpHU1p4VHpzMXpYVVRDYmd2aU5zPQo8L2RzOlNpZ25hdHVyZVZhbHVlPgo8L2RzOlNpZ25hdHVyZT48c2FtbDpTdWJqZWN0PjxzYW1sOk5hbWVJRCBGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjEuMTpuYW1laWQtZm9ybWF0OnVuc3BlY2lmaWVkIj4wNDM1Mjc0NDE3MjkxMDIwMTBDb21jYXN0LlVTUjRKUjwvc2FtbDpOYW1lSUQ+PHNhbWw6U3ViamVjdENvbmZpcm1hdGlvbiBNZXRob2Q9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpjbTpob2xkZXItb2Yta2V5Ij48c2FtbDpTdWJqZWN0Q29uZmlybWF0aW9uRGF0YSBOb3RCZWZvcmU9IjIwMTUtMDYtMTFUMjE6MTY6NTUuNzk4WiIgTm90T25PckFmdGVyPSIyMDE1LTA2LTEyVDAxOjE2OjU1Ljc5OFoiIFJlY2lwaWVudD0iaHR0cHM6Ly9sb2dpbi5jb21jYXN0Lm5ldC9hcGkvbG9naW4iLz48L3NhbWw6U3ViamVjdENvbmZpcm1hdGlvbj48L3NhbWw6U3ViamVjdD48c2FtbDpDb25kaXRpb25zIE5vdEJlZm9yZT0iMjAxNS0wNi0xMVQyMToxNjo1NS43OThaIiBOb3RPbk9yQWZ0ZXI9IjIwMTUtMDYtMTJUMDE6MTY6NTUuNzk4WiI+PHNhbWw6QXVkaWVuY2VSZXN0cmljdGlvbj48c2FtbDpBdWRpZW5jZT5odHRwczovL2xvZ2luLmNvbWNhc3QubmV0L2FwaS9sb2dpbjwvc2FtbDpBdWRpZW5jZT48L3NhbWw6QXVkaWVuY2VSZXN0cmljdGlvbj48L3NhbWw6Q29uZGl0aW9ucz48c2FtbDpBdXRoblN0YXRlbWVudCBBdXRobkluc3RhbnQ9IjIwMTUtMDYtMTFUMjE6MTY6NTUuNzk4WiIgU2Vzc2lvbk5vdE9uT3JBZnRlcj0iMjAxNS0wNi0xMlQwMToxNjo1NS43OThaIj48c2FtbDpBdXRobkNvbnRleHQ+PHNhbWw6QXV0aG5Db250ZXh0Q2xhc3NSZWY+dXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFjOmNsYXNzZXM6UGFzc3dvcmRQcm90ZWN0ZWRUcmFuc3BvcnQ8L3NhbWw6QXV0aG5Db250ZXh0Q2xhc3NSZWY+PHNhbWw6QXV0aG5Db250ZXh0RGVjbFJlZj51cm46Y29tY2FzdDpjaW1hOmxvZ2luOnJlc3Q6YWM6cGFzc3dvcmQ8L3NhbWw6QXV0aG5Db250ZXh0RGVjbFJlZj48L3NhbWw6QXV0aG5Db250ZXh0Pjwvc2FtbDpBdXRoblN0YXRlbWVudD48c2FtbDpBdHRyaWJ1dGVTdGF0ZW1lbnQ+PHNhbWw6QXR0cmlidXRlIE5hbWU9ImZpcnN0bmFtZSIgTmFtZUZvcm1hdD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmF0dHJuYW1lLWZvcm1hdDpiYXNpYyI+PHNhbWw6QXR0cmlidXRlVmFsdWUgeGk6dHlwZT0ieHM6c3RyaW5nIj5UUk9ZPC9zYW1sOkF0dHJpYnV0ZVZhbHVlPjwvc2FtbDpBdHRyaWJ1dGU+PHNhbWw6QXR0cmlidXRlIE5hbWU9Imxhc3RuYW1lIiBOYW1lRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXR0cm5hbWUtZm9ybWF0OmJhc2ljIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4aTp0eXBlPSJ4czpzdHJpbmciPkNJTUE8L3NhbWw6QXR0cmlidXRlVmFsdWU+PC9zYW1sOkF0dHJpYnV0ZT48c2FtbDpBdHRyaWJ1dGUgTmFtZT0iZW1haWxBZGRyZXNzIiBOYW1lRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXR0cm5hbWUtZm9ybWF0OmJhc2ljIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4aTp0eXBlPSJ4czpzdHJpbmciPkJfQ0lNQV9UUklQTEVfMDZAY29tY2FzdC5uZXQ8L3NhbWw6QXR0cmlidXRlVmFsdWU+PC9zYW1sOkF0dHJpYnV0ZT48c2FtbDpBdHRyaWJ1dGUgTmFtZT0iYXBwS2V5IiBOYW1lRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXR0cm5hbWUtZm9ybWF0OmJhc2ljIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4aTp0eXBlPSJ4czpzdHJpbmciPnRlc3RBcHA8L3NhbWw6QXR0cmlidXRlVmFsdWU+PC9zYW1sOkF0dHJpYnV0ZT48c2FtbDpBdHRyaWJ1dGUgTmFtZT0iY2lkIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4aTp0eXBlPSJ4czpiYXNlNjRCaW5hcnkiPmMreWNvMmU5Z0QxODZQVGxqTGsybXIyRTlnaz08L3NhbWw6QXR0cmlidXRlVmFsdWU+PC9zYW1sOkF0dHJpYnV0ZT48c2FtbDpBdHRyaWJ1dGUgTmFtZT0iY29zIiBOYW1lRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXR0cm5hbWUtZm9ybWF0OmJhc2ljIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4aTp0eXBlPSJ4czpzdHJpbmciPmhzaTwvc2FtbDpBdHRyaWJ1dGVWYWx1ZT48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4aTp0eXBlPSJ4czpzdHJpbmciPmNkdjwvc2FtbDpBdHRyaWJ1dGVWYWx1ZT48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4aTp0eXBlPSJ4czpzdHJpbmciPnZpZGVvPC9zYW1sOkF0dHJpYnV0ZVZhbHVlPjwvc2FtbDpBdHRyaWJ1dGU+PHNhbWw6QXR0cmlidXRlIE5hbWU9InN1YmplY3QtaWQiIE5hbWVGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphdHRybmFtZS1mb3JtYXQ6YmFzaWMiPjxzYW1sOkF0dHJpYnV0ZVZhbHVlIHhpOnR5cGU9InhzOnN0cmluZyI+MDQzNTI3NDQxNzI5MTAyMDEwQ29tY2FzdC5VU1I0SlI8L3NhbWw6QXR0cmlidXRlVmFsdWU+PC9zYW1sOkF0dHJpYnV0ZT48L3NhbWw6QXR0cmlidXRlU3RhdGVtZW50Pjwvc2FtbDpBc3NlcnRpb24+</LoginToken><Status Code=\"urn:comcast:login:api:rest:1.0:status:Success\"/></AuthnResponse>";
		String ltrFailXml = "<cima:AuthnResponse xmlns:cima=\"urn:comcast:login:api:v1.0\" version=\"1.0\"><cima:Status Code=\"urn:comcast:login:api:rest:1.0:status:KeyNotValid\" Message=\"Invalid application key\" /></cima:AuthnResponse>";
		String saml = "<saml:Assertion xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\" xmlns:ns4=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" ID=\"ID_2015-06-11T21.16.55.798Z\" IssueInstant=\"2015-06-11T21:16:55.801Z\" Version=\"2.0\"><saml:Issuer>https://login.comcast.net/api/login</saml:Issuer><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><ds:Reference URI=\"#ID_2015-06-11T21.16.55.798Z\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#WithComments\"/></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><ds:DigestValue>bYW/eQN0k7HV93FT7AJCe94tH/g=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>fdukrdXUP5uOES+v3h5SXopCz+1HWgKsip2kDy6myNvN9/JkRSJIDIBsQC2PBmLBWnwMzLCAdQL1XKi/gkOW2/qf2w8ftAe0G0r3ZWcV6xFA/fWPhLbaPHAtAwSD4LjQi/QZaqW6wBJ/9Wf0BeUUVAo/GSZxTzs1zXUTCbgviNs=</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\">043527441729102010Comcast.USR4JR</saml:NameID><saml:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:holder-of-key\"><saml:SubjectConfirmationData NotBefore=\"2015-06-11T21:16:55.798Z\" NotOnOrAfter=\"2015-06-12T01:16:55.798Z\" Recipient=\"https://login.comcast.net/api/login\"/></saml:SubjectConfirmation></saml:Subject><saml:Conditions NotBefore=\"2015-06-11T21:16:55.798Z\" NotOnOrAfter=\"2015-06-12T01:16:55.798Z\"><saml:AudienceRestriction><saml:Audience>https://login.comcast.net/api/login</saml:Audience></saml:AudienceRestriction></saml:Conditions><saml:AuthnStatement AuthnInstant=\"2015-06-11T21:16:55.798Z\" SessionNotOnOrAfter=\"2015-06-12T01:16:55.798Z\"><saml:AuthnContext><saml:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport</saml:AuthnContextClassRef><saml:AuthnContextDeclRef>urn:comcast:cima:login:rest:ac:password</saml:AuthnContextDeclRef></saml:AuthnContext></saml:AuthnStatement><saml:AttributeStatement><saml:Attribute Name=\"firstname\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\"><saml:AttributeValue xi:type=\"xs:string\">TROY</saml:AttributeValue></saml:Attribute><saml:Attribute Name=\"lastname\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\"><saml:AttributeValue xi:type=\"xs:string\">CIMA</saml:AttributeValue></saml:Attribute><saml:Attribute Name=\"emailAddress\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\"><saml:AttributeValue xi:type=\"xs:string\">B_CIMA_TRIPLE_06@comcast.net</saml:AttributeValue></saml:Attribute><saml:Attribute Name=\"appKey\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\"><saml:AttributeValue xi:type=\"xs:string\">testApp</saml:AttributeValue></saml:Attribute><saml:Attribute Name=\"cid\"><saml:AttributeValue xi:type=\"xs:base64Binary\">c+yco2e9gD186PTljLk2mr2E9gk=</saml:AttributeValue></saml:Attribute><saml:Attribute Name=\"cos\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\"><saml:AttributeValue xi:type=\"xs:string\">hsi</saml:AttributeValue><saml:AttributeValue xi:type=\"xs:string\">cdv</saml:AttributeValue><saml:AttributeValue xi:type=\"xs:string\">video</saml:AttributeValue></saml:Attribute><saml:Attribute Name=\"subject-id\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\"><saml:AttributeValue xi:type=\"xs:string\">043527441729102010Comcast.USR4JR</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>";
		
		try{
			System.out.println(saml);
			Map<XMLParserHelper.ParsedMapKeys, Object> map = XMLParserHelper.getXMLValueMap(satrFailXml, XMLParserHelper.XmlParsingObjectTypes.CIMA_SERVICE_ACCESS_TOKEN_RESPONSE);
			System.out.println(map);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*void getAuthToken(){
		try{
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.COMMON_CONTEXT_FILE_NAME);
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.CORE_CONTEXT_FILE_NAME);
			CimaLoginContextInitializer.initializeContext(CimaLoginContextInitializer.CONTEXT_FILE_NAME);
			Map<String, String> commonStringCacheParams = new HashMap<String, String>();
			commonStringCacheParams.put(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT, "QA");
			CoreCacheInitializer.initialize(commonStringCacheParams);
			
			Oauth2Handler handler = getOauth2Handler();
			initializeOauth2HandlerForAuthToken(handler, "cimalogintest", "code", "https://login.comcast.net/api/login#smartzone", "CIMA_CSG_HSD_03", "Comcast123", null);
			
			System.out.println("Starting to call oauth....");
			String result = handler.fetchAuthToken(true);
			System.out.println("result = "+result);
			System.out.println("Status = "+handler.getStatus());
			System.out.println("Error Description = "+handler.getErrorDescription());
			//oauth.fetchAccessToken();
			//JSONParserHelper.parseJSON(json_str, AccessTokenResponse.class);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				ObjectDestroyer.destroyAllCaches();
				CoreContextInitilizer.destroyContext();
			}catch(Exception e){e.printStackTrace();}
		}
	}*/
	
	/*public static void cryptoTVE(){
		try{
			CimaIdmContextInitializer.initializeContext(CimaIdmContextInitializer.COMMON_CONTEXT_FILE_NAME);
			ICitfCache configCache = getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			
			KeyPair kp = ((XMLSignatureGenerator)CimaIdmContextInitializer.getBean("xmlSignatureGenerator")).getKeyPairFromKeyStore(ICimaCommonConstants.JAVA_KEY_STORE_TVE, "citf_tve", "Comcast1234", "password", XMLSignatureGenerator.KeyStoreType.JKS);
			System.out.println("Private - "+kp.getPrivate()+" and public - "+kp.getPublic());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				CoreContextInitilizer.destroyContext();
			}catch(Exception e){e.printStackTrace();}
		}
	}*/
	
	
	public static void crypto(){
		try{
			EncryptionHandler h = new EncryptionHandler();
			h.setKeyManager(new KeyStoreManager());
			h.initializeEncrypter("citf-keystore.jks", "citf", "Comcast1234", "Comcast1234");
			h.decryptData(h.encryptData("comcast corp @#@$#!$#!%"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getSignature() {
		try {
			String sig = CodecUtility.getDigitalSignature("testApp testtest gvdccast", "QMpWea8O9uudJAqa1svyv8JaivtCVrI5gpPiplgHizg=", ICommonConstants.ENVIRONMENT_STAGE);
			//String sig = CodecUtility.getDigitalSignature("testApp Comcast123 suspendedLoginUsr01", "bcc89dd009ac03f54c56df4410502f756ce92b7afe547949f4a6c6211cebd27b", ICommonConstants.ENVIRONMENT_QA);
			System.out.println("Signature = " + sig);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}