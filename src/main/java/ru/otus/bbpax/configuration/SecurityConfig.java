package ru.otus.bbpax.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.otus.bbpax.service.security.CustomUserDetailsService;

import static ru.otus.bbpax.entity.security.Roles.ADMIN;
import static ru.otus.bbpax.entity.security.Roles.USER;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService service;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin().failureUrl("/login.html?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and()
                .authorizeRequests()
                    .antMatchers("/", "/book/*")
                    .permitAll()
                .and()
                .authorizeRequests()
                    .antMatchers("/author/**", "/genre/**", "/book/**")
                    .hasAnyAuthority(USER, ADMIN)
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.DELETE)
                    .hasAuthority(ADMIN)
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/author/**", "/genre/**")
                    .hasAuthority(ADMIN)
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.PUT)
                    .hasAuthority(ADMIN)
                .and()
                .authorizeRequests()
                    .antMatchers("/api/**")
                    .hasAuthority(ADMIN)
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service);

//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN");
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }
}
