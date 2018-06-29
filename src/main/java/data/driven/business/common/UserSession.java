package data.driven.business.common;

import data.driven.business.util.DateFormatUtil;
import data.driven.business.util.UUIDUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户session
 * @author hejinkai
 * @date 2018/6/26
 */
public class UserSession {

    /** 用于判断session是否能够自动延期，原因是权限设置是按天的，为了防止无限期使用的bug，即如果一直做交互session永不过期。限制为一天内失效 **/
    private static final String IS_EXPIRE_KEY = "EXPIRE_KEY_";
    private static final String IS_EXPIRE_VALUE = "TRUE";

    /**
     * 获取缓存对象
     * @param sessionID
     * @return
     */
    public static SessionBean getSessionBean(String sessionID){
        SessionBean sessionBean = RedisFactory.get(sessionID, SessionBean.class);
        //交互时自动延期
        autoDelayExpire(sessionID);
        return sessionBean;
    }

    /**
     * 更新缓存对象
     * @param sessionID
     * @param sessionBean
     * @return
     */
    public static void setSessionBean(String sessionID, SessionBean sessionBean){
        RedisFactory.set(sessionID, sessionBean, Constant.REDIS_DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * 登录时存入缓存,返回sessionID
     */
    public static String setSessionBean(SessionBean sessionBean){
        String sessionID = UUIDUtil.getUUID();
        //设置缓存
        setSessionBean(sessionID, sessionBean);
        //设置自动延期时效
        setExpireKeyToRedis(sessionID);
        return sessionID;
    }

    /**
     * 设置限制一天内session能自动延期的判断
     * @param sessionID
     */
    public static void setExpireKeyToRedis(String sessionID){
        Date now = new Date();
        Date nextDate = DateFormatUtil.addDate(now, 3, 1);
        SimpleDateFormat sdf = DateFormatUtil.getLocal();
        Date endDate = null;
        try{
            endDate = sdf.parse(sdf.format(nextDate));
        }catch (ParseException e){
            return ;
        }
        long startTime = now.getTime();
        long endTime = endDate.getTime();
        long milliseconds = endTime - startTime;
        String key = IS_EXPIRE_KEY + sessionID;
        RedisFactory.setString(key, IS_EXPIRE_VALUE, milliseconds);
    }

    /**
     * 交互时session自动延期
     * @param sessionID
     */
    public static void autoDelayExpire(String sessionID){
        if(isAutoRenewal(sessionID)){
            RedisFactory.expire(sessionID, Constant.REDIS_DEFAULT_EXPIRE_SECONDS);
        }
    }

    /**
     * 判断是否能够自动延期
     * @param sessionID
     * @return
     */
    private static boolean isAutoRenewal(String sessionID){
        String key = IS_EXPIRE_KEY + sessionID;
        String expireKey = RedisFactory.get(key);
        if(expireKey != null && IS_EXPIRE_VALUE.equals(expireKey)){
            return true;
        }
        return false;
    }

}
