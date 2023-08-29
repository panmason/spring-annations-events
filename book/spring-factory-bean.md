# Spring FactoryBean使用

## 1.FactoryBean的装配方式

我们定义一个`Color`，然后使用`FactoryBean`来进行Bean的装配，具体代码如下：

```java
import cn.com.cakelite.tartcaker.colors.Color;
import org.springframework.beans.factory.FactoryBean;

public class ColorFactoryBean implements FactoryBean<Color> {
    @Override
    public Color getObject() throws Exception {
        return new Color();
    }

    @Override
    public Class<?> getObjectType() {
        return Color.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
```
getObject: 获取对象实例的方法

getObjectType:获取Bean的类型

isSingleton:生成的Bean是单例还是多例的


我们在配置类中添加`ColorFactoryBean`的配置

```java
package cn.com.cakelite.tartcaker.config;

import cn.com.cakelite.tartcaker.colors.Blue;
import cn.com.cakelite.tartcaker.colors.Red;
import cn.com.cakelite.tartcaker.condition.CustomCondition;
import cn.com.cakelite.tartcaker.factory.ColorFactoryBean;
import cn.com.cakelite.tartcaker.filter.CustomFilter;
import cn.com.cakelite.tartcaker.selector.CustomSelector;
import org.springframework.context.annotation.*;

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

    @Bean
    public ColorFactoryBean colorFactoryBean() {
        return new ColorFactoryBean();
    }

}

```

我们运行测试程序得到如下输出：

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
colorFactoryBean
```

我们可以发现colorFactoryBean已经被添加到了容器中，这时可能会有疑问，我们不是在配置`Color`的bean吗，为什么在Spring的容器中是`colorFactoryBean`

接下来我们从容器中获取该bean，看下具体的类型是什么，修改测试程序：

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


    @Test
    void factoryBeanTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ComponentScanConfiguration.class);
        Object colorFactoryBean = applicationContext.getBean("colorFactoryBean");
        System.out.println(colorFactoryBean.getClass());
    }
}

```

得到了如下输出

```
class cn.com.cakelite.tartcaker.colors.Color
```
我们获取到依旧是Color类型的Bean

那我们如何获取到colorFactoryBean的实例呢，我们修改测试代码

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


    @Test
    void factoryBeanTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ComponentScanConfiguration.class);
        Object colorFactoryBean = applicationContext.getBean("colorFactoryBean");
        System.out.println(colorFactoryBean.getClass());

        Object colorFactoryBean1 = applicationContext.getBean("&colorFactoryBean");
        System.out.println(colorFactoryBean1.getClass());
    }
}

```
运行测试程序，得到如下结果：

```
class cn.com.cakelite.tartcaker.colors.Color
class cn.com.cakelite.tartcaker.factory.ColorFactoryBean
```

为什么添加了`&`后就可以获取原始Bean的类型，原因是因为在`BeanFactory`中对于Factory定义如下

```java
String FACTORY_BEAN_PREFIX = "&";
```
所以在使用FactoryBean时Spring会默认返回工厂配置的类型，需要获取到实际的工厂需要在Bean的ID前添加`&`