package org.ihiw.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Application constants as configured in the properties.
 */
@Configuration
public class ConstantsConfiguration {
    private final Environment env;

    public ConstantsConfiguration(Environment env) {
        this.env = env;
    }

    @Bean(name="activationEmail")
    public String activationEmail(){
        return env.getProperty("mail.activation-email");
    }

    @Bean(name="activation2Email")
    public String activation2Email(){
        return env.getProperty("mail.activation2-email");
    }
}
