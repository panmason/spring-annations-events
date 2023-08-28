package cn.com.cakelite.tartcaker;

import cn.com.cakelite.tartcaker.config.AnnotationConfiguration;
import cn.com.cakelite.tartcaker.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationBeanTest {

    @Test
    void annotationBeanTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }

        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);
    }

    @Test
    void scopeBeanTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AnnotationConfiguration.class);

        Person personNew1 = (Person) applicationContext.getBean("personNew");
        Person personNew2 = (Person) applicationContext.getBean("personNew");

        System.out.println(personNew1 == personNew2);

        Person personOne1 = (Person) applicationContext.getBean("personOne");
        Person personOne2 = (Person) applicationContext.getBean("personOne");

        System.out.println(personOne1 == personOne2);

    }

}
