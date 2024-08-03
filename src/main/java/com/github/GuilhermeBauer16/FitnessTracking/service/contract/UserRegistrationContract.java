package com.github.GuilhermeBauer16.FitnessTracking.service.contract;

import com.github.GuilhermeBauer16.FitnessTracking.model.UserEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;

public interface UserRegistrationContract {

    UserVO create(UserVO userVO);

    UserVO findUserByEmail(String email);

    void delete(String email);

}
