package com.asifiqbalsekh.EcomBE.controller;

import com.asifiqbalsekh.EcomBE.dto.AddressDTO;
import com.asifiqbalsekh.EcomBE.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {

        return new ResponseEntity<>(addressService.savedAddress(addressDTO), HttpStatus.CREATED);

    }
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,@Valid @RequestBody AddressDTO addressDTO) {

        return new ResponseEntity<>(addressService.updateAddressUsingID(addressId,addressDTO), HttpStatus.OK);

    }


    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        return new ResponseEntity<>(addressService.getAllAddresses(),HttpStatus.FOUND);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getSpecificAddress(@PathVariable Long addressId) {
        return new ResponseEntity<>(addressService.getAddressUsingId(addressId),HttpStatus.FOUND);
    }

    @GetMapping("/addresses/user")
    public ResponseEntity<List<AddressDTO>> getAllUserAddresses() {
        return new ResponseEntity<>(addressService.getAllAddressesOfUser(),HttpStatus.FOUND);
    }
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddress(@PathVariable Long addressId) {
        return new ResponseEntity<>(addressService.deleteAddressUsingId(addressId),HttpStatus.OK);
    }
}
