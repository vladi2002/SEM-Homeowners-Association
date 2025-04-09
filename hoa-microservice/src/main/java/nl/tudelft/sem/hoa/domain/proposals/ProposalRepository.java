package nl.tudelft.sem.hoa.domain.proposals;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for proposals.
 */
@Repository
public interface ProposalRepository extends JpaRepository<Proposal, ProposalPk> {

    Optional<Proposal> findProposalByProposalPk(ProposalPk proposalPk);

    boolean existsByProposalPk(ProposalPk proposalPk);
}
