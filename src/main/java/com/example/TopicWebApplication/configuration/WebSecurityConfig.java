package com.example.TopicWebApplication.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.TopicWebApplication.jwt.JwtAuthEntryPoint;
import com.example.TopicWebApplication.jwt.JwtAuthTokenFilter;
import com.example.TopicWebApplication.services.UserServices;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserServices  userServices;
	
//	@Autowired
//    private CustomUserTypesOAuth2UserService customOAuth2UserService;
//
//    @Autowired
//    private AuthenticationFailureHandler oAuth2AuthenticationException;
//
//    @Autowired
//    private AuthenticationSuccessHandler oAuth2AuthenticationToken;
    
    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public HttpSessionOAuth2AuthorizationRequestRepository httpSessionOAuth2AuthorizationRequestRepository() {
		return new HttpSessionOAuth2AuthorizationRequestRepository();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userServices)
			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.cors().and().csrf().disable()
//			.authorizeRequests()
//			.antMatchers("/api/user").permitAll()
//			.antMatchers("/api/auth/**").permitAll()
//			.anyRequest().authenticated()
//			.and()
//			.formLogin()
//			.defaultSuccessUrl("/").permitAll()
//			.and()
//			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
//			.and()
//			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
//			.logout().permitAll();
		
		http.cors().and().csrf().disable()
			.authorizeRequests()
	        .antMatchers("/api/auth/**", "/oauth2/**").permitAll()
	        .antMatchers("/api/get-topic").permitAll()
	        .anyRequest().authenticated()
//	        .and()
//	        .oauth2Login()
//	        .authorizationEndpoint()
//            .baseUri("/oauth2/authorize")
//            .authorizationRequestRepository(httpSessionOAuth2AuthorizationRequestRepository())
//            .and()
//	        .redirectionEndpoint()
//	            .baseUri("/oauth2/callback/*")
//	            .and()
//	        .userInfoEndpoint()
//	            .userService(customOAuth2UserService)
//	            .and()
//	        .successHandler(oAuth2AuthenticationToken)
//	        .failureHandler(oAuth2AuthenticationException)
//	        .and()
//	        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
	        .and()
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
