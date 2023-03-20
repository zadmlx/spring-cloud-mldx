package individual.me.client;

import individual.me.domain.R;
import individual.me.module.coupon.Coupon;
import individual.me.module.coupon.CouponRollbackDto;
import individual.me.module.coupon.UserCoupon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mldx-coupon")
public interface CloudCouponService {

    @PostMapping("/coupon/rollBack")
    R couponRollback(Long orderId);


    // 卖家后台管理系统手动添加优惠券信息
    @PostMapping("coupon/insertShopCoupon")
    R insertShopCoupon(@RequestBody Coupon coupon);

    @GetMapping("/get/{id}")
    List<UserCoupon> RemoteSelectCoupon(@PathVariable("id")Long userId);

}
