package com.comcast.test.citf.core.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.wink.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.cima.jsonObjs.OAuthAccessTokenInfo;
import com.comcast.test.citf.common.cima.jsonObjs.OAuthIDTokenInfo;
import com.comcast.test.citf.core.dataProvider.ScopeValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

/**
 * @author Valdas Sevelis
 */
@Service("oauthHelper")
public class OAuthHelper{
	
	@Autowired
	private ScopeValueProvider scopeDataProvider;

	/**
	 * URL Query separator
	 */
	private static final String QUERY_SEPARATOR = "&";

	/**
	 * Value separator in URL Query
	 */
	private static final String VALUE_SEPARATOR = "=";

	private final ObjectMapper objectMapper = new ObjectMapper();

	private URI tokenInfoUrl;

	public void setTokenInfoUrl(String tokenInfoUrl) {
		this.tokenInfoUrl = URI.create(tokenInfoUrl);
	}

	public OAuthAccessTokenInfo verifyAccessToken(String accessToken) {
		String accessTokenJson = new RestClient()
				.resource(tokenInfoUrl)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.queryParam("access_token", accessToken)
				.get(String.class);
		try {
			return objectMapper.readValue(accessTokenJson, OAuthAccessTokenInfo.class);
		} catch (IOException e) {
			throw new IllegalStateException("Can not verify access token", e);
		}
	}

	public OAuthIDTokenInfo verifyIdToken(String idToken) {
		String idTokenJson = new RestClient()
				.resource(tokenInfoUrl)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.queryParam("id_token", idToken)
				.get(String.class);
		try {
			return objectMapper.readValue(idTokenJson, OAuthIDTokenInfo.class);
		} catch (IOException e) {
			throw new IllegalStateException("Can not verify id token", e);
		}
	}

	/**
	 * Parses URL fragment as per OAuth 2.0 fragment rules &mdash; parameters/values
	 * are encoded using {@code application/x-www-form-urlencoded} content
	 * encoding rules
	 *
	 * @param fragment URL fragment
	 * @return parsed parameter values
	 */
	public Map<String, String> parseOAuthUrlFragment(String fragment) {
		final Map<String, String> keyValueMap = Splitter
				.on(QUERY_SEPARATOR)
				.omitEmptyStrings()
				.withKeyValueSeparator(VALUE_SEPARATOR)
				.split(fragment);

		try {
			final Map<String, String> resultMap = new HashMap<>(keyValueMap.size());
			for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
				final String cleanedValue =
						Strings.emptyToNull(
								URLDecoder.decode(
										Strings.nullToEmpty(entry.getValue()).trim(),
										Charsets.UTF_8.name()));
				if (cleanedValue != null) {
					resultMap.put(entry.getKey(), cleanedValue);
				}
			}
			return resultMap;
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Splits string of space-separated scopes into a list
	 * @param scopes space-separated scope list
	 * @return list of scopes
	 */
	public List<String> parseScopeString(String scopes) {
		return Splitter.on(' ').omitEmptyStrings().splitToList(scopes);
	}
	
	public List<String> parseScopeKeysToString(String scopeKeys) {
		List<String> scopeValues = null;
		List<String> scopeKeyList = parseScopeString(scopeKeys);
		
		if(scopeKeyList!=null){
			scopeValues = new ArrayList<String>();
			for(String scopeKey : scopeKeyList){
				scopeValues.add(scopeDataProvider.getIdmScopeValue(scopeKey));
			}
		}
		
		return scopeValues;
	}
}
