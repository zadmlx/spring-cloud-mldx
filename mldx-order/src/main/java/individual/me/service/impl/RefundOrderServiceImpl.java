package individual.me.service.impl;

import individual.me.module.order.Order;
import individual.me.module.order.RefundOrder;
import individual.me.service.RefundOrderService;
import org.springframework.stereotype.Service;

/*
TODO
 */
@Service
public class RefundOrderServiceImpl implements RefundOrderService {

    @Override
    public void updateRefundOrder(Order order) {

    }

    /**
     * 注意，此处，更新时间在数据库里设置
     * @param refund
     */
    @Override
    public void insertRefundOrder(RefundOrder refund) {

    }

    @Override
    public RefundOrder selectRefundOrder(Long orderId) {
        return null;
    }
}
