package cn.com.cakelite.tartcaker.config;

import cn.com.cakelite.tartcaker.colors.Blue;
import cn.com.cakelite.tartcaker.filter.CustomFilter;
import cn.com.cakelite.tartcaker.selector.CustomSelector;
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
@Import({Blue.class, CustomSelector.class})
public class ComponentScanConfiguration {
}
