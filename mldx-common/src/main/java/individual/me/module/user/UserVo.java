package individual.me.module.user;

import lombok.Data;

@Data
public class UserVo {

    private Long id;
    private String username;
    private String phone;
    private String email;
    private String nickname;
    private String gender;
    private String icon;
}
