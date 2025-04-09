package nl.tudelft.sem.hoa.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import nl.tudelft.sem.hoa.authentication.AuthManager;
import nl.tudelft.sem.hoa.authentication.JwtTokenVerifier;
import nl.tudelft.sem.hoa.domain.CreationDate;
import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import nl.tudelft.sem.hoa.domain.hoa.RuleHoa;
import nl.tudelft.sem.hoa.domain.hoa.RuleRepository;
import nl.tudelft.sem.hoa.domain.proposals.Proposal;
import nl.tudelft.sem.hoa.domain.proposals.ProposalPk;
import nl.tudelft.sem.hoa.domain.proposals.ProposalRepository;
import nl.tudelft.sem.hoa.domain.proposals.ProposalType;
import nl.tudelft.sem.hoa.domain.proposals.RuleProposal;
import nl.tudelft.sem.hoa.integration.utils.JsonUtil;
import nl.tudelft.sem.hoa.models.ProposalModel;
import nl.tudelft.sem.hoa.models.ProposalVoteModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@SuppressWarnings({"PMD"})
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager",
    "mockHoaRepository", "mockMembersRepository", "mockRuleRepository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProposalsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient HoaRepository mockHoaRepository;

    @Autowired
    private transient MembersRepository mockMembersRepository;

    @Autowired
    private transient ProposalRepository proposalRepository;

    @Autowired
    private transient RuleRepository mockRuleRepository;

    private final String netId = "netID";
    private final String proposalId = "propsalID";
    private final String hoaIdString = "hoaId";
    private final HoaId hoaId = new HoaId(hoaIdString);

    @Test
    public void create_proposal_works() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(true);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        // Set up request model
        ProposalModel proposalModel = new ProposalModel();
        proposalModel.setProposalId(proposalId);
        proposalModel.setHoaId(hoaIdString);
        proposalModel.setProposal("This is a proposal.");
        proposalModel.setProposalType("GENERAL");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create-proposal")
                .header("Authorization", "Bearer SomeToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(proposalModel)));

        // Assert
        resultActions
            .andExpect(status().isOk());

        Proposal savedProposal = proposalRepository.findProposalByProposalPk(new ProposalPk(proposalId, hoaId))
            .orElseThrow();

        Proposal expectedProposal = new Proposal(new ProposalPk(proposalId, hoaId), "This is a proposal.",
            ProposalType.GENERAL, 0, 0, 0, new ArrayList<>());

        assertThat(savedProposal)
            .isEqualTo(expectedProposal);
    }

    @Test
    public void create_rule_proposal_works() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(true);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        // Set up rules
        RuleHoa rule = new RuleHoa(1, hoaIdString, "Some rule");
        when(mockRuleRepository.findRulesByIdAndHoaId(1, hoaIdString)).thenReturn(new ArrayList<>(Arrays.asList(rule)));

        // Set up request model
        ProposalModel proposalModel = new ProposalModel();
        proposalModel.setProposalId(proposalId);
        proposalModel.setHoaId(hoaIdString);
        proposalModel.setProposal("This is a rule proposal.");
        proposalModel.setProposalType("RULE");
        proposalModel.setRuleId(1);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(proposalModel)));

        // Assert
        resultActions
            .andExpect(status().isOk());

        RuleProposal savedProposal =
            (RuleProposal) proposalRepository.findProposalByProposalPk(new ProposalPk(proposalId, hoaId)).orElseThrow();

        RuleProposal expectedProposal = new RuleProposal(new ProposalPk(proposalId, hoaId),
            "This is a proposal.", ProposalType.GENERAL, 0, 0, 0, new ArrayList<>(), 1);

        assertThat(savedProposal)
            .isEqualTo(expectedProposal);
    }

    @Test
    public void create_proposal_throws_404_hoa_id_not_found() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(false);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(true);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        // Set up rules
        when(mockRuleRepository.existsById(1)).thenReturn(true);

        // Set up request model
        ProposalModel proposalModel = new ProposalModel();
        proposalModel.setProposalId(proposalId);
        proposalModel.setHoaId(hoaIdString);
        proposalModel.setProposal("This is a rule proposal.");
        proposalModel.setProposalType("RULE");
        proposalModel.setRuleId(1);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(proposalModel)));

        // Assert
        resultActions
            .andExpect(status().isNotFound())
            .andExpect(status().reason(containsString("Hoa ID does not exist.")));
    }

    @Test
    public void create_proposal_throws_401_not_part_of_hoa() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        HoaId wrongHoaId = new HoaId("banan");
        when(mockHoaRepository.existsByHoaId(wrongHoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(wrongHoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(true);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        // Set up rules
        when(mockRuleRepository.existsById(1)).thenReturn(true);

        // Set up request model
        ProposalModel proposalModel = new ProposalModel();
        proposalModel.setProposalId(proposalId);
        proposalModel.setHoaId(hoaIdString);
        proposalModel.setProposal("This is a rule proposal.");
        proposalModel.setProposalType("RULE");
        proposalModel.setRuleId(1);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(proposalModel)));

        // Assert
        resultActions
            .andExpect(status().isUnauthorized())
            .andExpect(status().reason(containsString("User is not part of HOA.")));
    }

    @Test
    public void create_proposal_throws_401_not_board_member() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(false);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        // Set up rules
        when(mockRuleRepository.existsById(1)).thenReturn(true);

        // Set up request model
        ProposalModel proposalModel = new ProposalModel();
        proposalModel.setProposalId(proposalId);
        proposalModel.setHoaId(hoaIdString);
        proposalModel.setProposal("This is a rule proposal.");
        proposalModel.setProposalType("RULE");
        proposalModel.setRuleId(1);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(proposalModel)));

        // Assert
        resultActions
            .andExpect(status().isUnauthorized())
            .andExpect(status().reason(containsString("User is not a board member.")));
    }

    @Test
    public void create_proposal_throws_400_bad_enum() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(true);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        // Set up rules
        when(mockRuleRepository.existsById(1)).thenReturn(true);

        // Set up request model
        ProposalModel proposalModel = new ProposalModel();
        proposalModel.setProposalId(proposalId);
        proposalModel.setHoaId(hoaIdString);
        proposalModel.setProposal("This is a wrong proposal.");
        proposalModel.setProposalType("GENERIC");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(proposalModel)));

        // Assert
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(status().reason(containsString("Proposal type does not exist.")));
    }

    @Test
    public void create_proposal_throws_400_proposal_id_already_exists() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(true);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        // Set up rules
        when(mockRuleRepository.existsById(1)).thenReturn(true);

        // Set up proposal already in use
        ProposalPk proposalPk = new ProposalPk(proposalId, hoaId);
        proposalRepository.save(new Proposal(proposalPk, "This is a proposal.", ProposalType.GENERAL, new ArrayList<>()));

        // Set up request model
        ProposalModel proposalModel = new ProposalModel();
        proposalModel.setProposalId(proposalId);
        proposalModel.setHoaId(hoaIdString);
        proposalModel.setProposal("This is a proposal.");
        proposalModel.setProposalType("GENERAL");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/create-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(proposalModel)));

        // Assert
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(status().reason(containsString("Proposal ID already exists.")));
    }

    @Test
    public void vote_proposal_works() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(true);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        ProposalPk proposalPk = new ProposalPk(proposalId, hoaId);
        Proposal proposal = new Proposal(proposalPk, "This is a proposal.",
                ProposalType.GENERAL, new CreationDate(LocalDate.now().minusWeeks(2).minusDays(1)), new ArrayList<>());

        // Set up proposal repository
        proposalRepository.save(proposal);

        // Set up model
        ProposalVoteModel model = new ProposalVoteModel();
        model.setProposalId(proposalId);
        model.setHoaId(hoaIdString);
        model.setDecision("ACCEPT");

        ResultActions resultActions = mockMvc.perform(post("/vote-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(model)));

        resultActions
            .andExpect(status().isOk());

        Proposal savedProposal = proposalRepository.findProposalByProposalPk(proposalPk).orElseThrow();

        int[] expected = {1, 0, 0};
        assertThat(savedProposal.getVotes())
            .isEqualTo(expected);
    }

    @Test
    public void vote_proposal_throws_400_too_young() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(true);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        ProposalPk proposalPk = new ProposalPk(proposalId, hoaId);
        Proposal proposal = new Proposal(proposalPk, "This is a proposal.",
                ProposalType.GENERAL, new CreationDate(LocalDate.now()), new ArrayList<>());

        // Set up proposal repository
        proposalRepository.save(proposal);

        // Set up model
        ProposalVoteModel model = new ProposalVoteModel();
        model.setProposalId(proposalId);
        model.setHoaId(hoaIdString);
        model.setDecision("ACCEPT");

        ResultActions resultActions = mockMvc.perform(post("/vote-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(model)));

        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(status().reason(containsString("Proposal is not opened (Too young or too old).")));
    }

    @Test
    public void vote_proposal_throws_400_too_old() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(true);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        ProposalPk proposalPk = new ProposalPk(proposalId, hoaId);
        Proposal proposal = new Proposal(proposalPk, "This is a proposal.",
                ProposalType.GENERAL, new CreationDate(LocalDate.now().minusDays(4).minusWeeks(2)), new ArrayList<>());

        // Set up proposal repository
        proposalRepository.save(proposal);

        // Set up model
        ProposalVoteModel model = new ProposalVoteModel();
        model.setProposalId(proposalId);
        model.setHoaId(hoaIdString);
        model.setDecision("ACCEPT");

        ResultActions resultActions = mockMvc.perform(post("/vote-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(model)));

        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(status().reason(containsString("Proposal is not opened (Too young or too old).")));
    }

    @Test
    public void vote_proposal_throws_401_not_part_of_hoa() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        HoaId wrongHoaId = new HoaId("banan");
        when(mockHoaRepository.existsByHoaId(wrongHoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(wrongHoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(true);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        ProposalPk proposalPk = new ProposalPk(proposalId, hoaId);
        Proposal proposal = new Proposal(proposalPk, "This is a proposal.",
                ProposalType.GENERAL, new CreationDate(LocalDate.now().minusDays(1).minusWeeks(2)), new ArrayList<>());

        // Set up proposal repository
        proposalRepository.save(proposal);

        // Set up model
        ProposalVoteModel model = new ProposalVoteModel();
        model.setProposalId(proposalId);
        model.setHoaId(hoaIdString);
        model.setDecision("ACCEPT");

        ResultActions resultActions = mockMvc.perform(post("/vote-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(model)));

        resultActions
            .andExpect(status().isUnauthorized())
            .andExpect(status().reason(containsString("User is not part of HOA.")));
    }

    @Test
    public void vote_proposal_throws_401_not_board_member() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(false);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        ProposalPk proposalPk = new ProposalPk(proposalId, hoaId);
        Proposal proposal = new Proposal(proposalPk, "This is a proposal.",
                ProposalType.GENERAL, new CreationDate(LocalDate.now().minusDays(1).minusWeeks(2)), new ArrayList<>());

        // Set up proposal repository
        proposalRepository.save(proposal);

        // Set up model
        ProposalVoteModel model = new ProposalVoteModel();
        model.setProposalId(proposalId);
        model.setHoaId(hoaIdString);
        model.setDecision("ACCEPT");

        ResultActions resultActions = mockMvc.perform(post("/vote-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(model)));

        resultActions
            .andExpect(status().isUnauthorized())
            .andExpect(status().reason(containsString("User is not a board member.")));
    }

    @Test
    public void vote_proposal_throws_404_proposal_not_found() throws Exception {
        // Set up authentication
        when(mockAuthenticationManager.getNetId()).thenReturn(netId);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn(netId);

        // Set up HOA and membership
        when(mockHoaRepository.existsByHoaId(hoaId)).thenReturn(true);
        MemberAppUser memberAppUser = new MemberAppUser(netId, new Hoa(hoaId, "country", "city"),
            new Address("country", "city", "str", 12, "postal"));
        memberAppUser.setBoardMember(false);
        when(mockMembersRepository.findMemberAppUsersByUsername(netId)).thenReturn(java.util.Optional.of(memberAppUser));

        // Set up model
        ProposalVoteModel model = new ProposalVoteModel();
        model.setProposalId(proposalId);
        model.setHoaId(hoaIdString);
        model.setDecision("ACCEPT");

        ResultActions resultActions = mockMvc.perform(post("/vote-proposal")
            .header("Authorization", "Bearer SomeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(model)));

        resultActions
            .andExpect(status().isNotFound())
            .andExpect(status().reason(containsString("Proposal ID not found.")));
    }
}
