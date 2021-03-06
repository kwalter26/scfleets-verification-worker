package com.fusionkoding.scfleetsverificationworker;

import com.fusionkoding.scfleetsverificationworker.config.PropertyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@EnableConfigurationProperties(PropertyConfig.class)
public class ScfleetsVerificationWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScfleetsVerificationWorkerApplication.class, args);
    }

}
