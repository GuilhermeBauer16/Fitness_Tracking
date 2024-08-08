package com.github.GuilhermeBauer16.FitnessTracking.service;

import com.github.GuilhermeBauer16.FitnessTracking.enums.UserProfile;
import com.github.GuilhermeBauer16.FitnessTracking.exception.EmailAlreadyRegisterException;
import com.github.GuilhermeBauer16.FitnessTracking.exception.UserNotFoundException;
import com.github.GuilhermeBauer16.FitnessTracking.model.UserEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.repository.UserRepository;
import com.github.GuilhermeBauer16.FitnessTracking.response.UserResponse;
import com.github.GuilhermeBauer16.FitnessTracking.utils.ValidatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRegisterServiceTest {

    @Mock
    private UserEntity userEntity;

    @Mock
    private UserVO userVO;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserRegisterService service;

    @Mock
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL_ALREADY_REGISTER = "That email address is already registered";
    private static final String USER_NOT_FOUND = "the user can't be found";
    private static final String EMAIL_NOT_FOUND = "a user with that email can't be found!";

    private static final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";
    private static final String USER_NAME = "Paul";
    private static final String EMAIL = "Paultest@gmail.com";
    private static final String PASSWORD = "123456";
    private static final UserProfile USER_PROFILE = UserProfile.ADMIN;


    @BeforeEach
    void setUp() {
        userVO = new UserVO(ID, USER_NAME, EMAIL, PASSWORD, USER_PROFILE);
        userEntity = new UserEntity(ID, USER_NAME, EMAIL, PASSWORD, USER_PROFILE);
    }

    @Test
    void testCreateUser_WhenIsSuccessfulCreated_ShouldReturnUserResponseObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {
            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any(Class.class))).thenAnswer(invocation -> null);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any(Class.class))).thenAnswer(invocation -> null);

            when(repository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
            when(repository.save(any(UserEntity.class))).thenReturn(userEntity);

            UserResponse userResponse = service.create(userVO);

            verify(repository, times(1)).findUserByEmail(anyString());
            verify(repository, times(1)).save(any(UserEntity.class));

            assertNotNull(userResponse);
            assertEquals(USER_NAME, userResponse.getName());
            assertEquals(EMAIL, userResponse.getEmail());

        }
    }

    @Test
    void testCreate_WhenEmailIsAlreadyRegisterInTheDatabase_ShouldThrowEmailAlreadyRegisterExceptionException() {

        when(repository.findUserByEmail(EMAIL)).thenReturn(Optional.of(new UserEntity()));
        EmailAlreadyRegisterException exception =
                assertThrows(EmailAlreadyRegisterException.class, () -> service.create(userVO));

        assertNotNull(exception);
        assertEquals(exception.getMessage(), EmailAlreadyRegisterException.ERROR.formatErrorMessage(EMAIL_ALREADY_REGISTER));

    }

    @Test
    void testFindByEmail_WhenTheUserIsFindByEmail_ShouldReturnUserResponseObject() {
        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any(Class.class))).thenAnswer(invocation -> null);

            when(repository.findUserByEmail(EMAIL)).thenReturn(Optional.of(userEntity));
            UserResponse userByEmail = service.findUserByEmail(EMAIL);

            verify(repository, times(1)).findUserByEmail(anyString());

            assertNotNull(userByEmail);
            assertEquals(USER_NAME, userByEmail.getName());
            assertEquals(EMAIL, userByEmail.getEmail());

        }
    }

    @Test
    void testFindByEmail_WhenEmailIsNotRegisterInTheDatabase_ShouldThrowUserNotFoundExceptionException() {

        UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, () -> service.findUserByEmail(EMAIL));

        assertNotNull(exception);
        assertEquals(exception.getMessage(), UserNotFoundException.ERROR.formatErrorMessage(EMAIL_NOT_FOUND));

    }

    @Test
    void testFindByEmail_WhenEmailIsNull_ShouldThrowUserNotFoundExceptionException() {

        UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, () -> service.findUserByEmail(null));

        assertNotNull(exception);
        assertEquals(exception.getMessage(), UserNotFoundException.ERROR.formatErrorMessage(EMAIL_NOT_FOUND));

    }

    @Test
    void testDelete_WhenDeletingUser_thenDoNothing(){

        when(repository.findUserByEmail(EMAIL)).thenReturn(Optional.of(userEntity));
        doNothing().when(repository).delete(userEntity);
        service.delete(EMAIL);
        verify(repository, times(1)).delete(any(UserEntity.class));
    }

    @Test
    void testDelete_WhenEmailIsNotRegisterInTheDatabase_ShouldThrowUserNotFoundExceptionException() {

        UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, () -> service.delete(EMAIL));

        assertNotNull(exception);
        assertEquals(exception.getMessage(), UserNotFoundException.ERROR.formatErrorMessage(EMAIL_NOT_FOUND));

    }

    @Test
    void testDelete_WhenEmailIsNull_ShouldThrowUserNotFoundExceptionException() {

        UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, () -> service.delete(null));

        assertNotNull(exception);
        assertEquals(exception.getMessage(), UserNotFoundException.ERROR.formatErrorMessage(EMAIL_NOT_FOUND));

    }

    @Test
    void testLoadUserByUsername_WhenUserIsRecoveredSuccessful_ShouldReturnUserDetails(){

        when(repository.findUserByEmail(EMAIL)).thenReturn(Optional.of(userEntity));
        UserDetails userDetails = service.loadUserByUsername(EMAIL);
        assertNotNull(userDetails);
        assertEquals(USER_NAME,userDetails.getUsername());
        assertEquals(PASSWORD,userDetails.getPassword());

    }

    @Test
    void testLoadUserByUsername_WhenEmailIsNotRegisterInTheDatabase_ShouldThrowUserNotFoundExceptionException() {

        when(repository.findUserByEmail(EMAIL)).thenThrow(new UserNotFoundException(EMAIL_NOT_FOUND));
        UsernameNotFoundException exception =
                assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(EMAIL));

        assertNotNull(exception);
        assertEquals(USER_NOT_FOUND, exception.getMessage());

    }

    @Test
    void testLoadUserByUsername_WhenEmailIsNull_ShouldThrowUsernameNotFoundExceptionException() {

        UsernameNotFoundException exception =
                assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(null));

        assertNotNull(exception);
        assertEquals(USER_NOT_FOUND, exception.getMessage());

    }


}
