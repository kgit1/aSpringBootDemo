package com.kong.sboot.aSpringBootDemo;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		return new InMemoryUserDetailsManager(
				User.withDefaultPasswordEncoder().username("user1").password("password1").authorities("ROLE_USER")
						.build(),
				User.withDefaultPasswordEncoder().username("admin1").password("admin111").authorities("ROLE_ADMIN")
						.build(),

				User.withDefaultPasswordEncoder().username("act1").password("act111").authorities("ROLE_ACTUATOR")
						.build());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests()
				// any rules that come after the rule that matches are never reached. Generally
				// when writing the rules
				// for authenticated requests, the more specific rule will come first.
				.requestMatchers(EndpointRequest.to("info", "health")).anonymous()
				.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				// limits admin role only for /admin resources
				.antMatchers("/user*").permitAll().antMatchers("/admin").hasRole("ADMIN").antMatchers("/actuator")
				.hasRole("ACTUATOR").antMatchers("/events/**").hasRole("USER")
				
				// must be last in auth chain because if earlier - app checking rules and once
				// spotted this - applies
				// to any request if any role authorised

				.anyRequest().authenticated()
				// gives all resources permission for all
				.antMatchers("/**").permitAll()
				
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).deleteCookies("JSESSIONID")
				.invalidateHttpSession(true).clearAuthentication(true).logoutSuccessUrl("/login").and().formLogin()
				.loginProcessingUrl("/login").defaultSuccessUrl("/")
				
				// basic http auth popup
				.and().httpBasic();
				

				// .antMatchers("/login").permitAll()
				// .anyRequest().authenticated()

				// .and() //HTTP basic Authentication only for API
				// .antMatcher("/api/**").httpBasic()
				// Login Form configuration for all others
				// .and().formLogin().loginPage("/login").permitAll()

				// all logout tries not logging our, redirecting to success but still logged in
				// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
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
				
				// .and().logout().logoutUrl("/logout").logoutSuccessUrl("/actuator").permitAll()

				// .and().logout().logoutRequestMatcher(new
				// AntPathRequestMatcher("/logout")).logoutSuccessUrl("/actuator")
				// .deleteCookies("JSESSIONID").invalidateHttpSession(true).clearAuthentication(true)
				
	}

}
