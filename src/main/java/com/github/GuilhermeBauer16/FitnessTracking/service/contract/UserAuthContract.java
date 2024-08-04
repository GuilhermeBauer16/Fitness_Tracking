package com.github.GuilhermeBauer16.FitnessTracking.service.contract;

import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import com.github.GuilhermeBauer16.FitnessTracking.request.LoginRequest;

public interface UserAuthContract {

    TokenVO login(LoginRequest loginRequest);

}
