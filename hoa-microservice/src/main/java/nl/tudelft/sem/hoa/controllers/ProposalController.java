package nl.tudelft.sem.hoa.controllers;

import java.util.Locale;
import javassist.NotFoundException;
import nl.tudelft.sem.hoa.authentication.AuthManager;
import nl.tudelft.sem.hoa.domain.HoaIdNotFoundException;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.proposals.Proposal;
import nl.tudelft.sem.hoa.domain.proposals.ProposalPk;
import nl.tudelft.sem.hoa.domain.proposals.ProposalService;
import nl.tudelft.sem.hoa.domain.proposals.ProposalType;
import nl.tudelft.sem.hoa.domain.proposals.RuleProposal;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.NotBoardMemberException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.NotPartOfHoaException;
import nl.tudelft.sem.hoa.domain.vote.Decision;
import nl.tudelft.sem.hoa.domain.vote.ProposalVote;
import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import nl.tudelft.sem.hoa.models.ProposalModel;
import nl.tudelft.sem.hoa.models.ProposalResponseModel;
import nl.tudelft.sem.hoa.models.ProposalVoteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class ProposalController {
    private final transient AuthManager authManager;
    private final transient ProposalService proposalService;

    @Autowired
    public ProposalController(AuthManager authManager, ProposalService proposalService) {
        this.authManager = authManager;
        this.proposalService = proposalService;
    }

    /**
     * Post endpoint to create a new proposal.
     *
     * @param model The proposal in JSON form
     * @return A 200 OK status if creation worked
     */
    @PostMapping("/create-proposal")
    public ResponseEntity createProposal(@RequestBody ProposalModel model) {
        if (authManager.getNetId() == null) {
            return ResponseEntity.badRequest().build();
        }

        ProposalPk proposalPk = new ProposalPk(model.getProposalId(), new HoaId(model.getHoaId()));
        try {
            ProposalType proposalType = ProposalType.valueOf(model.getProposalType().toUpperCase(Locale.ENGLISH));
            proposalService.createProposal(proposalPk, model.getProposal(),
                    proposalType, model.getRuleId(), authManager.getNetId());
        } catch (HoaIdNotFoundException e) {
            throwNotFound("Hoa ID does not exist.", e);
        } catch (NotPartOfHoaException e) {
            throwUnauthorized("User is not part of HOA.", e);
        } catch (NotBoardMemberException e) {
            throwUnauthorized("User is not a board member.", e);
        } catch (IllegalArgumentException e) {
            throwBadRequest("Proposal type does not exist.", e);
        } catch (NotFoundException e) {
            throwNotFound(null, e);
        } catch (Exception e) {
            throwBadRequest(null, e);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Post endpoint for voting on a proposal.
     *
     * @param proposalVoteModel The vote in JSON form
     * @return A 200 OK if vote was added
     */
    @PostMapping("/vote-proposal")
    public ResponseEntity voteProposal(@RequestBody ProposalVoteModel proposalVoteModel) {
        String netId = authManager.getNetId();
        if (netId == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Decision decision = Decision.valueOf(proposalVoteModel.getDecision().toUpperCase(Locale.ENGLISH));
            ProposalVote vote = new ProposalVote(new TypelessVote(netId),
                    new ProposalPk(proposalVoteModel.getProposalId(), new HoaId(proposalVoteModel.getHoaId())), decision);
            proposalService.voteProposal(vote);
        } catch (HoaIdNotFoundException e) {
            throwNotFound("Hoa ID does not exist.", e);
        } catch (NotPartOfHoaException e) {
            throwUnauthorized("User is not part of HOA.", e);
        } catch (NotBoardMemberException e) {
            throwUnauthorized("User is not a board member.", e);
        } catch (NotFoundException e) {
            throwNotFound(null, e);
        } catch (Exception e) {
            throwBadRequest(null, e);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Get endpoint for getting a proposal.
     *
     * @param hoaId      The hoa ID
     * @param proposalId The proposal ID
     * @return A proposal object
     */
    @GetMapping("/get-proposal/{hoaId}/{proposalId}")
    public ResponseEntity<ProposalResponseModel> getProposal(@PathVariable String hoaId,
                                                             @PathVariable String proposalId) {
        ProposalPk id = new ProposalPk(proposalId, new HoaId(hoaId));
        try {
            Proposal proposal = proposalService.getProposal(id);
            ProposalResponseModel responseModel = new ProposalResponseModel(proposal);
            //            if (proposal.getProposalType() == ProposalType.GENERAL) {
            //                responseModel = new ProposalResponseModel(proposal.getProposalString(),
            //                        ProposalType.GENERAL, proposal.getAcceptVotes(), proposal.getRejectVotes(),
            //                        proposal.getAbstainVotes(), proposal.getDate());
            //            } else {
            //                RuleProposal ruleProposal = (RuleProposal) proposal;
            //                responseModel = new ProposalResponseModel(ruleProposal.getProposalString(),
            //                        ProposalType.RULE, ruleProposal.getAcceptVotes(), ruleProposal.getRejectVotes(),
            //                        ruleProposal.getAbstainVotes(), ruleProposal.getDate(), ruleProposal.getRuleId());
            //            }
            return ResponseEntity.ok(responseModel);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Helper method that throws unauthorized exception.
     *
     * @param message The error message, null if no special message needs to be provided
     * @throws ResponseStatusException if user is unauthorized
     */
    private void throwUnauthorized(String message, Exception e) throws ResponseStatusException {
        if (message == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message, e);
    }

    /**
     * Helper method that throws bad request exception.
     *
     * @param message The error message, null if no special message needs to be provided
     * @throws ResponseStatusException if request is a bad request
     */
    private void throwBadRequest(String message, Exception e) throws ResponseStatusException {
        if (message == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, e);
    }

    /**
     * Helper method that throws not found exception.
     *
     * @param message The error message, null if no special message needs to be provided
     * @throws ResponseStatusException if request is not found
     */
    private void throwNotFound(String message, Exception e) throws ResponseStatusException {
        if (message == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, message, e);
    }
}