package nl.tudelft.sem.hoa.models;

import lombok.Data;

@Data
public class CreateHoaRequestModel {
    private String id;
    private String country;
    private String city;

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
