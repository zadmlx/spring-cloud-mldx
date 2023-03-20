package individual.me.map;


import individual.me.domain.User;
import individual.me.module.user.UserVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConverter {

    UserVo toVo(User user);

}
