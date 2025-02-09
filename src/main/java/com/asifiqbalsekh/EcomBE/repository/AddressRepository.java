package com.asifiqbalsekh.EcomBE.repository;

import com.asifiqbalsekh.EcomBE.model.Address;
import com.asifiqbalsekh.EcomBE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByStreetAndCityAndStateAndZipCodeAndCountryAndUser(
            String street, String city, String state, String zipCode, String country, User user
    );
}
