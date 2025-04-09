package nl.tudelft.sem.template.authentication.models;

import lombok.Data;
import nl.tudelft.sem.template.authentication.domain.user.NetId;

@Data
public class GetAddressRequestModel {
    private String netId;
}
