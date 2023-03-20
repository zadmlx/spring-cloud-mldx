package individual.me.controller;


import cn.hutool.core.util.IdUtil;
import individual.me.client.CloudCouponService;
import individual.me.constants.SellerConstants;
import individual.me.domain.R;
import individual.me.domain.Seller;
import individual.me.module.coupon.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
public class CouponController {


    @Autowired
    private CloudCouponService cloudCouponService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/add")
    public R addCoupon(@RequestBody Coupon coupon){
        // 卖家添加优惠券信息，后端生成雪花id，一来可以避免主键id自增泄露商业信息，
        // 二来雪花id是自增的，存入数据库是有顺序的，避免了数据库发生页合并和分裂
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Seller seller = (Seller) authentication.getPrincipal();

        long flakeBachId = IdUtil.getSnowflakeNextId();
        coupon.setBatch(flakeBachId).setShopId(seller.getShopId());

        //调用coupon微服务，添加优惠券信息到数据库
        cloudCouponService.insertShopCoupon(coupon);

        // 添加到redis中，抢券的时候避免频繁请求数据库,redisson加锁，判断redis中库存是否>0,是的话直接由mq来完成添加优惠券到用户账户
        redisTemplate.opsForValue().set(SellerConstants.COUPON_KEY + flakeBachId,coupon.getStock());

        return R.ok("添加成功");

        // 保存到数据库，可以使用
    }


}
