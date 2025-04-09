package nl.tudelft.sem.template.authentication.controllers;

import nl.tudelft.sem.template.authentication.domain.user.Address;
import nl.tudelft.sem.template.authentication.domain.user.AddressService;
import nl.tudelft.sem.template.authentication.domain.user.NetId;
import nl.tudelft.sem.template.authentication.domain.user.NetIdDoesNotExistException;
import nl.tudelft.sem.template.authentication.models.AddressUpdateRequestModel;
import nl.tudelft.sem.template.authentication.models.GetAddressRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AddressController {

    private final transient AddressService addressService;

    /**
     * Instantiates a new Address Controller.
     *
     * @param addressService The address service
     */
    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * Endpoint for updating a users address.
     *
     * @param request The address update model
     * @return 200 OK if the address update is successful
     * @throws Exception if the given NetID does not exist
     */
    @PostMapping("/update-address")
    public ResponseEntity updateAddress(@RequestBody AddressUpdateRequestModel request) throws Exception {
        try {
            NetId netId = new NetId(request.getNetId()); // Will be changed to token once I figure that out
            String country = request.getCountry();
            String city = request.getCity();
            String street = request.getStreet();
            int houseNumber = request.getHouseNumber();
            String postalCode = request.getPostalCode();

            addressService.updateAddress(netId, new Address(country, city, street, houseNumber, postalCode));
        } catch (Exception e) {
            if (e.getClass().equals(NetIdDoesNotExistException.class)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Gets address by netID.
     *
     * @return the address of given netID
     * @throws Exception if netID not found
     */
    @PostMapping("/get-address")
    public ResponseEntity<Address> getAddress(@RequestBody GetAddressRequestModel request) throws Exception {
        try {
            NetId netId = new NetId(request.getNetId());
            Address address = addressService.getAddress(netId);
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            if (e.getClass().equals(NetIdDoesNotExistException.class)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
    }
}
