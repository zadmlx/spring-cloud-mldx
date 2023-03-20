package individual.me.service.impl;

import com.alibaba.fastjson.JSONArray;
import individual.me.cache.ProductCache;
import individual.me.module.product.ProductVo;
import individual.me.map.ProductConverter;
import individual.me.module.product.ShopProduct;
import individual.me.repository.ProductRepository;
import individual.me.service.ProductService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ProductConverter productConverter;

    private static final String CATEGORY_CACHE_REBUILD_KEY = "lock-category";
    @Override
    public List<ShopProduct> selectAllProduct(Long shopId) {
        return null;
    }

    @Override
    public List<ShopProduct> selectOneType(Long shopId,Long categoryId) {
        String key = shopId + ProductCache.CATEGORY + categoryId;
        Object o = redisTemplate.opsForValue().get(key);
        if (!ObjectUtils.isEmpty(o)){
            String all = JSONArray.toJSONString(o);
            return JSONArray.parseArray(all, ShopProduct.class);
        }

        if (o != null){
            return null;
        }

        // 重建缓存，加锁
        // 缓存中没有，值也不是""，所以需要从数据库查询，数据库如果为空，那么就缓存"";
        RLock lock = redissonClient.getLock(CATEGORY_CACHE_REBUILD_KEY + shopId);
        try {
            if (lock.tryLock()){
                List<ShopProduct> shopProducts = productRepository.selectOneType(shopId, categoryId);
                if (CollectionUtils.isEmpty(shopProducts)){
                    redisTemplate.opsForValue().set(key,"",ProductCache.CATEGORY_NULL_CACHE, TimeUnit.SECONDS);
                    return null;
                }

                redisTemplate.opsForValue().set(key,shopProducts,ProductCache.CATEGORY_NONNULL_CACHE,TimeUnit.SECONDS);
            }
        } finally {
            lock.unlock();
        }

        return null;

    }

    @Override
    public ProductVo selectProduct(Long proId) {
        return null;
    }

    @Override
    public void updateProduct(ShopProduct product) {

    }

    @Override
    public void deleteProducts(List<Long> ids) {

    }

    @Override
    public void insertProduct(ShopProduct product) {

    }

    @Override
    public List<ShopProduct> selectShopProducts(Set<Long> productIds) {
        return null;
    }
}
