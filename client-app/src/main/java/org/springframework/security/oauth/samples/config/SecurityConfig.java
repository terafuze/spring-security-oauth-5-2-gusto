/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth.samples.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

/**
 * @author Joe Grandja
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	// @formatter:off
	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring()
				.antMatchers("/webjars/**");

	}
	// @formatter:on

	// @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  
      http
      .csrf()
      .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
  .and()
      .headers()
      .contentSecurityPolicy("default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")
  .and()
      .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
  .and()
      .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
  .and()
      .frameOptions()
      .deny()
  .and()
      .authorizeRequests()
      .antMatchers("/api/auth-info").permitAll()
      .antMatchers("/api/**").authenticated()
      .antMatchers("/management/health").permitAll()
      .antMatchers("/management/info").permitAll()
      .antMatchers("/management/prometheus").permitAll()
  .and()
      // Configures authentication support using an OAuth 2.0 and/or OpenID Connect 1.0 Provider. 
      // The "authentication flow" is implemented using the Authorization Code Grant, as specified in the OAuth 2.0 Authorization Framework and OpenID Connect Core 1.0 specification. 
      .oauth2Login()
  .and()
          .oauth2Client();
	  
//		http
//			.authorizeRequests()
//				.anyRequest().permitAll()
//				.and()
//			.formLogin()
//				.loginPage("/login")
//				.failureUrl("/login-error")
//				.permitAll()
//				.and()
//			.oauth2Client();
	}
	// @formatter:on

	// @formatter:off
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user1")
            .password("password")
            .roles("USER")
            .build();
        return  new InMemoryUserDetailsManager(user);
    }
    // @formatter:on
}
