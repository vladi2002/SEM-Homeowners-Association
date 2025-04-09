package nl.tudelft.sem.hoa.domain.hoa;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;


@Service
public class SetUpService {

    private final transient HoaRepository hoaRepository;


    /**
     * Instantiates a new UserService.
     *
     * @param hoaRepository  the Hoa repository
     *
     */
    public SetUpService(HoaRepository hoaRepository) {
        this.hoaRepository = hoaRepository;

    }

    /**
     * SetUp a new Hoa.
     *
     * @param hoaId   name of Hoa
     * @param city The city of the Hoa
     * @throws Exception if the Hoa name already exists
     */
    public Hoa setUpHoa(HoaId hoaId, String country, String city) throws Exception {

        if (hoaId != null && checkHoaIdIsUnique(hoaId)) {

            // Create new Hoa
            Hoa hoa = new Hoa(hoaId, country, city);
            hoaRepository.save(hoa);

            return hoa;
        }
        throw new HoaAlreadyExistException(hoaId);

    }

    /**
     * Checks if HoaId is Unique.
     *
     * @param id id
     * @return boolean
     */
    public boolean checkHoaIdIsUnique(HoaId id) {
        return !(hoaRepository.existsByHoaId(id));
    }

    /**
     * Finds a list of HOAs based on country and city.
     *
     * @param country country
     * @param city city
     * @return found list
     * @throws Exception exception
     */
    public Optional<List<Hoa>> findByCountryAndCity(String country, String city) throws Exception {

        if (country != null && city != null) {
            Optional<List<Hoa>> found = hoaRepository.findByCountryAndCity(country, city);
            return found;
        }

        throw new Exception();
    }

    /**
     * Finds HOA from its id.
     *
     * @param hoaId hoaID
     * @return HOA if found, else null
     * @throws Exception exception
     */
    public Hoa findHoaByHoaId(String hoaId) throws Exception {
        if (hoaId != null) {
            Hoa hoa = hoaRepository.findHoaByHoaId(new HoaId(hoaId)).get();

            System.out.println("found hoa");
            return hoa;
        }

        throw new Exception();
    }


}
