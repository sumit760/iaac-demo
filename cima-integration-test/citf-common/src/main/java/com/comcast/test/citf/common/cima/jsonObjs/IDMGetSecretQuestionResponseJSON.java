package com.comcast.test.citf.common.cima.jsonObjs;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Bean class for IDM secret questions response.
 * 
 * @author Abhijit Rej (arej001c)
 * @since November 2015
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IDMGetSecretQuestionResponseJSON {

	@JsonProperty("FAVORITE_BEVERAGE")
    private String FAVORITE_BEVERAGE;

	@JsonProperty("FIRST_CAR")
    private String FIRST_CAR;
	
	
	@JsonProperty("FIRST_JOB")
    private String FIRST_JOB;
	
	@JsonProperty("NIECE_NEPHEW")
    private String NIECE_NEPHEW;
	
	@JsonProperty("HONEYMOON")
    private String HONEYMOON;
	
	@JsonProperty("SPORTS_TEAM")
    private String SPORTS_TEAM;
	
	@JsonProperty("BEST_FRIEND")
    private String BEST_FRIEND;
	
	@JsonProperty("FAVORITE_TEACHER")
    private String FAVORITE_TEACHER;
	
	@JsonProperty("PET_NAME")
    private String PET_NAME;
	
	
	@JsonProperty("FAVORITE_MOVIE")
    private String FAVORITE_MOVIE;
	
	
	/*JSONPObject obj = new JSONPObject(BEST_FRIEND, );*/
	
	
	
	
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	@JsonAnyGetter
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
}
