package com.github.GuilhermeBauer16.FitnessTracking.service;

import com.github.GuilhermeBauer16.FitnessTracking.exception.UserNotFoundException;
import com.github.GuilhermeBauer16.FitnessTracking.filters.JwtTokenProvider;
import com.github.GuilhermeBauer16.FitnessTracking.model.UserEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.repository.UserRepository;
import com.github.GuilhermeBauer16.FitnessTracking.service.contract.UserAuthContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService implements UserAuthContract {

    private static final String EMAIL_NOT_FOUND = "a user with that email: %s can't be found!";
    private static final String USER_NOT_FOUND = "the user can't be found";

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;


    private final UserRepository repository;


    @Autowired
    public UserAuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository repository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.repository = repository;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public TokenVO login(UserVO userVO) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userVO.getEmail(), userVO.getPassword())
            );

            UserEntity userEntity = repository.findUserByEmail(userVO.getEmail()).orElseThrow(() -> new UserNotFoundException(EMAIL_NOT_FOUND));
            return jwtTokenProvider.createAccessToken(userEntity.getEmail(),userEntity.getUserProfile());

        }catch (RuntimeException ignore){

            throw new UserNotFoundException(USER_NOT_FOUND);
        }

    }



}
