package nl.tudelft.sem.template.authentication.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.template.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.template.authentication.domain.user.Address;
import nl.tudelft.sem.template.authentication.domain.user.AppUser;
import nl.tudelft.sem.template.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.NetId;
import nl.tudelft.sem.template.authentication.domain.user.Password;
import nl.tudelft.sem.template.authentication.domain.user.PasswordHashingService;
import nl.tudelft.sem.template.authentication.domain.user.UserRepository;
import nl.tudelft.sem.template.authentication.framework.integration.utils.JsonUtil;
import nl.tudelft.sem.template.authentication.models.AddressUpdateRequestModel;
import nl.tudelft.sem.template.authentication.models.AuthenticationRequestModel;
import nl.tudelft.sem.template.authentication.models.AuthenticationResponseModel;
import nl.tudelft.sem.template.authentication.models.GetAddressRequestModel;
import nl.tudelft.sem.template.authentication.models.RegistrationRequestModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@SuppressWarnings({"PMD", "Checkstyle"})
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder", "mockTokenGenerator", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UsersTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient PasswordHashingService mockPasswordEncoder;

    @Autowired
    private transient JwtTokenGenerator mockJwtTokenGenerator;

    @Autowired
    private transient AuthenticationManager mockAuthenticationManager;

    @Autowired
    private transient UserRepository userRepository;

    @Test
    public void register_withValidData_worksCorrectly() throws Exception {
        // Arrange
        final NetId testUser = new NetId("SomeUser");
        final Password testPassword = new Password("password123");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);

        RegistrationRequestModel model = new RegistrationRequestModel();
        model.setNetId(testUser.toString());
        model.setPassword(testPassword.toString());

        // Act
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        // Assert
        resultActions.andExpect(status().isOk());

        AppUser savedUser = userRepository.findAppUserByNetId(testUser).orElseThrow();

        assertThat(savedUser.getNetId()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword.toString());
    }

    @Test
    public void register_withExistingUser_throwsException() throws Exception {
        // Arrange
        final NetId testUser = new NetId("SomeUser");
        final Password newTestPassword = new Password("password456");
        final HashedPassword existingTestPassword = new HashedPassword("password123");

        AppUser existingAppUser = new AppUser(testUser, existingTestPassword);
        userRepository.save(existingAppUser);

        RegistrationRequestModel model = new RegistrationRequestModel();
        model.setNetId(testUser.toString());
        model.setPassword(newTestPassword.toString());

        // Act
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        // Assert
        resultActions.andExpect(status().isBadRequest());

        AppUser savedUser = userRepository.findAppUserByNetId(testUser).orElseThrow();

        assertThat(savedUser.getNetId()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(existingTestPassword.toString());
    }

    @Test
    public void login_withValidUser_returnsToken() throws Exception {
        // Arrange
        final NetId testUser = new NetId("SomeUser");
        final Password testPassword = new Password("password123");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);

        when(mockAuthenticationManager.authenticate(argThat(authentication ->
                !testUser.toString().equals(authentication.getPrincipal())
                    || !testPassword.toString().equals(authentication.getCredentials())
        ))).thenThrow(new UsernameNotFoundException("User not found"));

        final String testToken = "testJWTToken";
        when(mockJwtTokenGenerator.generateToken(
            argThat(userDetails -> userDetails.getUsername().equals(testUser.toString())))
        ).thenReturn(testToken);

        AppUser appUser = new AppUser(testUser, testHashedPassword);
        userRepository.save(appUser);

        AuthenticationRequestModel model = new AuthenticationRequestModel();
        model.setNetId(testUser.toString());
        model.setPassword(testPassword.toString());

        // Act
        ResultActions resultActions = mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));


        // Assert
        MvcResult result = resultActions
                .andExpect(status().isOk())
                .andReturn();

        AuthenticationResponseModel responseModel = JsonUtil.deserialize(result.getResponse().getContentAsString(),
                AuthenticationResponseModel.class);

        assertThat(responseModel.getToken()).isEqualTo(testToken);

        verify(mockAuthenticationManager).authenticate(argThat(authentication ->
                testUser.toString().equals(authentication.getPrincipal())
                    && testPassword.toString().equals(authentication.getCredentials())));
    }

    @Test
    public void login_withNonexistentUsername_returns403() throws Exception {
        // Arrange
        final String testUser = "SomeUser";
        final String testPassword = "password123";

        when(mockAuthenticationManager.authenticate(argThat(authentication ->
                testUser.equals(authentication.getPrincipal())
                    && testPassword.equals(authentication.getCredentials())
        ))).thenThrow(new UsernameNotFoundException("User not found"));

        AuthenticationRequestModel model = new AuthenticationRequestModel();
        model.setNetId(testUser);
        model.setPassword(testPassword);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        // Assert
        resultActions.andExpect(status().isForbidden());

        verify(mockAuthenticationManager).authenticate(argThat(authentication ->
                testUser.equals(authentication.getPrincipal())
                    && testPassword.equals(authentication.getCredentials())));

        verify(mockJwtTokenGenerator, times(0)).generateToken(any());
    }

    @Test
    public void login_withInvalidPassword_returns403() throws Exception {
        // Arrange
        final String testUser = "SomeUser";
        final String wrongPassword = "password1234";
        final String testPassword = "password123";
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(new Password(testPassword))).thenReturn(testHashedPassword);

        when(mockAuthenticationManager.authenticate(argThat(authentication ->
                testUser.equals(authentication.getPrincipal())
                    && wrongPassword.equals(authentication.getCredentials())
        ))).thenThrow(new BadCredentialsException("Invalid password"));

        AppUser appUser = new AppUser(new NetId(testUser), testHashedPassword);
        userRepository.save(appUser);

        AuthenticationRequestModel model = new AuthenticationRequestModel();
        model.setNetId(testUser);
        model.setPassword(wrongPassword);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        // Assert
        resultActions.andExpect(status().isUnauthorized());

        verify(mockAuthenticationManager).authenticate(argThat(authentication ->
                testUser.equals(authentication.getPrincipal())
                    && wrongPassword.equals(authentication.getCredentials())));

        verify(mockJwtTokenGenerator, times(0)).generateToken(any());
    }

    // Test that getAddress gets correct address
    @Test
    public void get_address_works() throws Exception {
        // Arrange
        final String testUser = "SomeUser";
        final String wrongPassword = "password1234";
        final String testPassword = "password123";
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(new Password(testPassword))).thenReturn(testHashedPassword);

        AppUser appUser = new AppUser(new NetId(testUser), testHashedPassword, "Co", "Ci", "Str", 23, "Po");
        userRepository.save(appUser);

        GetAddressRequestModel model = new GetAddressRequestModel();
        model.setNetId(testUser);

        ResultActions resultActions = mockMvc.perform(post("/get-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"country\":\"Co\",\"city\":\"Ci\",\"street\":\"Str"
                    + "\",\"houseNumber\":23,\"postalCode\":\"Po\"}"));
    }

    // Test that getAddress throws not found exception when netID input does not exist
    @Test
    public void get_address_throws_404() throws Exception {

        GetAddressRequestModel model = new GetAddressRequestModel();
        model.setNetId("testUser");

        ResultActions resultActions = mockMvc.perform(post("/get-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        resultActions
                .andExpect(status().isNotFound());
    }

    // Test that updateAddress works
    @Test
    public void update_address_works() throws Exception {
        // Arrange
        final String testUser = "SomeUser";
        final String testPassword = "password123";
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(new Password(testPassword))).thenReturn(testHashedPassword);

        AppUser appUser = new AppUser(new NetId(testUser), testHashedPassword);
        userRepository.save(appUser);

        AddressUpdateRequestModel updateModel = new AddressUpdateRequestModel();
        updateModel.setNetId(testUser);
        updateModel.setCountry("Country");

        GetAddressRequestModel getModel = new GetAddressRequestModel();
        getModel.setNetId(testUser);

        ResultActions updateAddress = mockMvc.perform(post("/update-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(updateModel)));

        updateAddress
                .andExpect(status().isOk());

        AppUser savedUser = userRepository.findAppUserByNetId(new NetId(testUser)).orElseThrow();
        Address expectedAddress = new Address("Country", null, null, 0, null);
        assertThat(savedUser.getAddress())
                .isEqualTo(expectedAddress);
    }

    // Test that updateAddress overrides previous address and does not replace untouched information
    @Test
    public void update_address_works_overrides() throws Exception {
        // Arrange
        final String testUser = "SomeUser";
        final String testPassword = "password123";
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(new Password(testPassword))).thenReturn(testHashedPassword);

        AppUser appUser = new AppUser(new NetId(testUser), testHashedPassword, "Co", "Ci", "Str", 23, "Po");
        userRepository.save(appUser);

        AddressUpdateRequestModel updateModel = new AddressUpdateRequestModel();
        updateModel.setNetId(testUser);
        updateModel.setCountry("Country");

        GetAddressRequestModel getModel = new GetAddressRequestModel();
        getModel.setNetId(testUser);

        ResultActions updateAddress = mockMvc.perform(post("/update-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(updateModel)));

        updateAddress
                .andExpect(status().isOk());

        AppUser savedUser = userRepository.findAppUserByNetId(new NetId(testUser)).orElseThrow();
        Address expectedAddress = new Address("Country", "Ci", "Str", 23, "Po");
        assertThat(savedUser.getAddress())
                .isEqualTo(expectedAddress);
    }

    // Test that updateAddress updates all inputs
    @Test
    public void update_address_works_multiple_input() throws Exception {
        // Arrange
        final String testUser = "SomeUser";
        final String testPassword = "password123";
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(new Password(testPassword))).thenReturn(testHashedPassword);

        AppUser appUser = new AppUser(new NetId(testUser), testHashedPassword, "Co", "Ci", "Str", 23, "Po");
        userRepository.save(appUser);

        AddressUpdateRequestModel updateModel = new AddressUpdateRequestModel();
        updateModel.setNetId(testUser);
        updateModel.setCountry("Country");
        updateModel.setCity("City");

        GetAddressRequestModel getModel = new GetAddressRequestModel();
        getModel.setNetId(testUser);

        ResultActions updateAddress = mockMvc.perform(post("/update-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(updateModel)));

        updateAddress
                .andExpect(status().isOk());

        AppUser savedUser = userRepository.findAppUserByNetId(new NetId(testUser)).orElseThrow();
        Address expectedAddress = new Address("Country", "City", "Str", 23, "Po");
        assertThat(savedUser.getAddress())
                .isEqualTo(expectedAddress);
    }

    // Test that updateAddress throws not found exception when netID input does not exist
    @Test
    public void update_address_throws_404() throws Exception {

        AddressUpdateRequestModel model = new AddressUpdateRequestModel();
        model.setNetId("testUser");

        ResultActions resultActions = mockMvc.perform(post("/update-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        resultActions
                .andExpect(status().isNotFound());
    }

    @Test
    public void update_address_fail() throws Exception {
        // Arrange
        final String testUser = "SomeUser";
        final String testPassword = "password123";
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(new Password(testPassword))).thenReturn(testHashedPassword);

        AppUser appUser = new AppUser(new NetId(testUser), testHashedPassword);
        userRepository.save(appUser);

        AddressUpdateRequestModel updateModel = new AddressUpdateRequestModel();
        updateModel.setNetId(testUser);
        updateModel.setHouseNumber(0);

        GetAddressRequestModel getModel = new GetAddressRequestModel();
        getModel.setNetId(testUser);

        ResultActions updateAddress = mockMvc.perform(post("/update-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(updateModel)));

        updateAddress
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addressEquals() {
        Address address1 = new Address("Co", "Ci", "Str", 23, "Po");
        Address address2 = new Address("Co", "Ci", "Str", 23, "Po");
        assertThat(address1).isEqualTo(address2);
    }

    @Test
    public void addressEquals1() {
        Address address1 = new Address("Co", "Ci", "Str", 23, "Po");
        assertThat(address1).isEqualTo(address1);
    }

    @Test
    public void addressEquals2() {
        Address address1 = new Address("Co", "Ci", "Str", 23, "Po");
        assertThat(address1).isNotEqualTo(null);
    }

    @Test
    public void addressEquals3() {
        Address address1 = new Address("Co", "Ci", "Str", 23, "Po");
        Address address2 = new Address("o", "C", "Str", 24, "Po");
        assertThat(address1).isNotEqualTo(address2);
    }

    @Test
    public void addressHashEqual() {
        Address address1 = new Address("Co", "Ci", "Str", 23, "Po");
        Address address2 = new Address("Co", "Ci", "Str", 23, "Po");
        assertThat(address1.hashCode()).isEqualTo(address2.hashCode());
    }
}
