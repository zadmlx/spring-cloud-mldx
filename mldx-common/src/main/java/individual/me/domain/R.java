package individual.me.domain;

import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class R implements Serializable {

    private Integer status;
    private String message;

    private Object data;

    private static final String OK = "success";
    private static final String FAIL = "fail";

    private static final Integer OKSTATUS = HttpStatus.OK.value();
    private static final Integer FAILSTATUS = HttpStatus.BAD_REQUEST.value();

    private R() {
    }

    private R(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static R ok(Integer status, String message, Object data){
        return new R(status, message, data);
    }

    public static R ok(){
        return new R(OKSTATUS,OK,null);
    }

    public static R ok(String message){
        return new R(OKSTATUS,message,null);
    }

    public static R ok(String message, Object data){
        return new R(OKSTATUS,message,data);
    }

    public static R ok(Object data){
        return new R(OKSTATUS,OK,data);
    }


    public static R fail(Integer status, String message){
        return new R(status,message,null);
    }
    public static R fail(String message){
        return new R(FAILSTATUS,message,null);
    }

    public static R fail(String message, Object data){
        return new R(FAILSTATUS,message,data);
    }
}
