package nl.tudelft.sem.activity.domain;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * A DDD service for registering a new activity.
 */
@Service
public class RegistrationService {
    private final transient ActivityRepository activityRepository;

    /**
     * Instantiates a new UserService.
     *
     * @param activityRepository  the activity repository
     */
    public RegistrationService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Register a new user.
     *
     * @param organizer    The Organizer of the activity
     * @param description The description of the activity
     * @throws Exception if the user already exists
     */
    public Activity registerActivity(Organizer organizer, Description description,
                                     Date date, HoaId hoaId) throws Exception {

        if (checkActivityIsUnique(organizer, description)) {
            // Create new account
            Activity activity = new Activity(organizer, description, date, hoaId);
            activity.setOrganizerResponseToGoing();
            activityRepository.save(activity);

            return activity;
        }

        throw new ActivityAlreadyCreatedException(organizer, description);
    }

    /**
     * Register a new response to an activity.
     *
     * @param response    The Response of the activity
     * @param activity The activity
     * @throws Exception if the response exists
     */
    public void registerResponse(Response response, Activity activity) {
        activity.addResponse(response);
        activityRepository.save(activity);
    }

    /**
     * Find activity in table corresponding to this id.
     *
     * @param id The activity id we are searching by
     * @return The activity if it exists
     * @throws Exception if such an activity does not exist
     */
    public Optional<Activity> findActivityById(int id) throws Exception {

        if (checkActivityExistsById(id)) {
            try {
                Optional<Activity> found = activityRepository.findById(id);
                return found;
            } catch (Exception e) {
                e.printStackTrace();
                throw new ActivityNotExistsException(id);
            }

        }

        throw new ActivityNotExistsException(id);
    }

    /**
     * Find all activities in table corresponding to this hoa id.
     *
     * @param hoaId The HOA id we are searching by
     * @return The activities if they exists
     * @throws Exception if such do not exist
     */
    public Optional<List<Activity>> findAllActivities(HoaId hoaId) throws ActivitiesNotFoundException {
        try {
            Optional<List<Activity>> found = activityRepository.findByHoaId(hoaId);
            return found;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ActivitiesNotFoundException(hoaId);
        }
    }

    public boolean checkActivityIsUnique(Organizer organizer, Description description) {
        return !activityRepository.existsByOrganizerAndDescription(organizer, description);
    }

    public boolean checkActivityExistsById(int id) {
        return activityRepository.existsById(id);
    }
}
