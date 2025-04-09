package nl.tudelft.sem.hoa.domain.proposals;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaIdAttributeConverter;

@Embeddable
public class ProposalPk implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "proposal_id")
    private String proposalId;

    @Column(name = "hoa_id")
    @Convert(converter = HoaIdAttributeConverter.class)
    private HoaId hoaId;

    public ProposalPk() {
    }

    public ProposalPk(String proposalId, HoaId hoaId) {
        this.proposalId = proposalId;
        this.hoaId = hoaId;
    }

    public HoaId getHoaId() {
        return hoaId;
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setHoaId(HoaId hoaId) {
        this.hoaId = hoaId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    @Override
    public String toString() {
        return this.proposalId + " " + this.hoaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProposalPk that = (ProposalPk) o;
        return Objects.equals(proposalId, that.proposalId) && Objects.equals(hoaId, that.hoaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proposalId, hoaId);
    }
}
