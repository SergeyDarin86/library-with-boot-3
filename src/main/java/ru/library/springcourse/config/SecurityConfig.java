package ru.library.springcourse.config;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.library.springcourse.securuty.PersonDetails;
import ru.library.springcourse.services.CustomSuccessAuthenticationHandler;
import ru.library.springcourse.services.PersonDetailsService;

//в этом классе настраивается авторизация и аутентификация для Spring Security
// это главный класс для настроек

// аннотация дает понять Spring, что это конфигурационный класс для Spring Security
@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    //TODO: нужно посмотреть почему программа может работать даже если я не внедряю
    // в этом классе PersonDetailsService???
    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

//        private final AuthProviderImpl authProvider;
//
//    public SecurityConfig(AuthProviderImpl authProvider) {
//        this.authProvider = authProvider;
//    }

    //настраивает аутентификацию
    //даем понять Spring Security, что мы используем провайдер для аутентификации пользователя
    //сделал из метода бин, который возвращает AuthenticationManagerBuilder
    // до этого он возвращал void и класс наследовался от  WebSecurityConfigurerAdapter
    // в более ранней версии Spring 2 (смотри проект LibraryAppWithSecurity)
//    @Bean
//    @Primary
//    protected AuthenticationManagerBuilder configure(AuthenticationManagerBuilder auth){
//        return auth.authenticationProvider(authProvider);
//    }

    // без этого метода все работает, нужно разобраться почему

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService);
    }

    // без этого бина выбрасывается исключение
    // нужно обязательно указать что мы используем/не используем кодировку
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // предоставление/запрет доступа к определенным ендпоинам приложения
    // людям с определенными правами (пока у нас этих прав нет) с помощью данного бина
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth->auth.requestMatchers("/library/books/**").authenticated())
//                .authorizeHttpRequests(auth->auth.requestMatchers("/library/people/**").authenticated())
//                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll).build();
//    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSuccessAuthenticationHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/library/admin").hasRole("ADMIN")
                        .requestMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                        .anyRequest().hasAnyRole("USER","ADMIN"))
                .formLogin(auth -> auth.loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/library/people", true)
//                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/auth/login?error"))
                .logout(auth -> auth.logoutUrl("/logout").logoutSuccessUrl("/auth/login"))
                .build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//
//        return httpSecurity
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login", "/auth/registration", "/error")
//                        .permitAll()
//                        .anyRequest().authenticated())
//                .formLogin(auth -> auth.loginPage("/auth/login")
//
//                        .loginProcessingUrl("/process_login")
//                        .defaultSuccessUrl("/library/people", true)
////                        .successHandler(authenticationSuccessHandler())
//                        .failureUrl("/auth/login?error"))
//                .logout(auth -> auth.logoutUrl("/logout").logoutSuccessUrl("/auth/login"))
//                .build();
//    }

}
