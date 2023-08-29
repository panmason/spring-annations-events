package cn.com.cakelite.tartcaker.config;

import cn.com.cakelite.tartcaker.filter.CustomFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "cn.com.cakelite.tartcaker",
        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AnnotationConfiguration.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {CustomFilter.class})
        })
public class ComponentScanConfiguration {
}
