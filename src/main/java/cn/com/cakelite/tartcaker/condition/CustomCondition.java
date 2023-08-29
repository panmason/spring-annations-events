package cn.com.cakelite.tartcaker.condition;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

public class CustomCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return Objects.requireNonNull(context.getBeanFactory()).containsBean("cn.com.cakelite.tartcaker.colors.Blue");
    }
}
