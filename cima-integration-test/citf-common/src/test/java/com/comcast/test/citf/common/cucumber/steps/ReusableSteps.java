package com.comcast.test.citf.common.cucumber.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.comcast.test.citf.common.cucumber.beans.RandomNumberGenerator;

import cucumber.api.java.en.When;

@ContextConfiguration("classpath:cucumber_test.xml")
public class ReusableSteps  {
	
	@Autowired
	private RandomNumberGenerator randomGen;
	
	@When("^set a new random number $")
	public void setNewRandomNumber() {
		if(randomGen!=null){
			randomGen.setRandomNumber(-1);
		}
	}
	
	public int getRandomNo() {
		return randomGen!=null?randomGen.getRandomNumber():0;
	}
	
	public RandomNumberGenerator getRandomNumberGenerator(){
		return randomGen;
	}
}
