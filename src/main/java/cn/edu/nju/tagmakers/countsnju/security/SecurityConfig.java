package cn.edu.nju.tagmakers.countsnju.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Description:
 *
 * @author xxz
 * Created on 04/06/2018
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final SecurityUserController securityUserController;

    @Autowired
    public SecurityConfig(SecurityUserController securityUserController) {
        this.securityUserController = securityUserController;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .httpBasic().and()
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/new_user").permitAll()
                .antMatchers("/initiator/**").hasRole("INITIATOR")
                .antMatchers("/worker/**").hasRole("WORKER")
                .anyRequest().fullyAuthenticated()
                .and()
                .addFilter(new JWTLoginFilter(authenticationManager()))
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), securityUserController));
//                .anyRequest().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("ym").password("{noop}123").roles("MAN");
        auth.userDetailsService(securityUserController);
    }
}
