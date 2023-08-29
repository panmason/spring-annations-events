# Conditional条件加载

## 1.条件加载

在做条件加载时我们先看对应的注解

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {
    
	Class<? extends Condition>[] value();

}
```

在注解中我们需要自定义一个加载条件`Condition`，接下来我们先自定义一个加载条件

```java
public class CustomCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return Objects.requireNonNull(context.getBeanFactory()).containsBean("cn.com.cakelite.tartcaker.colors.Blue");
    }
}
```

在配置中添加`red`Bean的配置，配置如下：

```java
@Configuration
@ComponentScan(basePackages = "cn.com.cakelite.tartcaker",
        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AnnotationConfiguration.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {CustomFilter.class})
        })
@Import({Blue.class, CustomSelector.class})
public class ComponentScanConfiguration {

    @Conditional(CustomCondition.class)
    @Bean
    public Red red() {
        return new Red();
    }

}
```

如果spring的容器中有`cn.com.cakelite.tartcaker.colors.Blue`这个Bean，那就加载red，我们运行测试程序，得到如下输出：

```
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
componentScanConfiguration
userController
userDao
userService
cn.com.cakelite.tartcaker.colors.Blue
cn.com.cakelite.tartcaker.colors.Green
red
```
说明条件判断成功

