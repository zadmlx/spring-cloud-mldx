-- 1,抢购优惠券，需要判断库存，需要库存的key，
-- 2,查看自己的优惠券集合，是否有该优惠券id，如果有，返回-1，
-- 3如果没有，库存减一，将优惠券id添加到自己的优惠券集合中

local stockKey = ARGV[1]
local userCouponList = ARGV[2]
local couponId = ARGV[3]
-- 查询库存
local stock = redis.call('get',stockKey)
if tonumber(stockKey) <= 0 then
    return -1
end

-- 库存充足，就开始查询用户是否已经抢购过,抢购过则返回1，用户抢到券之后，添加到自己的集合中，
if redis.call('ismember',userCouponList,couponId) == 1 then
    return 1
end


-- 没有抢购过，那就扣减库存，将优惠券id添加到自己的优惠券集合中
redis.call('decr',stockKey,1)
redis.call("sadd",userCouponList,couponId)
return 0