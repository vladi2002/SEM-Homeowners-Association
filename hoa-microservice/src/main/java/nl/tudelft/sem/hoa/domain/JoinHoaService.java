package nl.tudelft.sem.hoa.domain;

import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.MemberAlreadyInHoaException;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import org.springframework.stereotype.Component;

@Component
public class JoinHoaService {
    private final transient MembersRepository membersRepository;


    /**
     * Instantiates a new UserService.
     *
     * @param memberRepository  the Hoa repository
     *
     */
    public JoinHoaService(MembersRepository memberRepository) {
        this.membersRepository = memberRepository;
    }

    /**
     * SetUp a new Hoa.
     *
     * @param hoa name of Hoa
     * @param user netId of user
     * @throws Exception if the Hoa name already exists
     */
    public MemberAppUser joinHoa(Hoa hoa, String user, Address address) throws Exception {

        if (checkUserIsInside(user)) {

            // Create new Hoa
            MemberAppUser memberAppUser = new MemberAppUser(user, hoa, address);
            membersRepository.save(memberAppUser);

            return memberAppUser;
        }
        throw new MemberAlreadyInHoaException(user, hoa.getHoaId());

    }

    /**
     * Method used for checking if a user is currently in a nonspecific Hoa.
     *
     * @param username netId of user.
     * @return False if user is in Hoa, true if user is not in Hoa.
     */
    public boolean checkUserIsInside(String username) {
        return !membersRepository.existsByUsername(username);
    }

    /**
     * Leave the HOA.
     *
     * @param user user
     * @throws Exception exception
     */
    public void leaveHoa(String user) throws Exception {

        if (!checkUserIsInside(user)) {

            MemberAppUser member = membersRepository.findMemberAppUsersByUsername(user).get();

            System.out.println("found " + member.toString());

            membersRepository.delete(member);

        } else {
            throw new Exception();
        }


    }

    /**
     * Method for HOA finding from net ID.
     *
     * @param username net Id
     * @return hoa id
     * @throws Exception not found exception
     */
    public HoaId findHoa(String username) throws Exception {

        if (!checkUserIsInside(username)) {

            MemberAppUser member = membersRepository.findMemberAppUsersByUsername(username).get();

            System.out.println("found " + member.toString());

            return member.getHoaId();

        }
        throw new Exception();
    }

    /**
     * Finds member from username.
     *
     * @param username username
     * @return member
     * @throws Exception if it finds something bad
     */
    public MemberAppUser findMember(String username) throws Exception {

        if (!checkUserIsInside(username)) {

            MemberAppUser member = membersRepository.findMemberAppUsersByUsername(username).get();

            System.out.println("found " + member.toString());
            return member;

        }
        throw new Exception();
    }

    /**
     * Updates Role from member.
     *
     * @param member member
     * @param role role
     * @throws Exception if it throws
     */
    public void updateRoleMember(MemberAppUser member, String role) throws Exception {
        try {
            MemberAppUser memberAppUser = membersRepository.findById(member.getId()).get();
            memberAppUser.setRole(role);
            membersRepository.save(memberAppUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

}
