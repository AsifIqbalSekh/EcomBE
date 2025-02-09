package com.asifiqbalsekh.EcomBE.controller;

import com.asifiqbalsekh.EcomBE.dto.*;
import com.asifiqbalsekh.EcomBE.exception.APIException;
import com.asifiqbalsekh.EcomBE.exception.ResourceNotFoundException;
import com.asifiqbalsekh.EcomBE.model.Role;
import com.asifiqbalsekh.EcomBE.model.User;
import com.asifiqbalsekh.EcomBE.repository.RoleRepository;
import com.asifiqbalsekh.EcomBE.repository.UserRepository;
import com.asifiqbalsekh.EcomBE.security.jwt.JwtUtils;
import com.asifiqbalsekh.EcomBE.security.securityservice.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDTO>signin(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication;

        try {
            authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDTO.getUsername(), loginRequestDTO.getPassword()
            ));
        } catch (AuthenticationException e) {
            log.error("Authentication failed! Username or password is incorrect");
            throw new ResourceNotFoundException("USER", "username", loginRequestDTO.getUsername());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken=jwtUtils.generateTokenFromUsername(userDetails);
        //For Cookie based authentication
//        ResponseCookie jwtCookie=jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return ResponseEntity.ok(new LoginResponseDTO(userDetails.getId(), jwtToken,userDetails.getUsername(), roles));
        //For Cookie based authentication
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(new LoginResponseDTO(
//                userDetails.getId(), jwtCookie.toString(),userDetails.getUsername(), roles
//        ));
    }

    @GetMapping("admin/promote-admin/{username}")
    ResponseEntity<SignUpResponseDTO>signup(@PathVariable String username) {

        User user= userRepository.findByUserName(username.toLowerCase()).orElseThrow(()->

                new ResourceNotFoundException("USER", "username", username)
        );
        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                .orElseGet(() -> {
                    Role newUserRole = new Role(AppRole.ROLE_ADMIN);
                    return roleRepository.save(newUserRole);
                });
        if(user.getRoles().contains(adminRole)){
            throw new APIException("ADMIN ROLE ALREADY ADDED FOR USER: " + username);
        }

        user.getRoles().add(adminRole);
        userRepository.save(user);

        return ResponseEntity.ok(new SignUpResponseDTO(username,user.getEmail(),"Promoted To Admin Success!"));
    }


    @PostMapping("/signup")
    ResponseEntity<SignUpResponseDTO>signup(@Valid @RequestBody SignUpRequestDTO signUpDTO) {
        userRepository.findByUserName(signUpDTO.getUsername().toLowerCase()).ifPresent(user -> {
            throw new APIException("UserName:" +user.getUserName()+ " already in use");
        });

        userRepository.findByEmail(signUpDTO.getEmail().toLowerCase()).ifPresent(user -> {
            throw new APIException("Email:" +user.getEmail()+ " already in use");
        });

        Set<String> givenRole=signUpDTO.getRole();
        Set<Role>roles=new HashSet<>();

        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseGet(() -> {
                    Role newUserRole = new Role(AppRole.ROLE_USER);
                    return roleRepository.save(newUserRole);
                });

        roles.add(userRole);

        if(givenRole!=null && !givenRole.isEmpty()){
            givenRole.forEach(role -> {
                switch (role.toLowerCase()){
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseGet(() -> {
                                    Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                                    return roleRepository.save(newSellerRole);
                                });
                        roles.add(sellerRole);
                        break;
                    case "user":
                        break;
                    default:
                        throw new APIException("Invalid role: " + role);

                }
            });
        }
        User user=new User(
                signUpDTO.getUsername().toLowerCase(),
                passwordEncoder.encode(signUpDTO.getPassword()),
                signUpDTO.getEmail().toLowerCase()
        );

        user.setRoles(roles);
        User savedUser=userRepository.save(user);
        return ResponseEntity.ok(new SignUpResponseDTO(
                savedUser.getUserName(),
                savedUser.getEmail(),
                "User registered successfully!"
        ));
    }


//For cookie based authentication
//    @PostMapping("/signout")
//    public ResponseEntity<SignUpResponseDTO>signOut(Authentication authentication) {
//
//        if(authentication==null){
//            throw new APIException("User Not Signed In");
//        }
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        ResponseCookie cookie= jwtUtils.generateJwtCleanCookie();
//
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(
//                new SignUpResponseDTO(
//                        userDetails.getUsername(),userDetails.getEmail(),"Signed Out Success!"
//                )
//        );
//    }

    @GetMapping("/test")
    public ResponseEntity<SignUpResponseDTO>healthEndpoint() {

        return ResponseEntity.ok().body(
                new SignUpResponseDTO(
                        "test","test@email.com","ok"
                )
        );
    }

    @GetMapping("/userinfo")
    public ResponseEntity<UserInfoResponseDTO>userInfoDetails(Authentication authentication) {
        if(authentication==null){
            throw new APIException("User Not Signed In");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return ResponseEntity.ok().body(
                new UserInfoResponseDTO(
                        userDetails.getId(),userDetails.getEmail(),userDetails.getUsername(), roles
                )
        );
    }


}
