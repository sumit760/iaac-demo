package com.comcast.test.citf.common.cucumber;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.comcast.test.citf.common.cucumber.beans.RandomNumberGenerator;
import com.comcast.test.citf.common.cucumber.steps.ReusableSteps;
import com.comcast.test.citf.common.cucumber.steps.SampleSteps;

import cucumber.api.java.ObjectFactory;

/**
 * 
 * @author arej001c
 * 
 * This is a unit test class to test the Simple Thread scope functionality with CITF. 
 * As this CucumberSpringObjectFactory is based of cucumber-spring, so the common cucumber 
 * steps instantiation tests have not been included here.
 *
 */
public class CucumberSpringFactoryTest {

	@Test
	public void shouldGiveSameStepInstancesForEachStepInASingleThread() {
		final ObjectFactory factory = new CucumberSpringFactory();
		factory.addClass(ReusableSteps.class);
		factory.addClass(SampleSteps.class);
		factory.start();

		final ReusableSteps o1 = factory.getInstance(ReusableSteps.class);
		final ReusableSteps o2 = factory.getInstance(SampleSteps.class).getIndependentReusableStep();

		factory.stop();

		assertNotNull(o1);
		assertNotNull(o2);
		assertSame(o1, o2);
	}


	@Test
	public void shouldGiveDifferentStepInstancesForEachStepInDifferentThreads() {
		final ObjectFactory factory = new CucumberSpringFactory();
		factory.addClass(ReusableSteps.class);

		// thread 1
		Thread.currentThread().setName("Thread1");
		factory.start();
		final ReusableSteps o1 = factory.getInstance(ReusableSteps.class);
		factory.stop();
		
		// thread 2
		Thread.currentThread().setName("Thread2");
		factory.start();
		final ReusableSteps o2 = factory.getInstance(ReusableSteps.class);
		factory.stop();

		assertNotNull(o1);
		assertNotNull(o2);
		assertNotSame(o1, o2);
	}


	@Test
	public void shouldGiveSameApplicationBeanValuesForEachStepInASingleThread() {
		final ObjectFactory factory = new CucumberSpringFactory();
		factory.addClass(ReusableSteps.class);
		factory.addClass(SampleSteps.class);
		factory.start();

		final SampleSteps sp = factory.getInstance(SampleSteps.class);
		final RandomNumberGenerator rng1 = sp.getRandomNumberGenerator();

		sp.genRandomNumber();
		int oldNumber = sp.getRandomNo();

		final ReusableSteps rs = factory.getInstance(ReusableSteps.class);
		final RandomNumberGenerator rng2 = rs.getRandomNumberGenerator();

		rs.setNewRandomNumber();
		int newNumber = rs.getRandomNo();

		factory.stop();

		assertNotNull(sp);
		assertNotNull(rs);
		assertNotNull(rng1);
		assertNotNull(rng2);
		assertSame(rng1, rng2);
		assertSame(oldNumber==newNumber, true);
	}


	@Test
	public void shouldGivedifferentApplicationBeanValuesForEachStepInDifferentThreads() {
		final ObjectFactory factory = new CucumberSpringFactory();
		factory.addClass(ReusableSteps.class);
		factory.addClass(SampleSteps.class);

		Thread.currentThread().setName("Thread1");
		factory.start();
		final SampleSteps sp = factory.getInstance(SampleSteps.class);
		final RandomNumberGenerator rng1 = sp.getRandomNumberGenerator();

		sp.genRandomNumber();
		int oldNumber = sp.getRandomNo();
		factory.stop();
		
		Thread.currentThread().setName("Thread2");
		factory.start();
		final ReusableSteps rs = factory.getInstance(ReusableSteps.class);
		final RandomNumberGenerator rng2 = rs.getRandomNumberGenerator();

		rs.setNewRandomNumber();
		int newNumber = rs.getRandomNo();
		factory.stop();

		assertNotNull(sp);
		assertNotNull(rs);
		assertNotNull(rng1);
		assertNotNull(rng2);
		assertNotSame(rng1, rng2);
		assertSame(oldNumber==newNumber, false);
	}
}
