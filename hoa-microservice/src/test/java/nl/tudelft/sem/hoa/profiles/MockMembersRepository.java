package nl.tudelft.sem.hoa.profiles;

import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockMembersRepository")
@Configuration
public class MockMembersRepository {
    @Bean
    @Primary
    public MembersRepository getMockMembersRepository() {
        return Mockito.mock(MembersRepository.class);
    }
}
