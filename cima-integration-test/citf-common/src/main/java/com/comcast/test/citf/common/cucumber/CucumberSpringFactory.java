package com.comcast.test.citf.common.cucumber;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestContextManager;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

import cucumber.api.java.ObjectFactory;
import cucumber.runtime.CucumberException;

/**
 * @author Abhijit Rej
 * @since March 2016
 *
 * This class is used as Object Factory to be used by Cucumber while creating new cucumber steps related objects. 
 */
public class CucumberSpringFactory implements ObjectFactory {

    private ConfigurableListableBeanFactory beanFactory;
    private CucumberTestContextManager testContextManager;

    private final Collection<Class<?>> stepClasses = new HashSet<Class<?>>();
    private Class<?> stepClassWithSpringContext = null;

    public CucumberSpringFactory() {
    }

    @Override
    public boolean addClass(final Class<?> stepClass) {
        if (!stepClasses.contains(stepClass)) {
            if (dependsOnSpringContext(stepClass)) {
                if (stepClassWithSpringContext == null) {
                    stepClassWithSpringContext = stepClass;
                } else {
                    checkAnnotationsEqual(stepClassWithSpringContext, stepClass);
                }
            }
            stepClasses.add(stepClass);
            LOGGER.debug("New step class {} has been added in spring context.", stepClass.getName());
        }
        return true;
    }

    private void checkAnnotationsEqual(Class<?> stepClassWithSpringContext, Class<?> stepClass) {
        Annotation[] annotations1 = stepClassWithSpringContext.getAnnotations();
        Annotation[] annotations2 = stepClass.getAnnotations();
        if (annotations1.length != annotations2.length) {
            throw new CucumberException("Annotations differs on glue classes found: " +
                    stepClassWithSpringContext.getName() + ", " +
                    stepClass.getName());
        }
        for (Annotation annotation : annotations1) {
            if (!isAnnotationInArray(annotation, annotations2)) {
                throw new CucumberException("Annotations differs on glue classes found: " +
                        stepClassWithSpringContext.getName() + ", " +
                        stepClass.getName());
            }
        }
        LOGGER.debug("Annotation Equal check passed.");
    }

    private boolean isAnnotationInArray(Annotation annotation, Annotation[] annotations) {
        for (Annotation annotationFromArray: annotations) {
            if (annotation.equals(annotationFromArray)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        if (stepClassWithSpringContext != null) {
            testContextManager = new CucumberTestContextManager(stepClassWithSpringContext);
        } else {
            if (beanFactory == null) {
                createFallbackContext();
            }
        }
        notifyContextManagerAboutTestClassStarted();
        if (beanFactory == null || isNewContextCreated()) {
            beanFactory = testContextManager.getBeanFactory();
            for (Class<?> stepClass : stepClasses) {
                registerStepClassBeanDefinition(beanFactory, stepClass);
            }
        }
        LOGGER.info("Cucumber Spring Factory started successfully.");
    }

    @SuppressWarnings("resource")
    private void createFallbackContext() {
        ConfigurableApplicationContext applicationContext;
        if (getClass().getClassLoader().getResource(CUCUMBER_SPRING_CONETXT_FILE_NAME) != null) {
            applicationContext = new ClassPathXmlApplicationContext(CUCUMBER_SPRING_CONETXT_FILE_NAME);
        } else {
            applicationContext = new GenericApplicationContext();
        }
        applicationContext.registerShutdownHook();
        beanFactory = applicationContext.getBeanFactory();
        beanFactory.registerScope(ICimaCommonConstants.CUCUMBER_SCOPE_NAME, new SimpleThreadScope());
        for (Class<?> stepClass : stepClasses) {
            registerStepClassBeanDefinition(beanFactory, stepClass);
        }
        
        LOGGER.debug("Fallback Context created.");
    }

    private void notifyContextManagerAboutTestClassStarted() {
        if (testContextManager != null) {
            try {
                testContextManager.beforeTestClass();
            } catch (Exception e) {
                throw new CucumberException(e.getMessage(), e);
            }
        }
    }

    private boolean isNewContextCreated() {
        if (testContextManager == null) {
            return false;
        }
        return !beanFactory.equals(testContextManager.getBeanFactory());
    }

    private void registerStepClassBeanDefinition(ConfigurableListableBeanFactory beanFactory, Class<?> stepClass) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        BeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(stepClass)
                .setScope(ICimaCommonConstants.CUCUMBER_SCOPE_NAME)
                .getBeanDefinition();
        registry.registerBeanDefinition(stepClass.getName(), beanDefinition);
        
        LOGGER.debug("Step Class Bean Definition has been registered in Cucumber Spring Factory.");
    }

    @Override
    public void stop() {
        notifyContextManagerAboutTestClassFinished();
    }

    private void notifyContextManagerAboutTestClassFinished() {
        if (testContextManager != null) {
            try {
                testContextManager.afterTestClass();
            } catch (Exception e) {
                throw new CucumberException(e.getMessage(), e);
            }
        }
    }

    @Override
    public <T> T getInstance(final Class<T> type) {
        try {
            return beanFactory.getBean(type);
        } catch (BeansException e) {
            throw new CucumberException(e.getMessage(), e);
        }
    }

    private boolean dependsOnSpringContext(Class<?> type) {
        boolean hasStandardAnnotations = annotatedWithSupportedSpringRootTestAnnotations(type);

        if(hasStandardAnnotations) {
            return true;
        }

        final Annotation[] annotations = type.getDeclaredAnnotations();
        return (annotations.length == 1) && annotatedWithSupportedSpringRootTestAnnotations(annotations[0].annotationType());
    }

    private boolean annotatedWithSupportedSpringRootTestAnnotations(Class<?> type) {
        return type.isAnnotationPresent(ContextConfiguration.class)
            || type.isAnnotationPresent(ContextHierarchy.class);
    }
    
    private static final String CUCUMBER_SPRING_CONETXT_FILE_NAME = "cucumber.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(CucumberSpringFactory.class);
}

class CucumberTestContextManager extends TestContextManager {

    public CucumberTestContextManager(Class<?> testClass) {
        super(testClass);
        registerSimpleThreadScope(getContext());
    }

    public ConfigurableListableBeanFactory getBeanFactory() {
        return getContext().getBeanFactory();
    }

    private ConfigurableApplicationContext getContext() {
        return (ConfigurableApplicationContext)getTestContext().getApplicationContext();
    }

    private void registerSimpleThreadScope(ConfigurableApplicationContext context) {
    	ConfigurableApplicationContext modifiedContext = context;
        do {
        	modifiedContext.getBeanFactory().registerScope(ICimaCommonConstants.CUCUMBER_SCOPE_NAME, new SimpleThreadScope());
        	modifiedContext = (ConfigurableApplicationContext)modifiedContext.getParent();
        } while (modifiedContext != null);
        
        LOGGER.info("{} scope has been registered in Cucumber Spring Factory.", ICimaCommonConstants.CUCUMBER_SCOPE_NAME);
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CucumberTestContextManager.class);
}
