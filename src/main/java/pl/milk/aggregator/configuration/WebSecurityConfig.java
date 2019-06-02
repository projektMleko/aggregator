package pl.milk.aggregator.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**/*").permitAll()
                .antMatchers("/h2/**").authenticated()
                .antMatchers("/actuator/**").authenticated()
                .antMatchers("/file_processing/**").authenticated()
                .antMatchers("/persistance/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/login");

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }


}
