package data.driven.business.controller.system;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.common.SessionBean;
import data.driven.business.common.UserSession;
import data.driven.business.util.JSONUtil;
import data.driven.business.vo.system.UserInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hejinkai
 * @date 2018/6/29
 */
@Controller
@RequestMapping(path = "/system/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 获取登录用户信息
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/getUserInfoId")
    public JSONObject getUserInfoId(String sessionID){
        SessionBean sessionBean = UserSession.getSessionBean(sessionID);
        UserInfoVO userInfo = sessionBean.getUserInfo();
        userInfo.clearSensitiveInfo();
        JSONObject result = JSONUtil.putMsg(true, "200", "调用成功");
        result.put("userInfo", userInfo);
        return result;
    }
}
