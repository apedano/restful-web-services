/*
 * COPYRIGHT NOTICE
 * © 2020  Transsmart Holding B.V.
 * All Rights Reserved.
 * All information contained herein is, and remains the
 * property of Transsmart Holding B.V. and its suppliers,
 * if any.
 * The intellectual and technical concepts contained herein
 * are proprietary to Transsmart Holding B.V. and its
 * suppliers and may be covered by European and Foreign
 * Patents, patents in process, and are protected by trade
 * secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from Transsmart Holding B.V.
 */
package it.apedano.webservices.restfulwebservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    
    /*
    curl --location --request GET 'http://localhost:8080/users/1' \

--header 'Content-Type: application/json' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
     */

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("adminPass")).roles("USER", "ADMIN")
                .and()
                .withUser("user").password(passwordEncoder().encode("userPass")).roles("USER");
    }
    
    // Secure the endpoins with HTTP Basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable() //anables post requests https://spring.io/blog/2013/08/21/spring-security-3-2-0-rc1-highlights-csrf-protection/
                //HTTP Basic authentication
                .httpBasic()
                /*
                This class will be responsible to send response when the credentials are no longer authorized. 
                If the authentication event was successful, or authentication was not attempted because 
                the HTTP header did not contain a supported authentication request, 
                the filter chain will continue as normal. 
                The only time the filter chain will be interrupted is if authentication fails and the AuthenticationEntryPoint 
                is called.
                */
                .authenticationEntryPoint(authenticationEntryPoint) //handles 
                .and()
                .authorizeRequests() //Allows restricting access based upon the {@link HttpServletRequest} using
                //.anyRequest().permitAll() //disable security for requests
                .antMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/users/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                .antMatchers("/jpa/**").hasRole("USER")
                .and()
                .headers().frameOptions().disable() //enables fram requests for h2 console
                .and()
                .formLogin().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
