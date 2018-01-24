package com.comcast.test.citf.common.cucumber.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.comcast.test.citf.common.cucumber.beans.RandomNumberGenerator;

import cucumber.api.java.en.When;

@ContextConfiguration("classpath:cucumber_test.xml")
public class SampleSteps {
	
	@Autowired
	private ReusableSteps reusableStep;
	
	@Autowired
	private RandomNumberGenerator randomGen;
	
	@When("^generate a random number in range $")
	public void genRandomNumber() {
		if(randomGen!=null){
			randomGen.generateRandomNumber(1000);
		}
	}
	
	public int getRandomNo() {
		return randomGen!=null?randomGen.getRandomNumber():0;
	}
	
	public ReusableSteps getIndependentReusableStep(){
		return reusableStep;
	}
	
	public RandomNumberGenerator getRandomNumberGenerator(){
		return randomGen;
	}
}
