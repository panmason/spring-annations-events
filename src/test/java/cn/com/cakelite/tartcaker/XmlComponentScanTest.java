package cn.com.cakelite.tartcaker;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlComponentScanTest {

    @Test
    void xmlComponentScanBeanTest() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-component-scan.xml");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }
    }

}
