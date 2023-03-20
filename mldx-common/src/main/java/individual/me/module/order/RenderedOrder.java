package individual.me.module.order;

import individual.me.module.coupon.UserCoupon;
import individual.me.module.product.ProductVo;
import individual.me.module.user.AddressBook;
import lombok.Data;

import java.util.List;

@Data
public class RenderedOrder {

    private List<ProductVo> productVos;

    private AddressBook addressBook;

    private List<UserCoupon> coupons;

    private Double totalPrice;
    private Long userId;
}
