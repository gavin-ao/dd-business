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

}
