package nl.tudelft.sem.template.authentication.domain.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsernameNotAllowedException extends UsernameNotFoundException {
    static final long serialVersionUID = -3387516993124229948L;

    public UsernameNotAllowedException(String msg) {
        super(msg);
    }

    public UsernameNotAllowedException(String msg, Throwable t) {
        super(msg, t);
    }

    @Override
    public String getMessage() {
        return "Username may only contain latin characters and numbers, and must be at least 4 characters long.";
    }
}
