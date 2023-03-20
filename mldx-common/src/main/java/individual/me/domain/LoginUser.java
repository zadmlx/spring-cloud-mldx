package individual.me.domain;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginUser {

    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "用户密码不能为空")
    private String password;
    private String uuid;
    @NotBlank(message = "验证码不能为空")
    private String code;
}
