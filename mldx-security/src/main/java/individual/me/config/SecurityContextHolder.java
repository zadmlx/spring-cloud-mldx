package individual.me.config;

import individual.me.constants.StringConstants;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityContextHolder {

    private static final InheritableThreadLocal<Map<String,String>> USER_MAP = new InheritableThreadLocal<>();


    private static Map<String,String> getLocalMap(){
        Map<String, String> localMap = USER_MAP.get();
        if (CollectionUtils.isEmpty(localMap)){
            localMap = new ConcurrentHashMap<>(2);
            USER_MAP.set(localMap);
        }
        return localMap;
    }

    public static void set(String key,String value){
        Map<String, String> localMap = getLocalMap();
        localMap.put(key,value == null ? "" : value);
    }
    public static void setUsername(String username){
        set(StringConstants.USERNAME,username);
    }

    public static void setUserId(String userId){
        set(StringConstants.USERID,userId == null ? "" : userId);
    }


    private static String get(String key){
        Map<String, String> localMap = getLocalMap();
        String value = localMap.get(key);
        return value == null ? "" : value;
    }


    public static String getUsername(){
        return get(StringConstants.USERNAME);
    }



    public static Long getUserId(){
        String userId = get(StringConstants.USERID);
        return StringUtils.hasText(userId) ? Long.parseLong(userId) : null;
    }

    public static void remove(){
        USER_MAP.remove();
    }
}
