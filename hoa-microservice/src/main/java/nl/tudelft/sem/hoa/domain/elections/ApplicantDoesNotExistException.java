package nl.tudelft.sem.hoa.domain.elections;

public class ApplicantDoesNotExistException extends Exception {
    static final long serialVersionUID = -3387546993124229948L;

    private String applicantId;

    public ApplicantDoesNotExistException(String applicantId)  {
        super(applicantId);
    }
}
