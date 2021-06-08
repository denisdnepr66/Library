package com.denys.library.some;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.denys.library.some.security.Permission.*;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //TODO add ant matcher for all endpoints
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()

                .antMatchers(HttpMethod.PUT,"/request/**").hasAuthority(BOOK_READ.getPermission())
                .antMatchers(HttpMethod.PUT,"/return/**").hasAuthority(BOOK_READ.getPermission())

                .antMatchers(HttpMethod.GET,"/books/**").hasAuthority(BOOK_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/books/**").hasAuthority(BOOK_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE,"/books/**").hasAuthority(BOOK_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT,"/books/**").hasAuthority(BOOK_WRITE.getPermission())

                .antMatchers(HttpMethod.GET,"/users/**").hasAuthority(USER_READ.getPermission())
                .antMatchers(HttpMethod.POST,"/users/**").hasAuthority(USER_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE,"/users/**").hasAuthority(USER_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT,"/users/**").hasAuthority(USER_WRITE.getPermission())

                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(daoAuthenticationProvider());
    }


    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
