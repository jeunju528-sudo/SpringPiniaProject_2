package com.sist.web.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.sist.web.security.LoginFailHandler;
import com.sist.web.security.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // SpringSecurity를 활성화 시켜줌
@RequiredArgsConstructor
public class SecurityConfig {
	private final LoginSuccessHandler loginSuccessHandler;
	private final LoginFailHandler loginFailHandler;
	private final DataSource dataSource;

	/*
	 * CSRF : 공격자가 인증된 브라우저에서 저장된 쿠키나 세션정보를 탈취해서 공격하는 것
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf->csrf.disable())
			.authorizeHttpRequests(auth->auth.requestMatchers("/","/member/**").permitAll()
											.requestMatchers("/admin/**").hasRole("ADMIN")
											.anyRequest().authenticated())
			.formLogin(form->form.loginPage("/member/login")
								.loginProcessingUrl("/member/login_process")
								.usernameParameter("userid")
								.passwordParameter("userpwd")
								.defaultSuccessUrl("/main",false)
								.successHandler(loginSuccessHandler)
								.failureHandler(loginFailHandler)
								.permitAll())
			.rememberMe(remember->remember.key("my-secret-key")
										.rememberMeParameter("remember-me")
										.tokenValiditySeconds(60*60*24))
			.logout(logout->logout.logoutUrl("/member/logout")
								.logoutSuccessUrl("/")
								.invalidateHttpSession(true)
								.deleteCookies("remember-me","JSESSIONID"));
		
		return http.build();
											
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder) throws Exception{
		return null;
	}
	
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() {
		return null;
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		return null;
	}

}
