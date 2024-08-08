package com.github.GuilhermeBauer16.FitnessTracking.service;

import com.github.GuilhermeBauer16.FitnessTracking.enums.UserProfile;
import com.github.GuilhermeBauer16.FitnessTracking.exception.UserNotFoundException;
import com.github.GuilhermeBauer16.FitnessTracking.filters.JwtTokenProvider;
import com.github.GuilhermeBauer16.FitnessTracking.model.UserEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import com.github.GuilhermeBauer16.FitnessTracking.repository.UserRepository;
import com.github.GuilhermeBauer16.FitnessTracking.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @Mock
    private UserEntity userEntity;

    @Mock
    private TokenVO tokenVO;

    @Mock
    private LoginRequest loginRequest;

    @Mock
    private UserRepository repository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserAuthService service;


    private static final String USER_NOT_FOUND = "the user can't be found";
    private static final String EMAIL_NOT_FOUND = "a user with that email can't be found!";

    private static final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";
    private static final String USER_NAME = "Paul";
    private static final String EMAIL = "Paultest@gmail.com";
    private static final String PASSWORD = "123456";
    private static final UserProfile USER_PROFILE = UserProfile.ADMIN;

    private static final boolean AUTHENTICATED = true;
    private static final Date CREATED = Date.from(Instant.parse("2024-08-08T20:10:35.504Z"));
    private static final Date EXPIRATION = Date.from(Instant.parse("2024-08-08T21:10:35.504Z"));

    private static final String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huVGV" +
            "zdEBob3RtYWlsLmNvbSIsInJvbGVzIjoiQURNSU4iLCJpc3MiOiJodHRwOi8vbG9jYW" +
            "xob3N0OjgwODAiLCJleHAiOjE3MjMxNTE0MzUsImlhdCI6MTcyMzE0Nzgz" +
            "NX0.OrHLYkrFAHI0Uy-gzxqdrYY2dLUOg1M2-kQsZzzERAU";

    private static final String REFRESH_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huVGVzdEB" +
            "ob3RtYWlsLmNvbSIsInJvbGVzIjoiQURNSU4iLCJleHAiOjE3MjMxNTg2MzUsImlhdCI6MTcyMzE0NzgzNX0.ohlj_fAlJ_" +
            "bwWgMQMyp9XxinMsKJES8Ysm6rCjnUCX0";





    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest(EMAIL, PASSWORD);
        userEntity = new UserEntity(ID, USER_NAME, EMAIL, PASSWORD, USER_PROFILE);
        tokenVO = new TokenVO(EMAIL,AUTHENTICATED,CREATED,EXPIRATION,ACCESS_TOKEN,REFRESH_TOKEN);
    }

    @Test
    void testLogin_WhenIsSuccessfulCreated_ShouldReturnTokenVOObject() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(repository.findUserByEmail(EMAIL)).thenReturn(Optional.of(userEntity));
        when(jwtTokenProvider.createAccessToken(EMAIL, USER_PROFILE)).thenReturn(tokenVO);

        TokenVO token = service.login(loginRequest);

        verify(repository, times(1)).findUserByEmail(anyString());

        assertNotNull(token);
        assertEquals(EMAIL, token.getUsername());
        assertEquals(AUTHENTICATED, token.getAuthenticated());
        assertEquals(ACCESS_TOKEN, token.getAccessToken());
        assertEquals(REFRESH_TOKEN, token.getRefreshToken());
        assertNotNull(token.getCreated());
        assertNotNull(token.getExpiration());
        assertEquals(CREATED, token.getCreated());
        assertEquals(EXPIRATION, token.getExpiration());

    }


    @Test
    void testLogin_InvalidCredentials_ShouldThrowUserNotFoundException() {


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("") {
                });


        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            service.login(loginRequest);
        });
        assertNotNull(exception);
        assertEquals(UserNotFoundException.ERROR.formatErrorMessage(USER_NOT_FOUND), exception.getMessage());
    }

    @Test
    void testLogin_UserNotFound_ShouldThrowUserNotFoundException() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(repository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());


        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            service.login(loginRequest);
        });
        assertNotNull(exception);
        assertEquals(UserNotFoundException.ERROR.formatErrorMessage(USER_NOT_FOUND), exception.getMessage());
    }



}
