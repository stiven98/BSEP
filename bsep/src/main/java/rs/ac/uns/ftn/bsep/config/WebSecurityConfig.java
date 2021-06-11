package rs.ac.uns.ftn.bsep.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import rs.ac.uns.ftn.bsep.security.TokenUtils;
import rs.ac.uns.ftn.bsep.security.auth.CustomAccessDeniedHandler;
import rs.ac.uns.ftn.bsep.security.auth.RestAuthenticationEntryPoint;
import rs.ac.uns.ftn.bsep.security.auth.TokenAuthenticationFilter;
import rs.ac.uns.ftn.bsep.service.UserService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Autowired
    private UserService userService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }



    @Autowired
    private TokenUtils tokenUtils;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // komunikacija izmedju klijenta i servera je stateless posto je u pitanju REST aplikacija
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()
                // svim korisnicima dopusti da pristupe putanjama /auth/**, (/h2-console/** ako se koristi H2 baza) i /api/foo
                .authorizeRequests().antMatchers("/auth/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/users/login").permitAll()
                .antMatchers("/api/users/forgotPassword").permitAll()
                .antMatchers("/api/users/resetPassword").permitAll()
                .antMatchers("/api/users/checkRequest/**").permitAll()
                .antMatchers("/api/users/activate/**").permitAll()
                .antMatchers("/api/users/register").permitAll()
                .antMatchers("/api/certificate/**").permitAll()

                // za svaki drugi zahtev korisnik mora biti autentifikovan
                .anyRequest().authenticated().and()

                // za development svrhe ukljuci konfiguraciju za CORS iz WebConfig klase
                .cors().and()
                .addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userService),
                        BasicAuthenticationFilter.class);

        // zbog jednostavnosti primera
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // TokenAuthenticationFilter ce ignorisati sve ispod navedene putanje
        web.ignoring().antMatchers(HttpMethod.POST, "/api/users/login");
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.js","/api/city/all");
    }
}
