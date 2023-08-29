package cn.com.cakelite.tartcaker.config;

import cn.com.cakelite.tartcaker.colors.Blue;
import cn.com.cakelite.tartcaker.colors.Red;
import cn.com.cakelite.tartcaker.condition.CustomCondition;
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

}
