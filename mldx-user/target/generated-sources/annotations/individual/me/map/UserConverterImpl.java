package individual.me.map;

import individual.me.domain.User;
import individual.me.module.user.UserVo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-18T00:11:58+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserVo toVo(User user) {
        if ( user == null ) {
            return null;
        }

        UserVo userVo = new UserVo();

        userVo.setId( user.getId() );
        userVo.setUsername( user.getUsername() );
        userVo.setPhone( user.getPhone() );
        userVo.setEmail( user.getEmail() );
        userVo.setNickname( user.getNickname() );
        userVo.setGender( user.getGender() );
        userVo.setIcon( user.getIcon() );

        return userVo;
    }
}
