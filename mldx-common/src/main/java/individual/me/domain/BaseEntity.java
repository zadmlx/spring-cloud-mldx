package individual.me.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss")
    private LocalDateTime updateTime;

}
