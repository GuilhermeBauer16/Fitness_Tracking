package com.github.GuilhermeBauer16.FitnessTracking.repository;

import com.github.GuilhermeBauer16.FitnessTracking.enums.UserProfile;
import com.github.GuilhermeBauer16.FitnessTracking.model.UserEntity;
import integrationtests.testContainers.AbstractionIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends AbstractionIntegrationTest {

    @Autowired
    private UserRepository repository;

    UserEntity userEntity;

    UserEntity savedUserEntity;

    private static final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";
    private static final String USER_NAME = "Paul";
    private static final String EMAIL = "Paultest@gmail.com";
    private static final String PASSWORD = "123456";
    private static final UserProfile USER_PROFILE = UserProfile.ADMIN;

    @BeforeEach
    void setUp(){

        userEntity = new UserEntity(ID,USER_NAME,EMAIL,PASSWORD,USER_PROFILE);
        savedUserEntity = repository.save(userEntity);
    }

    @Test
    void testFindUserByEmail_WhenIsSuccessful_ThenReturnUserEntityObject(){

        UserEntity userByEmail = repository.findUserByEmail(savedUserEntity.getEmail()).get();

        assertNotNull(userByEmail);
        assertEquals(ID, userByEmail.getId());
        assertEquals(USER_NAME, userByEmail.getUsername());
        assertEquals(EMAIL, userByEmail.getEmail());
        assertEquals(PASSWORD, userByEmail.getPassword());
        assertEquals(USER_PROFILE, userByEmail.getUserProfile());


    }

}
