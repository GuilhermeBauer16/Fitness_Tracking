package com.github.GuilhermeBauer16.FitnessTracking.service.contract;

import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.response.UserResponse;

public interface UserRegistrationContract {

    UserResponse create(UserVO userVO);

    UserResponse findUserByEmail(String email);

    void delete(String email);

}
