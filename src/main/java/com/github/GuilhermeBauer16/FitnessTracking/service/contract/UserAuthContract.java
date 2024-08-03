package com.github.GuilhermeBauer16.FitnessTracking.service.contract;

import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;

public interface UserAuthContract {

    TokenVO login(UserVO userVO);

}
