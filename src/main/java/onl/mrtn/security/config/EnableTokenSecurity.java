/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import onl.mrtn.security.service.TokenService;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({TokenSecurityConfiguration.class,
        TokenSecurityProperties.class,
        TokenService.class})
@Configuration
public @interface EnableTokenSecurity {
}
