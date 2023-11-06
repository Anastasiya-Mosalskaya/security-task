package org.epam.securitytask.config;

import org.epam.securitytask.entity.Permissions;
import org.epam.securitytask.entity.UserEntity;
import org.epam.securitytask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index", "/about").permitAll()
                        .requestMatchers("/info").hasAuthority(Permissions.VIEW_INFO.name())
                        .requestMatchers("/admin").hasAuthority(Permissions.VIEW_ADMIN.name())
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper() {
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToUpperCase(true);
        return authorityMapper;
    }

    @Bean
    public CommandLineRunner initDatabase(UserService userService) {
        return args -> {
            userService.saveUser(new UserEntity("admin@gmail.com", passwordEncoder().encode( "admin"),
                    List.of(Permissions.VIEW_ADMIN, Permissions.VIEW_INFO)));
            userService.saveUser(new UserEntity("manager@gmail.com", passwordEncoder().encode( "manager"),
                    List.of(Permissions.VIEW_ADMIN)));
            userService.saveUser(new UserEntity("test@gmail.com", passwordEncoder().encode("test"),
                    List.of(Permissions.VIEW_INFO)));
        };
    }
}
