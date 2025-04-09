package nl.tudelft.sem.hoa.domain.hoa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.hoa.domain.HasEvents;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "members")
@NoArgsConstructor
public class MemberAppUser extends HasEvents {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private int id;


    @Column(name = "isBoardMember")
    private boolean isBoardMember;

    @Column(name = "belongsToHoaId")
    @Convert(converter = HoaIdAttributeConverter.class)
    private HoaId hoaId;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "houseNumber")
    private int number;

    @Column(name = "postalCode")
    private String postalCode;

    @Column(name = "role")
    private String role;
    //can be Member, Applicant, Board

    @Column(name = "Time")
    private String time;

    /**
     * Creates Member App User class.
     *
     * @param netId new user net ID
     * @param hoa HOA to add the user to
     */
    public MemberAppUser(String netId, Hoa hoa, Address address) {
        this.isBoardMember = false;
        this.hoaId = hoa.getHoaId();
        this.city = hoa.getCity();
        this.country = hoa.getCountry();
        this.street = address.getStreet();
        this.number = address.getHouseNumber();
        this.postalCode = address.getPostalCode();
        this.username = netId;
        this.role = "MEMBER";
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        this.time = LocalDateTime.now().format(myFormatObj);
        this.recordThat(new UserHasJoinedEvent(hoaId, netId));
    }

    public boolean getBoardMember() {
        return isBoardMember;
    }

    public HoaId getHoaId() {
        return hoaId;
    }

    public int getId() {
        return id;
    }

    public void setBoardMember(boolean boardMember) {
        isBoardMember = boardMember;
    }

    public String getNetId() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberAppUser that = (MemberAppUser) o;
        return id == that.id && username.equals(that.username) && hoaId.equals(that.hoaId);
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isBoardMember, hoaId);
    }

    @Override
    public String toString() {
        return "MemberAppUser " + username;
    }
}
