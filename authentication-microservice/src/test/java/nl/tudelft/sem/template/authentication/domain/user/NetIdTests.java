package nl.tudelft.sem.template.authentication.domain.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class NetIdTests {
    
    @Test
    void testConstructorAllowed() {
        String s = "AbcC123";
        NetId netId = new NetId(s);
        assertEquals(s, netId.toString());
    }

    @Test
    void testConstructorBracesThrowsException() {
        assertThrows(UsernameNotAllowedException.class, () -> new NetId("{}[]()"));
    }

    @Test
    void testConstructorOtherCharsThrowsException() {
        assertThrows(UsernameNotAllowedException.class, () -> new NetId("._-nd"));
    }

    @Test
    void testConstructorCombiThrowsException() {
        assertThrows(UsernameNotAllowedException.class, () -> new NetId("Abc12-=_"));
    }

    @Test
    void testInputTooShortThrowsException() {
        assertThrows(UsernameNotAllowedException.class, () -> new NetId("abc"));
    }
}
