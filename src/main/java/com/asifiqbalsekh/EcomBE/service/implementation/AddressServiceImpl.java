package com.asifiqbalsekh.EcomBE.service.implementation;

import com.asifiqbalsekh.EcomBE.config.AuthUtil;
import com.asifiqbalsekh.EcomBE.dto.AddressDTO;
import com.asifiqbalsekh.EcomBE.exception.APIException;
import com.asifiqbalsekh.EcomBE.exception.ResourceNotFoundException;
import com.asifiqbalsekh.EcomBE.model.Address;
import com.asifiqbalsekh.EcomBE.model.User;
import com.asifiqbalsekh.EcomBE.repository.AddressRepository;
import com.asifiqbalsekh.EcomBE.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;

    @Override
    public AddressDTO savedAddress(AddressDTO addressDTO) {
        User user=authUtil.userDetails();

        boolean existedAddress =addressRepository.findByStreetAndCityAndStateAndZipCodeAndCountryAndUser(
                addressDTO.getStreet(), addressDTO.getCity(), addressDTO.getState(),
                addressDTO.getZipCode(), addressDTO.getCountry(), user
        ).isPresent();

        if(existedAddress){
            throw new APIException("Address already exists for the user");
        }

        Address address=modelMapper.map(addressDTO, Address.class);
        address.setUser(user);

        address=addressRepository.save(address);

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {

        List<Address> addresses=addressRepository.findAll();
        if(addresses.isEmpty()){
            throw new APIException("No address saved till now!");
        }
        return addresses.stream().map(address->
                modelMapper.map(address, AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO getAddressUsingId(Long addressId) {
        Address address=addressRepository.findById(addressId).orElseThrow(()->
                new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddressesOfUser() {
        User user=authUtil.userDetails();
        List<Address> addresses=user.getAddresses();
        if(addresses.isEmpty()){
            throw new ResourceNotFoundException("Address", "userName", user.getUserName());
        }
        return addresses.stream().map(address->modelMapper.map(address, AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO deleteAddressUsingId(Long addressId) {
        Address address=addressRepository.findById(addressId).orElseThrow(()->
                new ResourceNotFoundException("Address", "addressId", addressId));
        User user=authUtil.userDetails();
        if(!user.equals(address.getUser())){
            throw new APIException("User does not belong to this address");
        }
        addressRepository.delete(address);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressDTO updateAddressUsingID(Long addressId, AddressDTO addressDTO) {

        Address address=addressRepository.findById(addressId).orElseThrow(()->
                new ResourceNotFoundException("Address", "addressId", addressId));
        User user=authUtil.userDetails();
        if(!user.equals(address.getUser())){
            throw new APIException("User does not belong to this address");
        }
        boolean existedAddress =addressRepository.findByStreetAndCityAndStateAndZipCodeAndCountryAndUser(
                addressDTO.getStreet(), addressDTO.getCity(), addressDTO.getState(),
                addressDTO.getZipCode(), addressDTO.getCountry(), user
        ).isPresent();

        if(existedAddress){
            throw new APIException("Address already exists for the user");
        }

        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        address.setStreet(addressDTO.getStreet());

        address=addressRepository.save(address);
        return modelMapper.map(address, AddressDTO.class);
    }

}
