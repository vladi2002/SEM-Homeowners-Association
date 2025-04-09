package nl.tudelft.sem.hoa.profiles;

import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockHoaRepository")
@Configuration
public class MockHoaRepository {
    @Bean
    @Primary
    public HoaRepository getMockHoaRepository() {
        return Mockito.mock(HoaRepository.class);
    }
}
