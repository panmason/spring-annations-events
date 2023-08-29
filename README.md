# spring-annotations-events

## 1.Spring Bean的装配方式

### 1.1 基于配置文件与注解的装配方式

在Spring中有多种Bean的装配方式，作为一个初学者使用的方式有配置文件装配，需要在配置文件中书写多个`bean`标签，也有通过注解`@Bean`进行配置。

无论通过那种方式装配，最终目的是将配置的bean注册到Spring的容器中，下面介绍了两种装配方式与样例。

[两种装配方式介绍与样例](book/spring-bean-config.md)

### 1.2 使用`@ComponentScan`扫描包进行装配

在日常开发中，大多会使用`@Controller`,`@Service`,`@Repository`等注解来标记对应的业务代码中的Bean，此时使用Spring中的自动扫描功能，下面介绍对应扫描方式来自动装配Bean的配置

[自动包扫描简介与样例](book/spring-component-scan.md)


### 1.3 使用`@Import`导入Spring组件

在Spring容器中对于Bean的装配除了使用Bean注解，自动扫描等可以使用`@Import`直接将某个class添加到spring的容器中，对于使用`@Import`时可以配合`ImportSelector`使用，来达到类似过滤器的效果。

[Import使用介绍与样例](book/spring-imports.md)

### 1.4 使用`Conditional`进行条件加载



### 1.5 使用`FactoryBean`进行装配

## 2.Bean的生命周期

### 2.1 指定初始化与销毁方法

### 2.2 `PostConstruct`与`PreDestory`

### 2.3 `InitializingBean`与`DisposableBean`

### 2.4 后置处理器`BeanPostProcessor`原理与应用


## 3.自动装配

### 3.1 属性赋值与外部文件加载

### 3.2 依赖注入`@Autowired`与`@Resource`与`@Inject`

### 3.3 自动装配指定选择`@Qualifier`与`@Primary`

### 3.4 依据`Profile`环境区分装配


## 4.Spring的事件驱动