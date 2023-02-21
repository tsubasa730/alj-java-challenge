package jp.co.axa.apidemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*
        Create 2 temp user for testing security of endpoints
        admin:nimda can access all the endpoints
        AXA:AXA123 can only access get-mapping endpoints
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("nimda")).roles("ADMIN").authorities("READ", "CREATE", "UPDATE", "DELETE")
                .and()
                .withUser("AXA").password(passwordEncoder().encode("AXA123")).roles("USER").authorities("READ");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // all endpoints are required to authenticate
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        // disable csrf to test API by using Postman
        http.cors().and().csrf().disable();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
