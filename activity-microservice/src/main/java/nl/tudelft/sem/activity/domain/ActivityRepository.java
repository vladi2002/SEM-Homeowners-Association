package nl.tudelft.sem.activity.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for quering and persisting activity aggregate roots.
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    /**
     * Find activity by id.
     */
    @Query("SELECT a FROM Activity a WHERE a.id = ?1")
    Optional<Activity> findById(int id);

    /**
     * Find activity by id.
     */
    @Query("SELECT a FROM Activity a WHERE a.hoaId = ?1")
    Optional<List<Activity>> findByHoaId(HoaId hoaId);

    /**
     * Check if an existing user already uses a NetID.
     */
    boolean existsById(int id);

    /**
     * Check if an existing activity already has this organizer and description.
     */
    boolean existsByOrganizerAndDescription(Organizer organizer, Description description);
}
