package cn.com.cakelite.tartcaker.colors;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class Green {



    @PostConstruct
    public void initMethod() {
        System.out.println("@PostConstruct");
    }

    @PreDestroy
    public void preDestroyMethod() {
        System.out.println("@PreDestroy");
    }

}
