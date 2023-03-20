package individual.me.module.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class RefundOrder {

    // 主键，退款订单
    private Long orderId;


    private Long userId;

    private String buyer;

    /**买家联系电话 */
    private String contractPhone;

    /**买家申请内容 */
    private String buyerReason;

    /**退款申请状态,0买家申请提交,1卖家处理,2系统退款,3完成,4系统退款异常,5卖拒绝退款,6买家取消退款*/
    private Integer status;

    /**订单金额。本次交易支付订单金额，单位为人民币（元），精确到小数点后 2 位*/
    private Double price;

    /**退款类型:0系统自动退款, 1用户发起退款, 2商家发起退款*/
    private Integer type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;


}
