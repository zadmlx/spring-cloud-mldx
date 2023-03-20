package individual.me.module.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisCart {
    /** 产品主键 */
    private Long id;

    /** 产品属性 */
    private String detail;

    /** 产品数量 */
    private Integer amount;
}