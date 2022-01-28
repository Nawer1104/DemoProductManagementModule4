package com.example.productmanagement.config;

import com.example.productmanagement.service.impl.CustomEployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomEployeeDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/employees").hasAnyAuthority("Manager")
                .antMatchers("/employee/**").hasAnyAuthority("Manager")
                .antMatchers("/add").hasAnyAuthority("Manager", "Incharge")
                .antMatchers("/addCate").hasAnyAuthority("Manager", "Incharge")
                .antMatchers("/edit/**").hasAnyAuthority("Manager", "Incharge")
                .antMatchers("/delete/**").hasAnyAuthority("Manager", "Incharge")
                .antMatchers("/products/**", "/cart/**").hasAnyAuthority("Manager", "Incharge","Partner")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .usernameParameter("number")
                .defaultSuccessUrl("/products")
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");
        http.csrf().disable();
    }
}
