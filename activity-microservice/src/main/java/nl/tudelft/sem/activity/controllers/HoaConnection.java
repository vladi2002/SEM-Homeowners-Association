package nl.tudelft.sem.activity.controllers;

import nl.tudelft.sem.activity.domain.Organizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

@Controller
public class HoaConnection {

    private final transient String authorizationLiteral = "Authorization";

    @Autowired
    private transient RestTemplate restTemplate;

    /** Method that finds the HOA of a member through authentication.
     *
     * @param requestAuthHeader bearer with password
     * @param member username of member
     * @return response entity with HOA in the body.
     */
    public ResponseEntity<String> connectFindHoaByMember(@RequestHeader(authorizationLiteral)String requestAuthHeader,
                                                         String member) {
        String hoaResourceUrl = "http://localhost:8083/find-hoa/";
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.set(authorizationLiteral, requestAuthHeader);
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        Organizer organizer = new Organizer(member);
        ResponseEntity<String> response = restTemplate.exchange(hoaResourceUrl + organizer.toString(),
                HttpMethod.GET, entity, String.class);
        return response;
    }
}