package com.asifiqbalsekh.EcomBE.security.securityconfig;

import com.asifiqbalsekh.EcomBE.config.AppConstant;
import com.asifiqbalsekh.EcomBE.dto.AppRole;
import com.asifiqbalsekh.EcomBE.model.Role;
import com.asifiqbalsekh.EcomBE.model.User;
import com.asifiqbalsekh.EcomBE.repository.RoleRepository;
import com.asifiqbalsekh.EcomBE.repository.UserRepository;
import com.asifiqbalsekh.EcomBE.security.jwt.JWTAuthEntrypointException;
import com.asifiqbalsekh.EcomBE.security.jwt.JWTAuthTokenFilter;
import com.asifiqbalsekh.EcomBE.security.securityservice.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JWTAuthEntrypointException jwtAuthEntrypointException;
    private final JWTAuthTokenFilter jwtAuthTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception->
                        exception.authenticationEntryPoint(jwtAuthEntrypointException)
                )
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth-> {
                    auth.requestMatchers("/api/auth/admin/promote-admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/auth/**","/h2-console/**","/swagger-ui/**","/v3/api-docs/**","/api/public/**").permitAll();
                    auth.anyRequest().authenticated();
                });
        http.authenticationProvider(daoAuthenticationProvider());
        http.headers(headers -> headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        );
        http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //Allowing static resources without intercept
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v3/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    //Adding master admin

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            log.info("Creating master User ....");


            // Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });



            log.info("Master Role Created ....");

            Set<Role> masterRoles = Set.of(userRole, sellerRole, adminRole);

            User savedUser = userRepository.findByUserName(AppConstant.MASTER_USERNAME)
                    .orElseGet(() -> {

                        User createdUser = new User(
                                AppConstant.MASTER_USERNAME,
                                passwordEncoder.encode(AppConstant.MASTER_PASSWORD),
                                AppConstant.MASTER_EMAIL
                        );

                        return userRepository.save(createdUser);
                    });

            log.info("Master Admin Created ....");
            log.info("Master Admin Saved ....");

            userRepository.findByUserName(savedUser.getUserName()).ifPresent(user -> {
                user.setRoles(masterRoles);
                userRepository.save(user);
            });
            log.info("Master Admin Role Updated....");

        };
    }

}
