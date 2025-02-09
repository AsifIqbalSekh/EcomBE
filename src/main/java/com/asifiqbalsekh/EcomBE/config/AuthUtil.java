package com.asifiqbalsekh.EcomBE.config;

import com.asifiqbalsekh.EcomBE.dto.UserInfoResponseDTO;
import com.asifiqbalsekh.EcomBE.exception.ResourceNotFoundException;
import com.asifiqbalsekh.EcomBE.model.User;
import com.asifiqbalsekh.EcomBE.repository.UserRepository;
import com.asifiqbalsekh.EcomBE.security.securityservice.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UserRepository userRepository;



    public UserInfoResponseDTO userInfoDetails() {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return
                new UserInfoResponseDTO(
                        userDetails.getId(),userDetails.getEmail(),userDetails.getUsername(), roles

        );
    }

    public User userDetails() {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User","userName",userDetails.getUsername())
                );
    }
}

