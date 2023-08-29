# Spring使用Import直接导入Bean

## 1.直接使用`@Import`导入Bean

我们创建一个颜色集合，在包`cn.com.cakelite.tartcaker.colors`下，下面有`Blue`,`Green`,`Red`三种颜色，接下来我们在配置类上添加引入标签

```java
package cn.com.cakelite.tartcaker.config;

import cn.com.cakelite.tartcaker.colors.Blue;
import cn.com.cakelite.tartcaker.filter.CustomFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "cn.com.cakelite.tartcaker",
        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AnnotationConfiguration.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {CustomFilter.class})
        })
@Import({Blue.class})
public class ComponentScanConfiguration {
}

```
运行测试程序，得到了如下输出：
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
```

对应测试结果，我们已经把`Blue`添加到了Spring容器中。

## 2.使用`ImportSelector`过滤批量引入Spring组件

我们自定义一个`ImportSelector`，代码定义如下：
```java
public class CustomSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{"cn.com.cakelite.tartcaker.colors.Green"};
    }
}
```

该实现中的`selectImports`方法是返回所有要加载的类信息的数组，入参为`AnnotationMetadata`是标记了`@Import`注解类的所有注解

我们再次运行测试程序，得到如下输出：

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
```
我们看到Green已经被添加到容器中