package nl.tudelft.sem.hoa.models;

import lombok.Data;

@Data
public class FindHoaModel {
    private String country;
    private String city;

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
