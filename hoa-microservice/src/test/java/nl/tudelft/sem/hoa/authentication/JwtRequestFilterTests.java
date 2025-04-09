package nl.tudelft.sem.hoa.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.Stream;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtRequestFilterTests {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private transient JwtRequestFilter jwtRequestFilter;

    private transient HttpServletRequest mockRequest;
    private transient HttpServletResponse mockResponse;
    private transient FilterChain mockFilterChain;

    private transient JwtTokenVerifier mockJwtTokenVerifier;

    /**
     * Set up mocks.
     */
    @BeforeEach
    public void setup() {
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockFilterChain = Mockito.mock(FilterChain.class);
        mockJwtTokenVerifier = Mockito.mock(JwtTokenVerifier.class);

        jwtRequestFilter = new JwtRequestFilter(mockJwtTokenVerifier);

        SecurityContextHolder.getContext().setAuthentication(null);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    /**
     * Clear the byte streams for output.
     *
     * @throws ServletException thrown at times
     * @throws IOException if incorrect input
     */
    @AfterEach
    public void assertChainContinues() throws ServletException, IOException {
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
        verifyNoMoreInteractions(mockFilterChain);
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void correctToken() throws ServletException, IOException {
        // Arrange
        String token = "randomtoken123";
        String user = "user123";
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(mockJwtTokenVerifier.validateToken(token)).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(token)).thenReturn(user);

        // Act
        jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName())
                .isEqualTo(user);
    }

    @Test
    public void invalidToken() throws ServletException, IOException {
        // Arrange
        String token = "randomtoken123";
        String user = "user123";
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(mockJwtTokenVerifier.validateToken(token)).thenReturn(false);
        when(mockJwtTokenVerifier.getNetIdFromToken(token)).thenReturn(user);

        // Act
        jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
    }

    /**
     * Parameterized test for various token verification exceptions.
     *
     * @param throwable the exception to be tested
     */
    @ParameterizedTest
    @MethodSource("tokenVerificationExceptionGenerator")
    public void tokenVerificationException(Class<? extends Throwable> throwable)
            throws ServletException, IOException {
        // Arrange
        String token = "randomtoken123";
        String user = "user123";
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(mockJwtTokenVerifier.validateToken(token)).thenThrow(throwable);
        when(mockJwtTokenVerifier.getNetIdFromToken(token)).thenReturn(user);
        // Act
        jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        if (throwable.getName().equals("io.jsonwebtoken.ExpiredJwtException")) {
            assertEquals("JWT token has expired.", errContent.toString().trim());
        }
        if (throwable.getName().equals("io.jsonwebtoken.JwtException")) {
            assertEquals("Unable to parse JWT token", errContent.toString().trim());
        }
        if (throwable.getName().equals("java.lang.IllegalArgumentException")) {
            assertEquals("Unable to parse JWT token", errContent.toString().trim());
        }
        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
    }

    private static Stream<Arguments> tokenVerificationExceptionGenerator() {
        return Stream.of(
                Arguments.of(ExpiredJwtException.class),
                Arguments.of(IllegalArgumentException.class),
                Arguments.of(JwtException.class)

        );
    }

    @Test
    public void nullToken() throws ServletException, IOException {
        // Arrange
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
    }

    @Test
    public void invalidPrefix() throws ServletException, IOException {
        // Arrange
        String token = "randomtoken123";
        String user = "user123";
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer1 " + token);
        when(mockJwtTokenVerifier.validateToken(token)).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(token)).thenReturn(user);

        // Act
        jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        assertEquals("Invalid authorization header", errContent.toString().trim());
        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
    }

    @Test
    public void noPrefix() throws ServletException, IOException {
        // Arrange
        String token = "randomtoken123";
        String user = "user123";
        when(mockRequest.getHeader("Authorization")).thenReturn(token);
        when(mockJwtTokenVerifier.validateToken(token)).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(token)).thenReturn(user);

        // Act
        jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        assertEquals("Invalid authorization header", errContent.toString().trim());
        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
    }
}
