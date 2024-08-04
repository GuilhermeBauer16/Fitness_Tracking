package com.github.GuilhermeBauer16.FitnessTracking.service;

import com.github.GuilhermeBauer16.FitnessTracking.exception.EmailAlreadyRegisterException;
import com.github.GuilhermeBauer16.FitnessTracking.exception.FieldNotFound;
import com.github.GuilhermeBauer16.FitnessTracking.exception.UserNotFoundException;
import com.github.GuilhermeBauer16.FitnessTracking.factory.UserFactory;
import com.github.GuilhermeBauer16.FitnessTracking.mapper.Mapper;
import com.github.GuilhermeBauer16.FitnessTracking.model.UserEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.repository.UserRepository;
import com.github.GuilhermeBauer16.FitnessTracking.response.UserResponse;
import com.github.GuilhermeBauer16.FitnessTracking.service.contract.UserRegistrationContract;
import com.github.GuilhermeBauer16.FitnessTracking.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRegisterService implements UserDetailsService, UserRegistrationContract {

    private static final String USER_NOT_FOUND = "the user can't be found";
    private static final String EMAIL_NOT_FOUND = "a user with that email can't be found!";
    private static final String EMAIL_ALREADY_REGISTER = "That email address is already registered";

    private final Mapper<UserEntity, UserResponse> parseUserEntityToUserVO = new Mapper<>(UserEntity.class, UserResponse.class);

    private final UserRepository repository;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegisterService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserResponse create(UserVO userVO) {

        Optional<UserEntity> userByEmail = repository.findUserByEmail(userVO.getEmail());
        if(userByEmail.isPresent()){
            throw new EmailAlreadyRegisterException(EMAIL_ALREADY_REGISTER);
        }
        ValidatorUtils.checkObjectIsNullOrThrowException(userVO,USER_NOT_FOUND, UserNotFoundException.class);
        userVO.setPassword(passwordEncoder.encode(userVO.getPassword()));

        UserEntity userEntity = UserFactory.create(userVO.getName(), userVO.getEmail(), userVO.getPassword(), userVO.getUserProfile());
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(userEntity,USER_NOT_FOUND, FieldNotFound.class);
        UserEntity savedUser = repository.save(userEntity);

        return parseUserEntityToUserVO.parseObject(savedUser);

    }


    @Override
    public UserResponse findUserByEmail(String email) {
        UserEntity userEntity = repository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(EMAIL_NOT_FOUND));
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(userEntity, USER_NOT_FOUND, FieldNotFound.class);

        return parseUserEntityToUserVO.parseObject(userEntity);
    }

    @Override
    public void delete(String email) {
        UserEntity userEntity = repository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(EMAIL_NOT_FOUND));
        repository.delete(userEntity);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            return repository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(EMAIL_NOT_FOUND));
        } catch (RuntimeException ignore) {

            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

    }
}
