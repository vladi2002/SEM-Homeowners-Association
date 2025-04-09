package nl.tudelft.sem.template.authentication.domain.user;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final transient UserRepository userRepository;

    /**
     * Instantiate new AddressService.
     *
     * @param userRepository The user repository
     */
    public AddressService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Update address of given user.
     *
     * @param netId The netID of the user whose address needs to be updated
     * @param address The address that needs to be stored
     * @throws Exception if the user does not exist or no new address is given
     */
    public void updateAddress(NetId netId, Address address) throws Exception {
        if (!userRepository.existsByNetId(netId)) {
            throw new NetIdDoesNotExistException(netId);
        }
        if (address.getCountry() == null && address.getCity() == null && address.getStreet() == null
                && address.getHouseNumber() == 0 && address.getPostalCode() == null) {
            throw new NullPointerException();
        }
        Optional<AppUser> optionalAppUser = userRepository.findAppUserByNetId(netId);
        AppUser user = optionalAppUser.get();
        user.changeAddress(address);
        userRepository.save(user);
    }

    /**
     * Getter that gets address from userRepository by given NetID.
     *
     * @param netId The NetID whose address needs to be gotten
     * @return An address object
     * @throws Exception if NetID does not exist
     */
    public Address getAddress(NetId netId) throws Exception {
        if (!userRepository.existsByNetId(netId)) {
            throw new NetIdDoesNotExistException(netId);
        }
        Optional<AppUser> appUserOptional = userRepository.findAppUserByNetId(netId);
        AppUser user = appUserOptional.get();
        return user.getAddress();
    }
}
