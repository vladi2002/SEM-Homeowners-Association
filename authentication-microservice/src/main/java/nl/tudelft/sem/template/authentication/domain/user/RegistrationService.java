package nl.tudelft.sem.template.authentication.domain.user;

import org.springframework.stereotype.Service;


/**
 * A DDD service for registering a new user.
 */
@Service
public class RegistrationService {

    private final transient UserRepository userRepository;
    private final transient PasswordHashingService passwordHashingService;

    /**
     * Instantiates a new UserService.
     *
     * @param userRepository  the user repository
     * @param passwordHashingService the password encoder
     */
    public RegistrationService(UserRepository userRepository, PasswordHashingService passwordHashingService) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
    }

    /**
     * Register a new user.
     *
     * @param netId    The NetID of the user
     * @param password The password of the user
     * @throws Exception if the user already exists
     */
    public AppUser registerUser(NetId netId, Password password) throws Exception {

        if (checkNetIdIsUnique(netId)) {
            // Hash password
            HashedPassword hashedPassword = passwordHashingService.hash(password);

            // Create new account
            AppUser user = new AppUser(netId, hashedPassword);
            userRepository.save(user);

            return user;
        }

        throw new NetIdAlreadyInUseException(netId);
    }

    /**
     * Updates an existing user.
     *
     * @param netId The NetID of the user
     * @param password The password of the user
     * @throws Exception if the user doesn't exist yet
     */

    public void updatePassword(NetId netId, Password password) throws Exception {
        if (checkNetIdIsUnique(netId)) {
            throw new UserDoesNotExistException(netId);
        }

        HashedPassword hashedPassword = passwordHashingService.hash(password);

        AppUser appUser = userRepository.findAppUserByNetId(netId).get();
        AppUser user = userRepository.findById(appUser.getId()).get();

        user.changePassword(hashedPassword);
        userRepository.save(user);
    }

    public boolean checkNetIdIsUnique(NetId netId) {
        return !userRepository.existsByNetId(netId);
    }
}
