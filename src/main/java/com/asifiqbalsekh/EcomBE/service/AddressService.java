package com.asifiqbalsekh.EcomBE.service;

import com.asifiqbalsekh.EcomBE.dto.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO savedAddress(@Valid AddressDTO addressDTO);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressUsingId(Long addressId);

    List<AddressDTO> getAllAddressesOfUser();

    AddressDTO deleteAddressUsingId(Long addressId);

    AddressDTO updateAddressUsingID(Long addressId, @Valid AddressDTO addressDTO);
}
