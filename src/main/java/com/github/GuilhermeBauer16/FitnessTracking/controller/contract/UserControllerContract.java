package com.github.GuilhermeBauer16.FitnessTracking.controller.contract;

import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.request.LoginRequest;
import com.github.GuilhermeBauer16.FitnessTracking.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerContract {

    @PostMapping("/signIn")
    ResponseEntity<UserResponse> create(@RequestBody UserVO userVO);
    @PostMapping("/login")
    ResponseEntity<TokenVO> login(@RequestBody LoginRequest loginRequest);
    @GetMapping(value = "/{email}")
    ResponseEntity<UserResponse> findUserByEmail(@PathVariable("email")String email);
    @DeleteMapping(value = "/{email}")
    ResponseEntity<Void> delete(@PathVariable("email")String email);

}
