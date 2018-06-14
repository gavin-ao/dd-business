package data.driven.business.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信接口调用工具类
 * @author hejinkai
 * @date 2018/6/14
 */
public class WXUtil {

    private static final String APPID = "";
    private static final String SECRET = "";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    /** 根据登录code获取session_key的url **/
    private static final String jscode2session_url = "https://api.weixin.qq.com/sns/jscode2session";
//    js_code=JSCODE&

    /**
     * 根据appid,secret,code获取sessionKey
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public static JSONObject getSessionKey(String appid, String secret, String code){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appid", appid);
        paramMap.put("secret", secret);
        paramMap.put("grant_type", AUTHORIZATION_CODE);
        paramMap.put("js_code", code);
        String resultStr = HttpUtil.doPostSSL(jscode2session_url, paramMap);
        if(resultStr == null){
            return new JSONObject();
        }
        return JSONObject.parseObject(resultStr);
    }

    /**
     * 根据code获取sessionKey
     * @param code
     * @return
     */
    public static JSONObject getSessionKey(String code){
        return getSessionKey(APPID, SECRET, code);
    }

    public static void main(String[] args){
//        System.out.println(getSessionKey("sss"));
        System.out.println(getSessionKey("a","b","2"));
    }

}
