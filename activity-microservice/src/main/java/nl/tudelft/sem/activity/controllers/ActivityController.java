package nl.tudelft.sem.activity.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import nl.tudelft.sem.activity.authentication.AuthManager;
import nl.tudelft.sem.activity.domain.Activity;
import nl.tudelft.sem.activity.domain.Builder;
import nl.tudelft.sem.activity.domain.Description;
import nl.tudelft.sem.activity.domain.HoaId;
import nl.tudelft.sem.activity.domain.Organizer;
import nl.tudelft.sem.activity.domain.RegistrationService;
import nl.tudelft.sem.activity.domain.Response;
import nl.tudelft.sem.activity.domain.ResponseBuildDirector;
import nl.tudelft.sem.activity.domain.ResponseBuilder;
import nl.tudelft.sem.activity.domain.ResponseOption;
import nl.tudelft.sem.activity.domain.UserDoesNotBelongToThisHoaException;
import nl.tudelft.sem.activity.models.ActivityRegistrationRequestModel;
import nl.tudelft.sem.activity.models.ResponseToActivityRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 * Activity controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class ActivityController {

    private final transient String authorizationLiteral = "Authorization";

    private final transient String hoaResourceUrl = "http://localhost:8083/find-hoa/";

    private final transient AuthManager authManager;

    private final transient RegistrationService registrationService;

    @Autowired
    private transient RestTemplate restTemplate;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public ActivityController(AuthManager authManager, RegistrationService registrationService) {
        this.authManager = authManager;
        this.registrationService = registrationService;
    }


    /**
     * Gets activity by id.
     *
     * @return the activity found in the database with the given id
     */
    @GetMapping("/byId/{id}")
    public ResponseEntity findById(@PathVariable("id") int id) throws Exception {
        try {
            return ResponseEntity.ok("Activity: " + registrationService.findActivityById(id).get().toString());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Gets activity by id.
     *
     * @return the activity found in the database with the given id
     */
    @GetMapping("/getAll")
    public ResponseEntity findAllActivitiesHoa(@RequestHeader(authorizationLiteral) String requestAuthHeader)
            throws Exception {
        try {
            Organizer organizer = new Organizer(authManager.getUsername());
            HoaId belongsTo = findHoa(requestAuthHeader, organizer.toString());
            List<Activity> allActivities = registrationService.findAllActivities(belongsTo).get();
            return ResponseEntity.ok("Activities: " + allActivities.toString());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Gets upcoming activities by hoa id.
     *
     * @return the activity found in the database with the given id
     */
    @GetMapping("/getAllUpcoming")
    public ResponseEntity findAllUpcoming(@RequestHeader(authorizationLiteral) String requestAuthHeader)
            throws Exception {
        try {
            Organizer organizer = new Organizer(authManager.getUsername());
            HoaId belongsTo = findHoa(requestAuthHeader, organizer.toString());
            List<Activity> allActivities = registrationService.findAllActivities(belongsTo).get();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.GERMAN);
            Date currentDateTime = new Date();
            formatter.format(currentDateTime);
            List<Activity> pastActivities = new ArrayList<>();
            for (Activity a : allActivities) {
                if (!a.getDate().after(currentDateTime)) {
                    pastActivities.add(a);
                }
            }
            for (Activity a : pastActivities) {
                allActivities.remove(a);
            }
            return ResponseEntity.ok("Activities: " + allActivities.toString());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint for registration.
     *
     * @param request The registration model
     * @return 200 OK if the registration is successful
     * @throws Exception if a user with this netid already exists
     */
    @PostMapping("/registerActivity")
    public ResponseEntity registerActivity(@RequestHeader(authorizationLiteral) String requestAuthHeader,
                                           @RequestBody ActivityRegistrationRequestModel request)
            throws DateTimeParseException {

        try {
            Organizer organizer = new Organizer(authManager.getUsername());
            HoaId belongsTo = findHoa(requestAuthHeader, organizer.toString());
            System.out.println(belongsTo.toString());
            String dateText = request.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.GERMAN);
            try {
                Date date = formatter.parse(dateText);
                Description description = new Description(request.getDescription());
                Activity created = registrationService.registerActivity(organizer, description, date, belongsTo);
                return ResponseEntity.ok("Activity created successfully! " + created.toString());
            } catch (ParseException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date not recognized.");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint for responding to an activity.
     *
     * @param request The response to an activity model
     * @return 200 OK if recording the response is successful
     * @throws Exception if this response can not be recorded
     */
    @PostMapping("/respond")
    public ResponseEntity respondActivity(@RequestHeader(authorizationLiteral) String requestAuthHeader,
                                          @RequestBody ResponseToActivityRequestModel request)
            throws DateTimeParseException {

        try {
            String responderName = authManager.getUsername();
            HoaId belongsTo = findHoa(requestAuthHeader, responderName);
            int id = Integer.parseInt(request.getActivityId());
            Activity event = registrationService.findActivityById(id).get();
            if (event.getHoaId().equals(belongsTo)) {
                ResponseOption responseOption = ResponseOption.valueOf(request.getResponse());
                Builder builder = new ResponseBuilder();
                ResponseBuildDirector director = new ResponseBuildDirector(builder);
                director.construct(responseOption, responderName);
                Response responseActivity = director.buildActivityResponse();
                registrationService.registerResponse(responseActivity, event);
                return ResponseEntity.ok(responseActivity.toString() + " added to " + event.toString());
            } else {
                throw new UserDoesNotBelongToThisHoaException(belongsTo);
            }
        } catch (UserDoesNotBelongToThisHoaException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Autowired
    private transient HoaConnection hoaConnection;

    public HoaId findHoa(@RequestHeader(authorizationLiteral)String requestAuthHeader, String member) {
        ResponseEntity<String> response = hoaConnection.connectFindHoaByMember(requestAuthHeader, member);
        return new HoaId(response.getBody());
    }

}