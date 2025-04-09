package nl.tudelft.sem.hoa.domain.hoa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for HOAs.
 */
@Repository
public interface MembersRepository extends JpaRepository<MemberAppUser, Integer> {

    /**
     * Check if there is a user already with that NetId.
     */
    boolean existsByUsername(String username);

    @Query("SELECT a FROM MemberAppUser a WHERE a.username = ?1")
    Optional<MemberAppUser> findMemberAppUsersByUsername(String netId);


}
