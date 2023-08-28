# spring-annotations-events

## 1.Spring Bean的装配方式

### 1.1 基于配置文件与注解的装配方式

在Spring中有多种Bean的装配方式，作为一个初学者使用的方式有配置文件装配，需要在配置文件中书写多个`bean`标签，也有通过注解`@Bean`进行配置。

无论通过那种方式装配，最终目的是将配置的bean注册到Spring的容器中，下面介绍了两种装配方式与样例。

[两种装配方式介绍与样例](book/spring-bean-config.md)

### 1.2 使用`@ComponentScan`扫描包进行装配

### 1.3 使用`@import`导入Spring组件

### 1.4 使用`FactoryBean`进行装配