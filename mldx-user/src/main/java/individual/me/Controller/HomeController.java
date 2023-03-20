package individual.me.Controller;


import individual.me.config.SecurityContextHolder;
import individual.me.domain.R;
import individual.me.module.user.HomePageVo;
import individual.me.module.user.UserVo;
import individual.me.service.HomePageService;
import individual.me.service.UserService;
import individual.me.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequestMapping("/user")
@RestController
public class HomeController {

    @Autowired
    private HomePageService homePageService;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringUtil.getBean("mldxExecutor");

    @Autowired
    private UserService userService;

    // 用户进入首页，默认查询app主页，用户购物车如果是登录了的话和用户页信息
    @GetMapping("/profile")
    public R homePage(){
        log.info("开始查询用户信息");
        CompletableFuture<HomePageVo> future = CompletableFuture.supplyAsync(homePageService::loadHomePage, threadPoolTaskExecutor);
        Long userId = SecurityContextHolder.getUserId();
        log.info("当前用户id:{}",userId);
        UserVo userVo = userService.loadUserByUserId(userId);
        log.info("查询用户信息完成,阻塞异步任务完成");
        HomePageVo homePageVo = future.join();
        homePageVo.setUserInfo(userVo);
        return R.ok(homePageVo);
    }

}
