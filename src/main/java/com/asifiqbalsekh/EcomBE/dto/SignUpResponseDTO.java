package com.asifiqbalsekh.EcomBE.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDTO {
    private String username;
    private String email;
    private String message;
}
