package nl.tudelft.sem.hoa.domain.hoa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for HOAs.
 */
@Repository
public interface HoaRepository extends JpaRepository<Hoa, Integer> {
    /**
     * Find user by name.
     */
    @Query("SELECT a FROM Hoa a WHERE a.hoaId = ?1")
    Optional<Hoa> findHoaByHoaId(HoaId hoaId);

    /**
     * Check if an existing Hoa already uses the name.
     */
    boolean existsByHoaId(HoaId hoaId);

    @Query("SELECT a FROM Hoa a WHERE a.country = ?1 AND a.city = ?2")
    Optional<List<Hoa>> findByCountryAndCity(String country, String city);

}
