package individual.me.repository;

import individual.me.domain.RegisterBody;
import individual.me.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User loadUserByUsername(@Param("username") String username);

    /**
     * 使用手机号注册
     * @param user 注册信息
     */
    void registerViaPhone(RegisterBody user);

    /**
     * 使用邮箱注册
     * @param user 注册信息
     */
    void registerViaEmail(RegisterBody user);

    /**
     * 更新用户信息
     * @param user 用户信息
     */
    void updateUserInfo(User user);
}
