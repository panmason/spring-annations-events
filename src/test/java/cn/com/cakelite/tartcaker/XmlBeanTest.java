package cn.com.cakelite.tartcaker;

import cn.com.cakelite.tartcaker.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlBeanTest {

    @Test
    void xmlConfigBeanTest() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }

        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);

    }

}
