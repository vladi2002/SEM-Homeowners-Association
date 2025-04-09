package nl.tudelft.sem.template.authentication.controllers;

import nl.tudelft.sem.template.authentication.domain.user.Address;
import nl.tudelft.sem.template.authentication.domain.user.AddressService;
import nl.tudelft.sem.template.authentication.domain.user.NetId;
import nl.tudelft.sem.template.authentication.domain.user.NetIdDoesNotExistException;
import nl.tudelft.sem.template.authentication.domain.user.Password;
import nl.tudelft.sem.template.authentication.domain.user.RegistrationService;
import nl.tudelft.sem.template.authentication.models.AddressUpdateRequestModel;
import nl.tudelft.sem.template.authentication.models.GetAddressRequestModel;
import nl.tudelft.sem.template.authentication.models.RegistrationRequestModel;
import nl.tudelft.sem.template.authentication.models.UpdateRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    private final transient RegistrationService registrationService;
    private final transient AuthenticationManager authenticationManager;

    /**
     * Instantiates a new User Controller.
     *
     * @param registrationService The registration service
     * @param authenticationManager The authentication manager
     */
    @Autowired
    public UserController(RegistrationService registrationService,
                          AuthenticationManager authenticationManager) {
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Endpoint for registration.
     *
     * @param request The registration model
     * @return 200 OK if the registration is successful
     * @throws Exception if a user with this netID already exists
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegistrationRequestModel request) throws Exception {

        try {
            NetId netId = new NetId(request.getNetId());
            Password password = new Password(request.getPassword());
            registrationService.registerUser(netId, password);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for updating users.
     *
     * @param request The update model
     * @return 200 OK if the update is successful
     * @throws Exception if no user with this netID already exists
     */
    @PostMapping("/updatePassword")
    public ResponseEntity update(@RequestBody UpdateRequestModel request) throws Exception {
        try {
            NetId netId = new NetId(request.getNetId());
            Password oldPassword = new Password(request.getOldPassword());
            Password newPassword = new Password(request.getNewPassword());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            netId,
                            oldPassword));

            registrationService.updatePassword(netId, newPassword);

        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
