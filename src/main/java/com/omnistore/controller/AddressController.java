package com.omnistore.controller;

import com.omnistore.entity.Address;
import com.omnistore.entity.User;
import com.omnistore.services.AddressService;
import com.omnistore.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Address> addAddress(Principal principal, @RequestBody Address address) {
        User user = userService.findUserByEmail(principal.getName());
        address.setUserId(user.getId());
        return ResponseEntity.ok(addressService.addAddress(address));
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAddresses(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return ResponseEntity.ok(addressService.getAddressesByUserId(user.getId()));
    }
}
