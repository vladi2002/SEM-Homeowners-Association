package nl.tudelft.sem.hoa.domain.elections;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionRepository extends JpaRepository<Election, String> {

    Optional<Election> findByElectionId(String electionId);

    boolean existsByElectionId(String electionId);

    @Query("SELECT m FROM MemberAppUser m WHERE m.hoaId = ?1 AND m.isBoardMember = true")
    List<MemberAppUser> findByBoard(HoaId hoaId);

}
