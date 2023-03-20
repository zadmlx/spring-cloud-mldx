package individual.me.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.io.Serializable;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {


    // 更新用户信息的时候，必须携带用户的id，
    @NotEmpty(message = "用户信息异常",groups = Update.class)
    private Long id;
    private String username;
    private String password;
    @Pattern(regexp = "^(?:\\+?86)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[0-35-9]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[0-35-9]\\d{2}|6[2567]\\d{2}|4(?:(?:10|4[01])\\d{3}|[68]\\d{4}|[579]\\d{2}))\\d{6}$",message = "手机号格式有误")
    private String phone;
    @Email(message = "邮箱格式不正确")
    private String email;
    private String nickname;
    private String gender;
    private String level;
    private String icon;

    private interface Update extends Default{}

}
