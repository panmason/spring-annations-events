package cn.com.cakelite.tartcaker.config;

import cn.com.cakelite.tartcaker.model.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnnotationConfiguration {

    @Bean
    public Person person() {
        return new Person("peter", 30);
    }

}
