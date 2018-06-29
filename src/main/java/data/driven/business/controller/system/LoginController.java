package data.driven.business.controller.system;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.system.AppInfoService;
import data.driven.business.business.system.UserInfoService;
import data.driven.business.common.SessionBean;
import data.driven.business.common.UserSession;
import data.driven.business.entity.system.AppInfoEntity;
import data.driven.business.util.JSONUtil;
import data.driven.business.vo.system.UserInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static data.driven.business.common.Constant.USER_INFO_MAX_INTERVAL;
import static data.driven.business.util.WXUtil.executeDecodeUserInfo;
import static data.driven.business.util.WXUtil.getSessionKey;

/**
 * 登录
 * @author hejinkai
 * @date 2018/6/27
 */

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private UserInfoService userInfoService;

    @ResponseBody
    @RequestMapping(path = "/login")
    public JSONObject login(String code){
        //根据code获取sessionKey
        JSONObject sessionJsonObject = getSessionKey(code);
        String session_key = null;
//        if(sessionJsonObject.containsKey("session_key")){
//            session_key = sessionJsonObject.getString("session_key");
//        }else{
//            return JSONUtil.putMsg(false, "101", "获取不到session_key，请重新登录");
//        }
        //设置缓存
        SessionBean sessionBean = new SessionBean();
        //测试reids
        session_key = "XFeGmty0TKKqpIbegcK7ug==";
        sessionBean.setSessionKey(session_key);
        if(sessionJsonObject.containsKey("openid")){
            UserInfoVO userInfo = new UserInfoVO();
            userInfo.setOpenId(sessionJsonObject.getString("openid"));
            sessionBean.setUserInfo(userInfo);
        }

        String sessionID = UserSession.setSessionBean(sessionBean);
        JSONObject result = JSONUtil.putMsg(true, "200", "调用成功");
        result.put("sessionID", sessionID);
        return result;
    }

    @ResponseBody
    @RequestMapping(path = "/login/syncUser")
    public JSONObject login(String encryptedData, String iv, String sessionID){
        encryptedData = encryptedData.replace(" ","+");
        SessionBean sessionBean = UserSession.getSessionBean(sessionID);
        UserInfoVO sUser = sessionBean.getUserInfo();
        String openId = null;
        if(sUser != null){
            openId = sUser.getOpenId();
        }

        String sessionKey = sessionBean.getSessionKey();
        JSONObject userJson = executeDecodeUserInfo(encryptedData, iv, sessionKey);
        String appid = null;
        try{
            JSONObject watermark = userJson.getJSONObject("userInfo").getJSONObject("watermark");
            appid = watermark.getString("appid");
            Long timestamp = watermark.getLong("timestamp");
            long now = System.currentTimeMillis();
            long interval = now - timestamp;
            if(interval < 0 || interval > USER_INFO_MAX_INTERVAL){
                return JSONUtil.putMsg(false, "102", "处理超时，请重新登录");
            }

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        if(appid == null){
            return JSONUtil.putMsg(false, "103", "获取的信息有误，请重试");
        }

        AppInfoEntity appInfoEntity = appInfoService.getAppInfo(appid);
        if(appInfoEntity == null){
            return JSONUtil.putMsg(false, "104", "小程序不存在，非法登录");
        }
        UserInfoVO userObj = userJson.toJavaObject(UserInfoVO.class);
        if(openId == null){
            openId = userObj.getUnionId();
        }
        //根据openId获取数据库中的用户信息
        UserInfoVO userInfo = userInfoService.getUserInfoByOpenId(openId);
        if(userInfo == null){
            //如果openId获取的用户为空，就根据unionId获取用户信息
            userInfo = userInfoService.getUserInfoByUnionId(userObj.getUnionId());
            String userInfoId = null;
            //如果用户信息为空，就新增用户到数据库中
            if(userInfo == null){
                userInfoId = userInfoService.addUserInfo(userInfo);
            }else{
                userInfoId = userInfo.getUserInfoId();
            }
            //根据openId获取用户为空时，就新增一条用户关联app的关系信息到数据库中
            userInfoService.addUserAndAppMap(appInfoEntity.getAppInfoId(), userInfoId, openId);
        }

        sessionBean.setUserInfo(userInfo);
        UserSession.setSessionBean(sessionID, sessionBean);
        JSONObject result = JSONUtil.putMsg(true, "200", "登录成功");
        return result;
    }



}
