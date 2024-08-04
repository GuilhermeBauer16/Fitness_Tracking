package com.github.GuilhermeBauer16.FitnessTracking.factory;

import com.github.GuilhermeBauer16.FitnessTracking.enums.UserProfile;
import com.github.GuilhermeBauer16.FitnessTracking.model.UserEntity;
import com.github.GuilhermeBauer16.FitnessTracking.utils.UuidUtils;

/**
 * That factory create an instance of the class {@link UserEntity}
 * <p>
 * That factory hide the business rules to create a User Entity,
 * include the creation of a unique UUID for each User Entity
 * </p>
 */


public class UserFactory {

    public UserFactory() {
    }


    public static UserEntity create( String name, String email, String password, UserProfile userProfile) {
        return new UserEntity(UuidUtils.generateUuid(), name, email, password, userProfile);
    }


}
