package nl.tudelft.sem.hoa.domain.proposals.exceptions;


import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;

public class NotBoardMemberException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public NotBoardMemberException(MemberAppUser memberAppUser) {
        super(memberAppUser.toString());
    }
}
