package com.github.GuilhermeBauer16.FitnessTracking.service;

import com.github.GuilhermeBauer16.FitnessTracking.factory.UserFactory;
import com.github.GuilhermeBauer16.FitnessTracking.mapper.Mapper;
import com.github.GuilhermeBauer16.FitnessTracking.model.UserEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.repository.UserRepository;
import com.github.GuilhermeBauer16.FitnessTracking.service.contract.UserRegistrationContract;
import com.github.GuilhermeBauer16.FitnessTracking.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterService implements UserDetailsService, UserRegistrationContract {

    private final Mapper<UserVO, UserEntity> parseUserVOToUserEntity = new Mapper<>(UserVO.class, UserEntity.class);

    private final Mapper<UserEntity, UserVO> parseUserEntityToUserVO = new Mapper<>(UserEntity.class, UserVO.class);


    private final UserRepository repository;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegisterService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserVO create(UserVO userVO) {

        ValidatorUtils.checkObjectIsNullOrThrowException(userVO,"not found", RuntimeException.class);
        userVO.setPassword(passwordEncoder.encode(userVO.getPassword()));

        UserEntity userEntity = UserFactory.create(userVO.getName(), userVO.getEmail(), userVO.getPassword(), userVO.getUserProfile());
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(userEntity,"not found", RuntimeException.class);
        UserEntity savedUser = repository.save(userEntity);
        return parseUserEntityToUserVO.parseObject(savedUser);

    }

    @Override
    public UserVO findUserByEmail(String email) {

        UserEntity userEntity = repository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("not found"));

        return parseUserEntityToUserVO.parseObject(userEntity);
    }

    @Override
    public void delete(String email) {

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            return repository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("not found"));
        } catch (RuntimeException ignore) {

            throw new UsernameNotFoundException("not found");
        }

    }
}
