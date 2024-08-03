package com.github.GuilhermeBauer16.FitnessTracking.controller;

import com.github.GuilhermeBauer16.FitnessTracking.controller.contract.UserControllerContract;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController implements UserControllerContract {

    @Autowired
    private UserRegisterService userRegisterService;

    @Override
    public ResponseEntity<UserVO> create(UserVO userVO) {
        UserVO createdUser = userRegisterService.create(userVO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserVO> login(UserVO userVO) {
        return null;
    }

    @Override
    public ResponseEntity<UserVO> findUserByEmail(String email) {
        UserVO userByEmail = userRegisterService.findUserByEmail(email);
        return ResponseEntity.ok(userByEmail );
    }

    @Override
    public ResponseEntity<Void> delete(String email) {
        return null;
    }
}
