package nl.tudelft.sem.hoa.domain.proposals;

import java.util.Locale;
import javax.persistence.AttributeConverter;

public class ProposalTypeAttributeConverter implements AttributeConverter<ProposalType, String> {
    @Override
    public String convertToDatabaseColumn(ProposalType attribute) {
        return attribute.toString();
    }

    @Override
    public ProposalType convertToEntityAttribute(String dbData) {
        return ProposalType.valueOf(dbData.toUpperCase(Locale.ENGLISH));
    }
}
