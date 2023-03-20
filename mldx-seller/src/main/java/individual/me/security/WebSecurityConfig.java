package individual.me.security;

import individual.me.security.handler.MldxAuthenticationFailureHandler;
import individual.me.security.handler.MldxAuthenticationSuccessHandler;
import individual.me.security.handler.MldxLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private MldxAuthenticationFailureHandler mldxAuthenticationFailureHandler;

    @Autowired
    private MldxLogoutHandler mldxLogoutHandler;

    @Autowired
    private MldxAuthenticationSuccessHandler mldxAuthenticationSuccessHandler;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeRequests().mvcMatchers("/admin/login", "/admin/captcha").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/admin/login").usernameParameter("username").passwordParameter("password")
                .successHandler(mldxAuthenticationSuccessHandler)
                .failureHandler(mldxAuthenticationFailureHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .logout().logoutSuccessHandler(mldxLogoutHandler).and()
                .csrf().disable().build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
