package nl.tudelft.sem.hoa.profiles;

import nl.tudelft.sem.hoa.domain.hoa.RuleRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockRuleRepository")
@Configuration
public class MockRuleRepository {
    @Bean
    @Primary
    public RuleRepository getMockRuleRepository() {
        return Mockito.mock(RuleRepository.class);
    }
}
