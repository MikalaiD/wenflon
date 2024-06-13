package com.yosik.basic;

import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.WenflonBeanPostprocessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    WenflonBeanPostprocessor wenflonBeanPostprocessor() {
        return new WenflonBeanPostprocessor();
    }

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()->"John";
    }
}

