# Spring中使用自动扫描机制来装配Bean

我们来模拟对应的业务，创建业务组件`UserController`,`UserService`,`UserDao`来模拟对应的一个用户业务中的增删改查。

## 1.基于配置文件的自动扫描配置

我们在Spring的配置文件中添加对应配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
       http://www.springframework.org/schema/beans/spring-beans.xsd 
       http://www.springframework.org/schema/context 
       https://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="cn.com.cakelite.tartcaker" />

</beans>

```

接下来我们来编写对应的测试程序[XmlComponentScanTest.java](../src/test/java/cn/com/cakelite/tartcaker/XmlComponentScanTest.java)

在测试程序中我们得到了如下输出
```
annotationConfiguration
userController
userDao
userService
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
person
personNew
personOne
```
在上面结果中我们发现对应的userController,userService,userDao都已经被正确装配，同时我们原来测试的使用的`AnnotationConfiguration`被`Configuration`标记的配置类也同样被加载。

## 2.基于注解配置的自动扫描配置

我们编写一个新的配置类[ComponentScanConfiguration.java](../src/main/java/cn/com/cakelite/tartcaker/config/ComponentScanConfiguration.java)

在该类中进行如下定义：

```java
package cn.com.cakelite.tartcaker.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "cn.com.cakelite.tartcaker")
public class ComponentScanConfiguration {
}

```
通过`@ComponentScan`注解进行扫描对应的报名，我们来编写一个全新的测试程序：

[AnnotationComponsentScanTest.java](../src/test/java/cn/com/cakelite/tartcaker/AnnotationComponsentScanTest.java)

测试程序如下：

```java
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

}
```

我们得到如下输出：
```
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
componentScanConfiguration
annotationConfiguration
userController
userDao
userService
person
personNew
personOne
```
说明Spring容器被正确初始化对应的Bean也被正确加载。

## 3.ComponentScan注解详细

在`@ComponentScan`有很多注解参数，我们下面简介几个常用的参数：

```

String[] basePackages() default {};  扫描的包路径

Filter[] includeFilters() default {}; 包含的过滤器

Filter[] excludeFilters() default {}; 排除的过滤器

boolean useDefaultFilters() default true; 是否使用默认的过滤器，默认配置是true

```

接下来我们通过配置排除我们一开始配置的`AnnotationConfiguration`，我们在`ComponentScanConfiguration`上添加一个excludeFilters。
```java
@Configuration
@ComponentScan(basePackages = "cn.com.cakelite.tartcaker", 
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AnnotationConfiguration.class)}
)
public class ComponentScanConfiguration {
}
```

在上面的配置我们排除了`AnnotationConfiguration`配置类的扫描，我们在此运行测试程序我们得到了如下结果
```
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
componentScanConfiguration
userController
userDao
userService
```
说明对应的配置已经被排除了，接下来我们来`@ComponentScan.Filter`中的类型：

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({})
@interface Filter {
		FilterType type() default FilterType.ANNOTATION;
        
		@AliasFor("classes")
		Class<?>[] value() default {};
        
		@AliasFor("value")
		Class<?>[] classes() default {};

		
		String[] pattern() default {};

}
```

我们主要观察在过滤器中的类型有：

```

ANNOTATION        对于注解的控制，可以控制对于注解的扫描

ASSIGNABLE_TYPE   Class类型的控制

ASPECTJ           Aspectj类型的控制

REGEX             通过正则表达式的控制

CUSTOM            自定义过滤类型

```

在上面的测试样例中我们直接过滤一个配置类，加下来我们主要看自定义类型的过滤器：

我们在实现自定义过滤器时需要实现`org.springframework.core.type.filter.TypeFilter`对应接口

```java
package cn.com.cakelite.tartcaker.filter;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class CustomFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        System.out.println("CustomFilter====》" + metadataReader.getClassMetadata().getClassName());
        return false;
    }
}

```

```java
@Configuration
@ComponentScan(basePackages = "cn.com.cakelite.tartcaker",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AnnotationConfiguration.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {CustomFilter.class})
        })
public class ComponentScanConfiguration {
}
```
我们将自定义过滤器配置到扫描器上，运行测试程序观察运行结果：
```
CustomFilter====》cn.com.cakelite.tartcaker.AnnotationBeanTest
CustomFilter====》cn.com.cakelite.tartcaker.AnnotationComponsentScanTest
CustomFilter====》cn.com.cakelite.tartcaker.XmlBeanTest
CustomFilter====》cn.com.cakelite.tartcaker.XmlComponentScanTest
CustomFilter====》cn.com.cakelite.tartcaker.config.AnnotationConfiguration
CustomFilter====》cn.com.cakelite.tartcaker.controller.UserController
CustomFilter====》cn.com.cakelite.tartcaker.dao.UserDao
CustomFilter====》cn.com.cakelite.tartcaker.filter.CustomFilter
CustomFilter====》cn.com.cakelite.tartcaker.model.Person
CustomFilter====》cn.com.cakelite.tartcaker.service.UserService
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
componentScanConfiguration
userController
userDao
userService

```
通过运行结果发现，配置的包扫描路径下所有类都会经过match方法，那么我接下来通过自定义过滤器过滤掉`AnnotationConfiguration`

Filter修改后结果如下：

```java
public class CustomFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        String className = metadataReader.getClassMetadata().getClassName();
        return className.contains("AnnotationConfiguration");
    }
}
```
运行结果如下：

```
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
componentScanConfiguration
userController
userDao
userService
```

说明我们自定义的过滤器已经生效，特别说明，需要主要使用的是includeFilters还是excludeFilters



