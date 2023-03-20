package individual.me.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Log implements Serializable {
    private Long id;
    private String username;
    private String description;
    private String method;
    private String logType;
    private String ip;
    private String browser;
    private Long time;
    private String exception;
    private LocalDateTime createTime;

    public Log(String logType,Long time){
        this.logType = logType;
        this.time = time;
    }
}
