package individual.me.service;

import individual.me.domain.LoginUser;
import individual.me.domain.R;

public interface UserService {

    R login(LoginUser loginUser);

    R sendCode();
}
