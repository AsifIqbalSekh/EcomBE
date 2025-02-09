package com.asifiqbalsekh.EcomBE.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private Long addressId;

    @NotEmpty
    @Size(min = 3, message = "must be 3 character")
    private String street;

    @NotEmpty
    @Size(min = 3, message = "must be 3 character")
    private String city;

    @NotEmpty
    @Size(min = 2, message = "must be 2 character")
    private String state;

    @NotEmpty
    @Size(min = 6,max = 6, message = "must be exact 6 character long")
    private String zipCode;

    @NotEmpty
    @Size(min = 3, message = "must be 3 character")
    private String country;

    private UserAddressDTO user;
}
