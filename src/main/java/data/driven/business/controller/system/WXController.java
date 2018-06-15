package data.driven.business.controller.system;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.util.WXUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信测试controller
 * @author hejinkai
 * @date 2018/6/15
 */
@Controller
@RequestMapping(path = "/wx/test")
public class WXController {

    @ResponseBody
    @RequestMapping(path = "/getSession")
    public JSONObject getSession(String appid, String secret, String code){
        return WXUtil.getSessionKey(appid, secret, code);
    }

    /**
     * 解密用户信息
     * @param encryptedData 用户信息密文
     * @param iv    初始向量 - 微信接口返回
     * @param code  微信登录接口返回的code，用于获取session_key
     */
    @ResponseBody
    @RequestMapping(path = "/decodeUserInfo")
    public JSONObject decodeUserInfo(String encryptedData, String iv, String code){
        encryptedData = encryptedData.replace(" ","+");
        return WXUtil.decodeUserInfo(encryptedData, iv, code);
    }

}
