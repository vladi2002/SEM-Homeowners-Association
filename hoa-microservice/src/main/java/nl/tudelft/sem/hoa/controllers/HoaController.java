package nl.tudelft.sem.hoa.controllers;

import java.util.List;
import nl.tudelft.sem.hoa.authentication.AuthManager;
import nl.tudelft.sem.hoa.domain.JoinHoaService;
import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.SetUpService;
import nl.tudelft.sem.hoa.models.CreateHoaRequestModel;
import nl.tudelft.sem.hoa.models.FindHoaModel;
import nl.tudelft.sem.hoa.models.GetAddressRequestModel;
import nl.tudelft.sem.hoa.models.JoinHoaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HoaController {
    private final transient SetUpService setUpService;
    private final transient AuthManager authManager;
    private final transient JoinHoaService joinHoaService;
    @Autowired
    private transient RestTemplate restTemplate;

    /**
     * Instantiates a new HoaController.
     *
     * @param setUpService the setUpService
     * @param authManager the authManager
     * @param joinHoaService the joinHoaService
     */
    @Autowired
    public HoaController(SetUpService setUpService, JoinHoaService joinHoaService, AuthManager authManager) {
        this.joinHoaService = joinHoaService;
        this.setUpService = setUpService;
        this.authManager = authManager;
    }

    /**
     * Creates a new Hoa.
     *
     * @return new Hoa
     */
    @PostMapping("/create")
    public ResponseEntity<String> createHoa(@RequestHeader("Authorization") String requestAuthHeader,
                                            @RequestBody CreateHoaRequestModel request) {
        try {
            if (!joinHoaService.checkUserIsInside(authManager.getNetId())) {
                return ResponseEntity.badRequest().build();
            }
            try {
                getStringResponseEntity(requestAuthHeader);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.notFound().build();
            }

            HoaId hoaId = new HoaId(request.getId()); // Will be changed to token once I figure that out
            String country = request.getCountry();
            String city = request.getCity();

            Address address = getStringResponseEntity(requestAuthHeader);
            Hoa hoa = setUpService.setUpHoa(hoaId, country, city);
            joinHoaService.joinHoa(hoa, authManager.getNetId(), address);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Finds and returns a list of HOAs based on the country and city.
     *
     * @param request request
     * @return list of Hoas
     */
    @GetMapping("/find")
    public ResponseEntity<String> findHoa(@RequestBody FindHoaModel request) {

        try {

            List<Hoa> hoaList = setUpService
                    .findByCountryAndCity(request.getCountry(), request.getCity()).get();

            String output = "";

            for (Hoa h : hoaList) {
                output += h.toString();
                output += "\n";
            }
            return ResponseEntity.ok(output);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Joins an HOA based on the id.
     *
     * @return ok string
     */
    @PostMapping("/join")
    public ResponseEntity<String> joinHoa(@RequestHeader("Authorization") String requestAuthHeader,
                                          @RequestBody JoinHoaModel request) {
        try {
            if (joinHoaService.checkUserIsInside(authManager.getNetId())) {

                String hoaId = request.getHoaId();

                Hoa hoa = setUpService.findHoaByHoaId(hoaId);

                System.out.println("found " + hoaId);

                Address address = getStringResponseEntity(requestAuthHeader);

                joinHoaService.joinHoa(hoa, authManager.getNetId(), address);
                return ResponseEntity.ok(authManager.getNetId()
                        + " joined hoa " + hoa.getHoaId().toString());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    private Address getStringResponseEntity(String requestAuthHeader) {

        ResponseEntity<String> response = connectToAuthentication(requestAuthHeader);

        System.out.println(response.getBody());
        String[] bodyResponse = response.getBody().split(",");
        String country = bodyResponse[0].split(":")[1];
        String city = bodyResponse[1].split(":")[1];
        String street = bodyResponse[2].split(":")[1];
        int streetNumber = Integer.parseInt(bodyResponse[3].split(":")[1]);
        String postalCode = bodyResponse[4].split(":")[1].split("}")[0];


        Address address = new Address(country, city, street, streetNumber, postalCode);

        System.out.println(address);
        return address;
    }

    private final transient String addressResourceUrl = "http://localhost:8081/get-address";

    private ResponseEntity<String> connectToAuthentication(String requestAuthHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", requestAuthHeader);

        GetAddressRequestModel getAddressRequestModel = new GetAddressRequestModel();
        getAddressRequestModel.setNetId(authManager.getNetId());

        HttpEntity<?> entity = new HttpEntity<Object>(getAddressRequestModel, headers);

        ResponseEntity<String> response = restTemplate.exchange(addressResourceUrl,
                HttpMethod.POST, entity, String.class);
        return response;
    }

    /**
     * Makes the User leave the HOA.
     *
     * @return ok string
     */
    @PostMapping("/leave")
    public ResponseEntity<String> leaveHoa() {
        try {
            joinHoaService.leaveHoa(authManager.getNetId());
            return ResponseEntity.ok(authManager.getNetId() + " has left the HOA ");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint for Vladi. Finds an HOA from the user.
     *
     * @param username netId
     * @return hoa ID
     */
    @GetMapping("/find-hoa/{username}")
    public ResponseEntity<String> findHoaFromUsername(@PathVariable String username) {
        try {
            HoaId hoaId = joinHoaService.findHoa(username);
            return ResponseEntity.ok(hoaId.toString());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}