package nl.tudelft.sem.hoa.profiles;

import nl.tudelft.sem.hoa.domain.hoa.SetUpService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockSetUpServiceProfile")
@Configuration
public class MockSetUpServiceProfile {
    @Bean
    @Primary  // marks this bean as the first bean to use when trying to inject an AuthenticationManager
    public SetUpService getMockTokenVerifier() {
        return Mockito.mock(SetUpService.class);
    }
}
