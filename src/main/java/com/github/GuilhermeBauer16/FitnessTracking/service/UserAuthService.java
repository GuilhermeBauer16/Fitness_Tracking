package com.github.GuilhermeBauer16.FitnessTracking.service;

import com.github.GuilhermeBauer16.FitnessTracking.jwt.JwtTokenProvider;
import com.github.GuilhermeBauer16.FitnessTracking.model.UserEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.repository.UserRepository;
import com.github.GuilhermeBauer16.FitnessTracking.service.contract.UserAuthContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService implements UserAuthContract {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @SuppressWarnings("rawtypes")
    public TokenVO login(UserVO userVO) {

        try {

            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userVO.getEmail(), userVO.getPassword())
            );


            UserEntity userEntity = repository.findUserByEmail(userVO.getEmail()).orElseThrow(() -> new RuntimeException("not found that user"));
            return jwtTokenProvider.createAccessToken(userEntity.getEmail(),userEntity.getUserProfile());

        }catch (RuntimeException ignore){

            throw new RuntimeException("not found!");
        }

    }

//    @SuppressWarnings("rawtypes")
//    public ResponseEntity signin(AccountCredentialsVO data) {
//        try {
//
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//            var user = userRepository.findByUsername(username);
//            var tokenResponse = new TokenVO();
//            if (user != null) {
//                tokenResponse = jwtTokenProvider.createAccessToken(username, user.getRoles());
//            } else {
//                throw new UsernameNotFoundException("Username: " + username + " not found!");
//            }
//            System.out.println(user.getPassword());
//            return ResponseEntity.ok(tokenResponse);
//
//        } catch (Exception e) {
//            throw new BadCredentialsException("Invalid username/password supplied");
//        }
//    }
//
//    @SuppressWarnings("rawtypes")
//    public ResponseEntity refreshToken(String username, String refreshToken) {
//
//
//        var user = userRepository.findByUsername(username);
//        var tokenResponse = new TokenVO();
//        if (user != null) {
//            tokenResponse = jwtTokenProvider.createRefreshToken(refreshToken);
//        } else {
//            throw new UsernameNotFoundException("Username: " + username + " not found!");
//        }
//        return ResponseEntity.ok(tokenResponse);
//    }
}
