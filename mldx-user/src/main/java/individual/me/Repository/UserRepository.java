package individual.me.Repository;

import individual.me.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {

    User loadUserById(@Param("userId") Long userId);

}
