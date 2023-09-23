package cn.com.cakelite.tartcaker.colors;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class BeanInfo implements InitializingBean, DisposableBean {
    @Override
    public void destroy() throws Exception {
        System.out.println("BeanInfo destroy");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("BeanInfo afterPropertiesSet");
    }
}
