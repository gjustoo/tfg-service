// package com.gabriel.tfg.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// @Configuration
// @EnableWebSecurity
// public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         http.cors().and().csrf().disable()
//                 .authorizeRequests()
//                 .antMatchers("/api/auth/**").permitAll()
//                 .antMatchers("/v2/api-docs",
//                         "/configuration/ui",
//                         "/swagger-resources/**",
//                         "/configuration/security",
//                         "/swagger-ui.html",
//                         "/webjars/**")
//                 .permitAll()
//                 .anyRequest().authenticated().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                 .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
//                 .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//         http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//     }
// }