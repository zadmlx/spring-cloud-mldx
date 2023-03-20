package individual.me.constants;

public class CacheConstants {
    public static final String ONLINE_TOKEN_KEY = "user-";
    //token有效期小于该30分钟的时候，就进行续期
    public static final Long TOKEN_RENEW_TTL = 1800L;
    // token有效期小于30分钟的时候，续期30分钟
    public static final Long TOKEN_RENEW_TIME = 1800L;

    public static final String VERIFY_TOKEN_PREFIX = "vc-";

    public static final Long VC_TTL = 120L;
}
