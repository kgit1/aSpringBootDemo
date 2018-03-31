package com.kong.sboot.aSpringBootDemo;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		return new InMemoryUserDetailsManager(
				User.withDefaultPasswordEncoder().username("user1").password("password1").authorities("ROLE_USER")
						.build(),
				User.withDefaultPasswordEncoder().username("admin11").password("admin111")
						.authorities("ROLE_ACTUATOR", "ROLE_USER", "ROLE_ADMIN").build()
						);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests()
		.anyRequest().authenticated()
				.requestMatchers(EndpointRequest.to("info")).permitAll()
				.requestMatchers(EndpointRequest.to("info", "health")).hasRole("ADMIN")
				.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				// limits admin role only for /admin resources
				.antMatchers("/admin").hasRole("ADMIN")
				.antMatchers("/actuator")
				.hasRole("ACTUATOR")
				
				// gives all resources permission for all
				.antMatchers("/**").permitAll()
				.antMatchers("/login").permitAll()
				.anyRequest().authenticated()
				
				// .and() //HTTP basic Authentication only for API
				// .antMatcher("/api/**").httpBasic()
				// Login Form configuration for all others
				// .and().formLogin().loginPage("/login").permitAll()

				// all logout tries not logging our, redirecting to success but still logged in
				//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
				// Logout Form configuration
				// The default is that accessing the URL
				// * "/logout" will log the user out by invalidating the HTTP Session, cleaning
				// up any
				// * {@link #rememberMe()} authentication that was configured, clearing the
				// * {@link SecurityContextHolder}, and then redirect to "/login?success".
				// .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				// .logoutSuccessUrl("/").deleteCookies("JSESSIONID")
				// .invalidateHttpSession(true);

				// .and().logout().logoutRequestMatcher(new
				// AntPathRequestMatcher("/logout")).deleteCookies("JSESSIONID")
				// .invalidateHttpSession(true).clearAuthentication(true).logoutSuccessUrl("/loged.out")
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
				.and().formLogin().loginProcessingUrl("/login").defaultSuccessUrl("/")

				// .and().logout().logoutUrl("/logout").logoutSuccessUrl("/actuator").permitAll()

				// .and().logout().logoutRequestMatcher(new
				// AntPathRequestMatcher("/logout")).logoutSuccessUrl("/actuator")
				// .deleteCookies("JSESSIONID").invalidateHttpSession(true).clearAuthentication(true)

				// basic http auth popup
				.and().httpBasic();
	}

}
