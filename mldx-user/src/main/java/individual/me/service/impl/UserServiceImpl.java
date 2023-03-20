package individual.me.service.impl;

import individual.me.Repository.UserRepository;

import individual.me.client.CloudProductService;
import individual.me.config.SecurityContextHolder;
import individual.me.constants.UserConstants;
import individual.me.domain.User;
import individual.me.module.user.UserVo;
import individual.me.exception.BadRequestException;
import individual.me.map.CartConverter;
import individual.me.map.UserConverter;
import individual.me.module.product.ShopProduct;
import individual.me.module.user.*;
import individual.me.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private CartConverter cartConverter;
    @Autowired
    private ThreadPoolTaskExecutor threadPool;
    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private CloudProductService cloudProductService;

    @Override
    public UserVo loadCurrentUser() {
        Long userId = getUserId();
        return loadUser(userId);
    }

    @Override
    public UserVo loadUserByUserId(Long userId) {
        if (userId == null) {
            log.error("用户信息为空，直接返回");
            return null;
        }
        log.info("开始查找id为{}的用户",userId);
        return loadUser(userId);
    }


    @Override
    public void update(User user) {

    }


    /**
     * 查询用户购物车，redis集合中，只含有每个数量和产品规格，返回前端还需要其他的信息，比如价格，图片等
     * date 2022年10月15日03:14:51
     *
     * @return
     */
    @Override
    public List<List<ShoppingCartVo>> selectUserShoppingCart(Long userId) {
        String cartKey = UserConstants.USER_SHOPPING_CART_KEY + userId;
        HashOperations<String, Long, List<RedisCart>> redisOperations = redisTemplate.opsForHash();
        try {
            // 通过redis中的集合，查询

            CompletableFuture<Set<Long>> idsFuture = CompletableFuture.supplyAsync(() -> {
                // 首先获取到所有的购物车商品主键，在根据主键去查询商品的其他信息
                Set<Long> ids = redisOperations.keys(cartKey);

                return CollectionUtils.isEmpty(ids) ? null : ids;
            });


            /*
             * 调用其他服务查询产品信息需要上一步的返回结果，同时有返回值
             *
             *
             * thenApply:任务 A 执行完执行 B，B 需要 A 的结果，同时任务 B 有返回值
             * thenRun:任务 A 执行完执行 B，并且 B 不需要 A 的结果
             * thenAccept:任务 A 执行完执行 B，B 需要 A 的结果，但是任务 B 无返回值
             *
             * 我们需要上一任务的返回结果，同时会返回结果,
             * 首先远程调用产品服务，获取对应主键的产品信息，
             * 然后批量获取redis集合中的所有产品的key
             *
             * redis中，用户购物车大致结构
             * userId:商品id:{"商品id","商品规格","商品数量"};
             * 1:10010:{"10010","紫色","2";"10010","绿色","1"}
             *   10011:{"10011","紫色","2"}
             *   10012:{"10012","紫色","2"}
             *
             * 将redis购物车的信息填充到ShoppingCartVo中，{id,description,categoryId,detail,price,amount}
             * 目前已有id，detail,amount;
             * 调用产品服务获取产品信息，可以获取到剩余所需的description，categoryId，price；
             *
             * 填充之后返回。
             *
             *
             *
             * 首先可以批量获取hashKey也就是商品id的set集合，然后每个id对应一个集合，
             * 然后使用线程池线程去获取
             */

            CompletableFuture<List<ShopProduct>> productsFuture = idsFuture.thenApply(cloudProductService::selectShopProducts);
            CompletableFuture<List<List<RedisCart>>> redisCartFuture = idsFuture.thenApplyAsync(ids -> redisOperations.multiGet(cartKey, ids), threadPool);


            // 获取到组装ShoppingCartVo的两个对象，然后进行组装
            CompletableFuture<List<List<ShoppingCartVo>>> VoFuture = productsFuture.thenCombine(redisCartFuture, (products, redisCartLists) -> {
                // 此处最好有一个converter，拷贝这俩的属性到新的集合中
                return redisCartLists.stream().map(redisCarts -> cartConverter.
                        toShopCartVos(products, redisCarts)).sorted().toList();
            });
            // 阻塞等到执行完成之后返回
            return VoFuture.join();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加产品到购物车，
     * @param productId 产品id
     * @param detail 产品规格
     */
    @Override
    public void add2shoppingCart(Long productId, String detail) {
        Long userId = getUserId();
        String key = UserConstants.USER_SHOPPING_CART_KEY + userId;

        HashOperations<String, Long, List<RedisCart>> redisOperations = redisTemplate.opsForHash();
        // 首先查询购物车中的同类产品是否存在，


        List<RedisCart> redisCarts = redisOperations.get(key, productId);

        /*
         * 一个用户对应一个hash结构，userId:商品id1:{"1","紫色","10"; "1","粉色","1"; ...}
         *                                商品id2:{"2","紫色","10"; "1","黑色","1"; ...}
         * 1 直接判断返回的list是否为空，为空直接添加一个产品信息进去，完后退出
         * 2 不为空，遍历集合中的每个对象判断与待添加的属性是否一致，是的话，数量加一，
         * 3 不是的话，就创建一个对象，添加到集合中，在把集合直接更新。
         */

        // 如果该款商品在redis购物车集合中不存在，那就直接添加,不用判断具体某个规格，
        if (CollectionUtils.isEmpty(redisCarts)){
            RedisCart redisCart = new RedisCart(productId, detail, 1);
            Objects.requireNonNull(redisCarts).add(redisCart);
            redisOperations.put(key,productId,redisCarts);
            // 添加完毕，直接退出
            return;
        }

        //redisCarts.stream().filter(item->item.getDetail().equals(detail)).map()

        // redis购物车集合中可能有该这个产品的信息，但也可能没有，有的话，遍历到产品信息一致的，加一之后直接返回
        for (RedisCart redisCart : redisCarts) {
            if (StringUtils.equals(redisCart.getDetail(),detail)){
                redisCart.setAmount(redisCart.getAmount() + 1);
                return;
            }
        }



        // 没有的话，就在集合中添加一项
        redisCarts.add(new RedisCart(productId,detail,1));
        redisOperations.put(key,productId,redisCarts);



    }


    private Long getUserId(){
        return SecurityContextHolder.getUserId();
    }


    private UserVo loadUser(Long userId){
        log.info("开始查询用户信息");
        if (userId < 1){
            log.error("请求的用户id：{}非法",userId);
            return null;
        }
        User user = userRepository.loadUserById(userId);
        return userConverter.toVo(user);
    }

}
