package cn.com.cakelite.tartcaker;

import cn.com.cakelite.tartcaker.config.ComponentScanConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationComponsentScanTest {

    @Test
    void annotationComponentScanTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ComponentScanConfiguration.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }
    }


    @Test
    void factoryBeanTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ComponentScanConfiguration.class);
        Object colorFactoryBean = applicationContext.getBean("colorFactoryBean");
        System.out.println(colorFactoryBean.getClass());

        Object colorFactoryBean1 = applicationContext.getBean("&colorFactoryBean");
        System.out.println(colorFactoryBean1.getClass());
    }
}
