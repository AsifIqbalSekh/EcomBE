package com.asifiqbalsekh.EcomBE.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @NotEmpty
    @Size(min = 3, message = "must be more than 2 character")
    private String username;

    @NotEmpty
    @Size(min = 6, message = "must be more than 5 character")
    private String password;
}
