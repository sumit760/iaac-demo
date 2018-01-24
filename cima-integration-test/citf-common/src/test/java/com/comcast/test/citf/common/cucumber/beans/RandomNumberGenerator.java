package com.comcast.test.citf.common.cucumber.beans;

import java.util.Random;

public class RandomNumberGenerator {
	
	private int rand;

	public void generateRandomNumber(int range) {
		Random randomGenerator = new Random();
		this.rand = randomGenerator.nextInt(range);
	}
	
	public void setRandomNumber(int number) {
		if(this.rand<0){
			this.rand = number;
		}
	}
	
	public int getRandomNumber() {
		return this.rand;
	}
}
