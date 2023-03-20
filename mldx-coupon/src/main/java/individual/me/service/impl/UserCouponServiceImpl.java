package individual.me.service.impl;

import individual.me.module.coupon.UserCoupon;
import individual.me.module.coupon.CouponRollbackDto;
import individual.me.repository.UserCouponRepository;
import individual.me.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    @Autowired
    private UserCouponRepository userCouponRepository;


    @Override
    public void insertCoupon(UserCoupon userCoupon) {

    }

    @Override
    public void updateCoupon(UserCoupon userCoupon) {

    }

    @Override
    public void rollbackCoupon(Long orderId) {
        // 获取订单编号，根据订单编号回滚优惠券
        userCouponRepository.rollbackCoupon(orderId);
    }

    @Override
    public List<UserCoupon> selectUserCoupon(Long userId) {
        return null;
    }

    @Override
    public UserCoupon selectUserCouponByOrderId(Long orderId) {
        return null;
    }


}
