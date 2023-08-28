# Spring中bean的装配方式

Spring中的`ApplicationContext`是Spring的上文，也是Spring容器初始化方式。

下面主要介绍基于`ClassPathXmlApplicationContext`与`AnnotationConfigApplicationContext`两种方式初始化与装配Bean。

创建基础对象[Person](../src/main/java/cn/com/cakelite/tartcaker/model/Person.java)，演示将该类装配到容器中。

```java
public class Person {
    private String name;
    private int age;
    
    // 省略方法，详细请看示例代码
}
```

## 1.使用配置文件进行装配

使用配置文件进行Bean的装配需要配合Spring的配置文件进行装配，Spring配置文件如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="person" class="cn.com.cakelite.tartcaker.model.Person">
        <property name="name" value="peter" />
        <property name="age" value="21" />
    </bean>

</beans>
```
在以上配置文件中，我们通过`bean`标签来指定了bean的id与bean的实现类，通过`property`标签通过调用class中`set`方法对对象属性进行注入。在编写好配置文件后，使用测试类进行测试。

测试代码详见[[XmlBeanTest.java](../src/test/java/cn/com/cakelite/tartcaker/XmlBeanTest.java)]

```java
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

```
## 2.使用注解进行装配

在SpringBoot中全部使用注解进行bean的装配，Spring的配置类如下[AnnotationConfiguration.java](../src/main/java/cn/com/cakelite/tartcaker/config/AnnotationConfiguration.java)：

```java
package cn.com.cakelite.tartcaker.config;

import cn.com.cakelite.tartcaker.model.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnnotationConfiguration {

    @Bean
    public Person person() {
        return new Person("peter", 30);
    }

}

```

在使用注解进行装配Bean时，我们需要指定一个配置类，在该类上标注`@Configuration`，告知Spring该类是一个的配置类，在该类中使用`@Bean`注解来完成一个Bean对象创建。

测试代码详见[AnnotationBeanTest.java](../src/test/java/cn/com/cakelite/tartcaker/AnnotationBeanTest.java)

```java
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

}
```

## 3.装配小结

在上述两个装配样例中我们分别使用了`ClassPathXmlApplicationContext`与`AnnotationConfigApplicationContext`两个Spring容器初始化的方式。

对于要装配的`Person`类我们均可以在Spring的容器中获取到对应的注册实例，但是基于`AnnotationConfigApplicationContext`的容器会额外添加一下组件来辅助初始化。

```
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
annotationConfiguration
```

基于配置文件进行没有以上的组件进行配合注册

## 4.基于Scope的作用域

`@Scope`是用来标记Bean的作用域，Spring默认的Bean是单例配置，在某些场景下需要使用Bean是多例的，在使用配置文件时，通过scope属性进行指定，在注解装配时使用`@Scope`进行标记。

作用域有以下几种：

`ConfigurableBeanFactory#SCOPE_PROTOTYPE` 非单例模式，每次获取都会产生一个新的实例对象。

`ConfigurableBeanFactory#SCOPE_SINGLETON` 单例模式，除特殊标记外，会在Spring的容器启动时创建并添加到Spring容器中。

`org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST` 在Web环境中使用，不常用，一般需求会方式Request域中。

`org.springframework.web.context.WebApplicationContext#SCOPE_SESSION`  在Web环境中使用，不常用，一般需求会方式Session域中。

我们在[AnnotationConfiguration.java](../src/main/java/cn/com/cakelite/tartcaker/config/AnnotationConfiguration.java)中配置两个Bean：

```java
package cn.com.cakelite.tartcaker.config;

import cn.com.cakelite.tartcaker.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AnnotationConfiguration {

    @Bean
    public Person person() {
        return new Person("peter", 30);
    }

    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean("personNew")
    public Person personNew() {
        return new Person("peterNew", 30);
    }

    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean("personOne")
    public Person personDog() {
        return new Person("peterOne", 30);
    }
}

```

通过多次获取`personNew`与`personOne`进行比对值，可以得出单例模式下，Bean只会创建一次，而在多例模式下，Bean在每次获取都会创建一次。


[AnnotationBeanTest.java](../src/test/java/cn/com/cakelite/tartcaker/AnnotationBeanTest.java)中的`scopeBeanTest()`

```java
package cn.com.cakelite.tartcaker;

import cn.com.cakelite.tartcaker.config.AnnotationConfiguration;
import cn.com.cakelite.tartcaker.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationBeanTest {

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

```


## 5.`@Lazy`的使用

对于上面的单例Bean与多例Bean是何时加载到容器中的呢，我们可以对Bean的配置方法进行修改来进行观察，在`personNew`与`personOne`构建前添加输出来观察对应的创建时机。

```java
package cn.com.cakelite.tartcaker.config;

import cn.com.cakelite.tartcaker.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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

    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean("personOne")
    public Person personDog() {
        System.out.println("peterOne created");
        return new Person("personOne", 30);
    }
}

```
再次运行测试代码，我们得到如下输出：

```java
package cn.com.cakelite.tartcaker;

import cn.com.cakelite.tartcaker.config.AnnotationConfiguration;
import cn.com.cakelite.tartcaker.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationBeanTest {

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

```

```
peterOne created
peterNew created
peterNew created
false
true
```

我们先获取了`personNew`的Bean，但是`peterOne created`先输出，说明单例的Bean在Spring容器创建时就已经被实例化。

接下来我们在`personNew`添加`@Lazy`注解再次运行测试程序，我们得到了如下输出：

```
peterNew created
peterNew created
false
peterOne created
true
```

在被`@Lazy`标记的Bean只有在使用时才会真正的被实例化加载。



