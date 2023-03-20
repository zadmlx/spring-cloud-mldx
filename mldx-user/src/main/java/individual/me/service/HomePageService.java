package individual.me.service;

import com.alibaba.fastjson.JSON;
import individual.me.client.CloudProductService;
import individual.me.client.CloudUserService;
import individual.me.config.SecurityContextHolder;
import individual.me.constants.UserConstants;
import individual.me.domain.R;
import individual.me.module.product.ShopProduct;
import individual.me.module.user.HomePageVo;
import individual.me.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
@Service
public class HomePageService implements InitializingBean {

    private static final String HOMEPAGE_VO = "home-page-cache";
    @Autowired
    private CloudProductService cloudProductService;


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private LinkedBlockingDeque<CompletableFuture<HomePageVo>> blockingDeque;

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;


    public HomePageVo loadHomePage(){
        log.info("当前线程:{}",Thread.currentThread().getName());
        CompletableFuture<HomePageVo> homePageFuture;
        homePageFuture = new CompletableFuture<>();
        log.info("添加查询首页的任务到定时队列");
        blockingDeque.add(homePageFuture);
        return homePageFuture.join();
    }


    @Override
    public void afterPropertiesSet() {
        init();
    }


    private void init(){
        blockingDeque = new LinkedBlockingDeque<>(128);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            if (blockingDeque.isEmpty()) {
                return;
            }
            // 从redis查询缓存
            log.info("开始从redis查询缓存");
            String homepageVoString = (String) redisTemplate.opsForValue().get(HOMEPAGE_VO);
            HomePageVo homePageVo = JSON.parseObject(homepageVoString, HomePageVo.class);
            // 缓存不存在，那就使用redisson 分布式锁，保证只有一个线程来进行缓存重建
            if (ObjectUtils.isEmpty(homePageVo)){
                log.info("deque size :{}",blockingDeque.size());
                log.info("缓存为空，申请redisson锁");
                RLock lock = redissonClient.getLock("homepage:lock");
                try {
                    lock.lock();
                    log.info("申请锁成功，开始首页数据的缓存重建");
                    homePageVo = rebuildHomepageVo();
                    //
                    log.info("数据已返回，存入redis");
                    redisTemplate.opsForValue().set(HOMEPAGE_VO, JSON.toJSONString(homePageVo), UserConstants.HOMEPAGE_CACHE_TTL, TimeUnit.SECONDS);
                } finally {
                    lock.unlock();
                }
            }

            log.info("redis缓存不为空，直接返回");
            Objects.requireNonNull(blockingDeque.poll()).complete(homePageVo);
            //
        },0,1, TimeUnit.SECONDS);
    }


    private HomePageVo rebuildHomepageVo(){
        HomePageVo homePageVo = new HomePageVo();
        log.info("调用产品微服务，查询产品信息");
        List<ShopProduct> shopProducts = cloudProductService.selectOneType(1L, 1L);
        log.info("查询结束，开始返回数据");
        homePageVo.setShopProducts(shopProducts);
        return homePageVo;
    }
}
