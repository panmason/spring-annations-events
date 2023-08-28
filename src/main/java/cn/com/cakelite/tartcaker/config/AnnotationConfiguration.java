package cn.com.cakelite.tartcaker.config;

import cn.com.cakelite.tartcaker.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

@Configuration
public class AnnotationConfiguration {

    @Bean
    public Person person() {
        return new Person("peter", 30);
    }

    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean("personNew")
    public Person personNew() {
        System.out.println("peterNew created");
        return new Person("peterNew", 30);
    }

    @Lazy
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean("personOne")
    public Person personDog() {
        System.out.println("peterOne created");
        return new Person("personOne", 30);
    }
}
