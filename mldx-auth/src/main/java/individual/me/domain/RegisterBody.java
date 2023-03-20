package individual.me.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

@Data
public class RegisterBody {


    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^(?:\\+?86)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[0-35-9]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[0-35-9]\\d{2}|6[2567]\\d{2}|4(?:(?:10|4[01])\\d{3}|[68]\\d{4}|[579]\\d{2}))\\d{6}$",message = "手机号格式有误",groups = Phone.class)
    private String phone;

    @NotEmpty(message = "邮箱不能为空",groups = Email.class)
    @javax.validation.constraints.Email(message = "邮箱格式不正确")
    private String email;
    @NotEmpty(message = "密码不能为空")
    private String password;

    public interface Email extends Default{};
    public interface Phone extends Default{};
}
