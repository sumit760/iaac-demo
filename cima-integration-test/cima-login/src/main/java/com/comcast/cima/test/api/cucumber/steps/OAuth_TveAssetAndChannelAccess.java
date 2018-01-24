package com.comcast.cima.test.api.cucumber.steps;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.comcast.test.citf.common.xacml.XACMLRequestBuilder;
import com.comcast.test.citf.core.cucumber.steps.CoreCucumberSteps;
import com.comcast.test.citf.core.cucumber.steps.OAuthCommonSteps;
import com.comcast.test.citf.core.dataProvider.TveDataProvider;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class OAuth_TveAssetAndChannelAccess {
	
	@Autowired
	private OAuth_WebServerApplication_Flow oauthWebServerApplicationFlow; 
	
	@Autowired
	private CoreCucumberSteps coreCucumberSteps;
	
	@Autowired
	private OAuthCommonSteps oAuthCommonSteps;
	
	@Autowired
	private TveDataProvider tveDataProvider;
	
	
	private String tveChannelRequest = null;
	protected String getTveChannelRequest() {
		return this.tveChannelRequest;
	}
	protected void setTveChannelRequest(String inTveChannelRequest) {
		this.tveChannelRequest = inTveChannelRequest;
	}
	//
	private String tveChannelResponseBody = null;
	protected String getTveChannelResponseBody() {
		return this.tveChannelResponseBody;
	}
	protected void setTveChannelResponseBody(String inTveChannelResponseBody) {
		this.tveChannelResponseBody = inTveChannelResponseBody;
	}
	//
	private int tveChannelResponseCode = 0;
	protected int getTveChannelResponseCode() {
		return this.tveChannelResponseCode;
	}
	protected void setTveChannelResponseCode(int inTveChannelResponseCode) {
		this.tveChannelResponseCode = inTveChannelResponseCode;
	}
	//
	@And("^oauth client prepares and sends a XACML \\(no action\\) asset request by passing the access token$")
	public void oauthClientPreparesAndSendsXacmlNoActionAssetRequestByPassingTheAccessToken(
			List<Map<String, String>> inParams) throws Exception {
		//
		prepareXacmlAssetRequest(inParams, null);
		executeTveRequestAndReadResponse();
	}
	//
	@And("^oauth client prepares and sends a XACML view asset request by passing the access token$")
	public void oauthClientPreparesAndSendsXacmlViewAssetRequestByPassingTheAccessToken(
			List<Map<String, String>> inParams) throws Exception {
		//
		prepareXacmlViewAssetRequest(inParams);
		executeTveRequestAndReadResponse();
	}
	//
	@And("^oauth client prepares and sends a XACML view asset request with empty resource by passing the access token$")
	public void oauthClientPreparesAndSendsXacmlViewAssetRequestByPassingTheAccessToken() throws Exception {
		//
		this.tveChannelRequest = (new XACMLRequestBuilder()).createAssetRequestForOAUTH(
				new XACMLRequestBuilder.TveAssetResource[]{null}, getAction(VIEW));
		executeTveRequestAndReadResponse();
	}
	//
	@And("^prepare a XACML view asset request$")
	public void prepareXacmlViewAssetRequest(
			List<Map<String, String>> inParams) throws Exception {
		prepareXacmlAssetRequest(inParams, VIEW);
	}
	private void prepareXacmlAssetRequest(
			List<Map<String, String>> inParams, String inAction) throws Exception {
		//
		final String errorPrefix = "Column '";
		final String errorSuffix = "' must be present and have a non-null value";
		//
		List<XACMLRequestBuilder.TveAssetResource> tarList = new ArrayList<XACMLRequestBuilder.TveAssetResource>();
		for (Map<String, String> nextParams : inParams) {
			String tvNetwork = nextParams.get("TV Network");
			String resourceId = nextParams.get("Resource ID");
			String namespace = nextParams.get("Namespace");
			String vChip = nextParams.get("V-Chip");
			String mpaa = nextParams.get("MPAA");
			String assetTitle = nextParams.get("Asset Title");
			//
			XACMLRequestBuilder.TveAssetResource nextTar;
			if (tvNetwork == null) {
				throw new IllegalArgumentException(errorPrefix + "TV Network" + errorSuffix);
			} else if (resourceId == null) {
				throw new IllegalArgumentException(errorPrefix + "Resource ID" + errorSuffix);
			} else if (namespace == null) {
				throw new IllegalArgumentException(errorPrefix + "Namespace" + errorSuffix);
			} else if (assetTitle == null) {
				throw new IllegalArgumentException(errorPrefix + "Asset Title" + errorSuffix);
			} else if (vChip != null && mpaa != null) {
				throw new IllegalArgumentException(
						"Columns '" + "V-Chip" + "' and '" + "MPAA" + "' cannot both be present with non-null values");
			} else if (vChip != null) {
				nextTar = XACMLRequestBuilder.TveAssetResource.newInstanceForVChip(
						tvNetwork, resourceId, namespace, vChip, assetTitle);
			} else if (mpaa != null) {
				nextTar = XACMLRequestBuilder.TveAssetResource.newInstanceForMpaa(
						tvNetwork, resourceId, namespace, mpaa, assetTitle);
			} else {
				throw new IllegalArgumentException(
						"Either (but not both) '" + "V-Chip" + "' or '" + "MPAA"
								+ "' column must be present with non-null value");
			}
			//
			tarList.add(nextTar);
		}
		this.tveChannelRequest = (new XACMLRequestBuilder()).createAssetRequestForOAUTH(
				tarList.toArray(new XACMLRequestBuilder.TveAssetResource[tarList.size()]), getAction(inAction));
	}
	@And("^malform existing XACML request$")
	public void malformExistingXacmlRequest() {
		inTheTveChannelRequestSearchForAndReplaceItWith("<xacml-context:Request", "MalformedRequest");
	}
	@And("^change the XACML request by substituting a view action with an invalid one$")
	public void changeTheXacmlRequestBySubstitutingViewActionWithInvalidOne() {
		inTheTveChannelRequestSearchForAndReplaceItWith(VIEW, "INVALID");
	}
	@And("^change the XACML request by substituting a view action with an empty one$")
	public void changeTheXacmlRequestBySubstitutingViewActionWithEmptyOne() {
		inTheTveChannelRequestSearchForAndReplaceItWith(VIEW, "");
	}
	@And("^prepare a XACML view channel request for '(.*)'$")
	public void prepareTveChannelRequestOfTypeForChannel(String inChannel) throws Exception {
		prepareTveChannelRequestOfTypeForChannels(VIEW, inChannel);
		executeTveRequestAndReadResponse();
	}
	@And("^oauth client prepares and sends a XACML view channel request for '(.*)' by passing the access token$")
	public void prepareTveChannelRequestOfViewTypeForChannel(String inChannel) throws Exception {
		prepareTveChannelRequestOfTypeForChannels(VIEW, inChannel);
		executeTveRequestAndReadResponse();
	}
	@And("^oauth client prepares and sends a XACML view channel request for no channels by passing the access token$")
	public void prepareTveChannelRequestOfViewTypeForChannel() throws Exception {
		prepareTveChannelRequestOfTypeForChannels(VIEW);
		executeTveRequestAndReadResponse();
	}
	@And("^oauth client prepares and sends a XACML \\(no action\\) channel request for '(.*)' by passing the access token$")
	public void prepareTveChannelRequestOfNoTypeForChannel(String inChannel) throws Exception {
		prepareTveChannelRequestOfTypeForChannels(null, inChannel);
		executeTveRequestAndReadResponse();
	}
	@And("^oauth client prepares and sends a XACML view channel request for channels '(.*)' and '(.*)' by passing the access token$")
	public void prepareTveChannelRequestOfTypeForChannelsAnd(
			String inChannelOne, String inChannelTwo) throws Exception {
		prepareTveChannelRequestOfTypeForChannels(VIEW, inChannelOne, inChannelTwo);
		executeTveRequestAndReadResponse();
	}
	private void prepareTveChannelRequestOfTypeForChannels(
			String inType, String... inChannels) throws Exception {
		this.tveChannelRequest = (new XACMLRequestBuilder()).createChannelRequestForOAUTH(inChannels, getAction(inType));
	}
	@And("^in the TVE Channel request, search for '(.*)' and replace it with '(.*)'$")
	public void inTheTveChannelRequestSearchForAndReplaceItWith(
			String inCurrentText, String inUpdatedText) {
		this.tveChannelRequest = this.tveChannelRequest.replaceAll(inCurrentText, inUpdatedText);
	}
	@And("^confirm that the status code of the last TVE call is (\\d+)$")
	public void confirmThatTheStatusCodeOfTheLastCallIs(int inStatusCode) throws Exception {
		if (this.tveChannelResponseCode != inStatusCode) {
			throw new IllegalStateException("The last web call had a response code of "
					+ this.tveChannelResponseCode + " instead of " + inStatusCode);
		}
	}
	@And("^execute TVE request and read response$")
	public void executeTveRequestAndReadResponse() throws Exception {
		try {
			//
			String bearerToken = this.oAuthCommonSteps.getOverrideAccessToken();
			if (bearerToken == null) {
				bearerToken = this.oauthWebServerApplicationFlow.getAccessTokenResponse().getAccessToken();
			}
			//
			HttpPost httpPost = new HttpPost(tveDataProvider.getRequestUrl());
			httpPost.setEntity(new StringEntity(this.tveChannelRequest));
			httpPost.addHeader("Authorization", "Bearer " + bearerToken);
			HttpClient instance = HttpClientBuilder.create().build();
			HttpResponse r = instance.execute(httpPost);
			this.tveChannelResponseBody = new BasicResponseHandler().handleResponse(r);
			this.tveChannelResponseCode = r.getStatusLine().getStatusCode();
		} catch (org.apache.http.client.HttpResponseException hre) {
			this.tveChannelResponseCode = hre.getStatusCode();
		}
	}
	@Then("^a TVE customer with parental control thresholds 'G' and 'TV-G'$")
	public void aTveCustomerWithParentalControlThresholdGAndTVG() throws Exception {
		this.coreCucumberSteps.setUserIdAndPassword(tveDataProvider.getUser1Username(), tveDataProvider.getUser1Password());
	}
	@Then("^a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'$")
	public void aDisneyCustomer() throws Exception {
		this.coreCucumberSteps.setUserIdAndPassword(tveDataProvider.getUser2Username(), tveDataProvider.getUser2Password());
	}
	@Then("^confirm that the (.*) TVE 'Result' has a 'Decision' of '(.*)'$")
	public void confirmThatTheResultHasADecisionOf(
			String inOridinalNumber, String inDecision) throws Exception {
		confirmXpathValue(
				"/Response/Result["
						+ convertOrdinalNumberToCardinalNumber(inOridinalNumber)
						+ "]/Decision/text()",
				inDecision);
	}
	@Then("^confirm that the (.*) TVE 'Result' has an 'Attribute' with name-value pair \\('(.*)','(.*)'\\)$")
	public void confirmThatTheResultHasADecisionOf(
			String inOridinalNumber, String inName, String inValue) throws Exception {
		confirmXpathValue(
				"/Response/Result["
						+ convertOrdinalNumberToCardinalNumber(inOridinalNumber)
						+ "]/Obligations/Obligation/AttributeAssignment["
						+"@AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:ratings:"
						+ inName + "\"]/text()",
				inValue);
	}
	private void confirmXpathValue(String inQuery, String inValue) throws Exception {
		//
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(getTveChannelResponseBody()));
		Document doc = db.parse(is);
		//
		XPath nextXpath =  XPathFactory.newInstance().newXPath();
		String nextResult = nextXpath.compile(inQuery).evaluate(doc);
		if (!inValue.equals(nextResult)) {
			throw new IllegalStateException("For XPath query '" + inQuery + "', the expected result was '"
					+ inValue + "', but the actual result was '" + nextResult
					+ "' for the following response (with status code " + getTveChannelResponseCode() + ")\n"
					+ getTveChannelResponseBody());
		}
	}
	//
	//
	private int convertOrdinalNumberToCardinalNumber(String inValue) {
		int outValue;
		final String[] supportedCardinalNumbers = new String[]{"1st", "2nd", "3rd"};
		outValue = Arrays.asList(supportedCardinalNumbers).indexOf(inValue) + 1;
		if (outValue == 0) {
			throw new IllegalArgumentException("Unsupported ordinal value '" + inValue
					+ "', submitted value must be one of " + Arrays.toString(supportedCardinalNumbers));
		}
		return outValue;
	}
	private XACMLRequestBuilder.Actions getAction(String inType) {
		XACMLRequestBuilder.Actions outValue = null;
		if (inType != null) {
			try {
				outValue = XACMLRequestBuilder.Actions.valueOf(inType);
			} catch (IllegalArgumentException iae) {
				throw new IllegalArgumentException("TVE request of type '" + inType + "' isn't valid", iae);
			}
		}
		return outValue;
	}
	
	private static final String VIEW = "VIEW";
}
