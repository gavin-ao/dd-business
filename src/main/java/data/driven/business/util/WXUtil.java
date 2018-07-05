package data.driven.business.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * 微信接口调用工具类
 * @author hejinkai
 * @date 2018/6/14
 */
public class WXUtil {
    /** 小程序id **/
    private static final String APPID = "wx6f8fab67827259b0";
    /** 小程序签名 **/
    private static final String SECRET = "ed71f12d39b999ee6c47d0b77a6f2c8c";
    /** 许可类型 **/
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
        return parseObject(resultStr);
    }

    /**
     * 根据code获取sessionKey
     * @param code
     * @return
     */
    public static JSONObject getSessionKey(String code){
        return getSessionKey(APPID, SECRET, code);
    }

    /**
     * 解密用户信息
     * @param encryptedData 用户信息密文
     * @param iv    初始向量 - 微信接口返回
     * @param code  微信登录接口返回的code，用于获取session_key
     */
    public static JSONObject decodeUserInfo(String encryptedData, String iv, String code){
        if(encryptedData == null || iv == null || code == null){
            return JSONUtil.putMsg(false, "101", "参数为空");
        }
        String session_key = null;
        JSONObject sessionJsonObject = getSessionKey(code);
        if(sessionJsonObject.containsKey("session_key")){
            session_key = sessionJsonObject.getString("session_key");
        }else{
            return JSONUtil.putMsg(false, "102", "获取不到session_key，请重新登录");
        }

        JSONObject result = executeDecodeUserInfo(encryptedData, iv, session_key);
        System.out.println(result);
        return result;
    }

    /**
     * 开始解密
     * @param encryptedData 用户信息密文
     * @param iv    初始向量 - 微信接口返回
     * @param session_key   利用code获取的session_key
     * @return
     */
    public static JSONObject executeDecodeUserInfo(String encryptedData, String iv, String session_key) {
        System.out.println("开始解密：-------");
        System.out.println("encryptedData:"+encryptedData);
        System.out.println("iv:"+iv);
        System.out.println("session_key:"+session_key);
        JSONObject result = null;
        try {
            byte[] resultByte  = AESUtil.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(session_key), Base64.decodeBase64(iv));
            if(null != resultByte && resultByte.length > 0){
                String userInfo = new String(resultByte, "UTF-8");
                result = JSONUtil.putMsg(true, "200", "解密成功");
                result.put("userInfo", JSONObject.parseObject(userInfo));
            }else{
                result = JSONUtil.putMsg(true, "109", "解密失败");
            }
        }catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args){

        String encryptedData = "hqZuO3yN7xpjh660AgzL0iQwIUrqnDqgclRGaxm7RatzkuH25bpxaTcolvSYQXqbbjZjgSbqRvbmi/ypycmnVRi69LGKy+SwGVDQC6Eoic6cFYJdoC3no5cu4bAfjtwTTW0x1M3gDnc/KNOBAFN/iqgcZbk6wPDA7vJU8wf0WwvC2+0x9z5AZLjnGQrN7cp6oc0e1RCjAQo2RJNp93fO0v5GRrD7HC4+oL/ioaddqSv5t72skesFGjUPI2exP3G/3DUGg5bvz515nxrYV+ocRCmk38bR8PYwhKmn9YiDo1ChQ+kwwKyzlIU7K4cWm91UxWka3UWv9irLNmWNNi/UUFmjutjOP0OH0BQblp4g54USgsjFR1Gylcjz4reyDrQPQxb74NmFuSDaquuyLdT4k1qtFrcZahogCVTbIU97NPimT8k08OR50DRMQYwzSoCnviOSBIvhbiAWg6klclX4w7HMkWgM6pujTuTs6VYTgE4=";
        String iv = "tP5c7E8+xkZoxGu6eT1HAQ==";
        String sessionKey = "YowUQR+UWuSTMzZmWmnv5g==";
//        String appId = "wx4f4bc4dec97d474b";
//        String sessionKey = "tiihtNczf5v6AKRyjwEUhQ==";
//        String encryptedData =
//                "CiyLU1Aw2KjvrjMdj8YKliAjtP4gsMZM"+
//                        "QmRzooG2xrDcvSnxIMXFufNstNGTyaGS"+
//                        "9uT5geRa0W4oTOb1WT7fJlAC+oNPdbB+"+
//                        "3hVbJSRgv+4lGOETKUQz6OYStslQ142d"+
//                        "NCuabNPGBzlooOmB231qMM85d2/fV6Ch"+
//                        "evvXvQP8Hkue1poOFtnEtpyxVLW1zAo6"+
//                        "/1Xx1COxFvrc2d7UL/lmHInNlxuacJXw"+
//                        "u0fjpXfz/YqYzBIBzD6WUfTIF9GRHpOn"+
//                        "/Hz7saL8xz+W//FRAUid1OksQaQx4CMs"+
//                        "8LOddcQhULW4ucetDf96JcR3g0gfRK4P"+
//                        "C7E/r7Z6xNrXd2UIeorGj5Ef7b1pJAYB"+
//                        "6Y5anaHqZ9J6nKEBvB4DnNLIVWSgARns"+
//                        "/8wR2SiRS7MNACwTyrGvt9ts8p12PKFd"+
//                        "lqYTopNHR1Vf7XjfhQlVsAJdNiKdYmYV"+
//                        "oKlaRv85IfVunYzO0IKXsyl7JCUjCpoG"+
//                        "20f0a04COwfneQAGGwd5oa+T8yO5hzuy"+
//                        "Db/XcxxmK01EpqOyuxINew==";
//        String iv = "r7BXXKkLb8qrSNn05n0qiA==";
        JSONObject j = executeDecodeUserInfo(encryptedData, iv, sessionKey);
//        System.out.println(getSessionKey("sss"));
//        System.out.println(getSessionKey("a","b","2"));
        System.out.println(j);
    }

}
