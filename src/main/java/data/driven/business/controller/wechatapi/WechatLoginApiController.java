package data.driven.business.controller.wechatapi;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.wechat.WechatAppInfoService;
import data.driven.business.business.wechat.WechatLoginLogService;
import data.driven.business.business.wechat.WechatUserService;
import data.driven.business.common.WechatApiSession;
import data.driven.business.common.WechatApiSessionBean;
import data.driven.business.entity.wechat.WechatAppInfoEntity;
import data.driven.business.util.JSONUtil;
import data.driven.business.vo.wechat.WechatUserInfoVO;
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
@RequestMapping(path = "/wechatapi/service")
public class WechatLoginApiController {

    private static final Logger logger = LoggerFactory.getLogger(WechatLoginApiController.class);

    @Autowired
    private WechatAppInfoService wechatAppInfoService;
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private WechatLoginLogService wechatLoginLogService;

    @ResponseBody
    @RequestMapping(path = "/login")
    public JSONObject login(String appid, String secret, String code){
        //根据code获取sessionKey
        JSONObject sessionJsonObject = getSessionKey(appid, secret, code);
        String sessionKey = null;
        if(sessionJsonObject.containsKey("session_key")){
            sessionKey = sessionJsonObject.getString("session_key");
        }else{
            return JSONUtil.putMsg(false, "101", "获取不到session_key，请重新登录");
        }
        //设置缓存
        WechatApiSessionBean wechatApiSessionBean = new WechatApiSessionBean();
        wechatApiSessionBean.setSessionKey(sessionKey);
        if(sessionJsonObject.containsKey("openid")){
            WechatUserInfoVO userInfo = new WechatUserInfoVO();
            userInfo.setOpenId(sessionJsonObject.getString("openid"));
            wechatApiSessionBean.setUserInfo(userInfo);
        }

        String sessionID = WechatApiSession.setSessionBean(wechatApiSessionBean);
        JSONObject result = JSONUtil.putMsg(true, "200", "调用成功");
        result.put("sessionID", sessionID);
        return result;
    }

    @ResponseBody
    @RequestMapping(path = "/syncUser")
    public JSONObject syncUser(String encryptedData, String iv, String sessionID){
        encryptedData = encryptedData.replace(" ","+");
        iv = iv.replace(" ","+");
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        WechatUserInfoVO sUser = wechatApiSessionBean.getUserInfo();
        String openId = null;
        if(sUser != null){
            openId = sUser.getOpenId();
        }

        String sessionKey = wechatApiSessionBean.getSessionKey();
        JSONObject userJson = executeDecodeUserInfo(encryptedData, iv, sessionKey);
        String appid = null;
        try{
            JSONObject watermark = userJson.getJSONObject("userInfo").getJSONObject("watermark");
            appid = watermark.getString("appid");
            Long timestamp = watermark.getLong("timestamp");
            long now = System.currentTimeMillis();
            long interval = now / 1000 - timestamp;
            if(interval < 0 || interval > USER_INFO_MAX_INTERVAL){
                return JSONUtil.putMsg(false, "102", "处理超时，请重新登录");
            }

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        if(appid == null){
            return JSONUtil.putMsg(false, "103", "获取的信息有误，请重试");
        }

        WechatAppInfoEntity wechatAppInfoEntity = wechatAppInfoService.getAppInfo(appid);
        if(wechatAppInfoEntity == null){
            return JSONUtil.putMsg(false, "104", "小程序不存在，非法登录");
        }
        WechatUserInfoVO userObj = userJson.getJSONObject("userInfo").toJavaObject(WechatUserInfoVO.class);
        if(openId == null){
            openId = userObj.getUnionId();
        }
        //根据openId获取数据库中的用户信息
        WechatUserInfoVO userInfo = wechatUserService.getUserInfoByOpenId(openId);
        if(userInfo == null){
            //如果openId获取的用户为空，就根据unionId获取用户信息
            userInfo = wechatUserService.getUserInfoByUnionId(userObj.getUnionId());
            String wechatUserInfoId = null;
            //如果用户信息为空，就新增用户到数据库中
            if(userInfo == null){
                System.out.println(userJson);
                wechatUserInfoId = wechatUserService.addUserInfo(userObj);
            }else{
                wechatUserInfoId = userInfo.getWechatUserId();
            }
            //根据openId获取用户为空时，就新增一条用户关联app的关系信息到数据库中
            wechatUserService.addUserAndAppMap(wechatAppInfoEntity.getAppInfoId(), wechatUserInfoId, openId);
            userInfo = wechatUserService.getUserInfoByOpenId(openId);
        }

        wechatApiSessionBean.setUserInfo(userInfo);
        WechatApiSession.setSessionBean(sessionID, wechatApiSessionBean);
        //插入登录日志
        wechatLoginLogService.insertLoginLog(userInfo.getWechatUserId(), userInfo.getAppInfoId());
        JSONObject result = JSONUtil.putMsg(true, "200", "登录成功");
        return result;
    }



}
