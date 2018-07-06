package data.driven.business.controller.wechatapi;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.wechat.WechatShareDetailService;
import data.driven.business.business.wechat.WechatShareInfoService;
import data.driven.business.business.wechat.WechatUserService;
import data.driven.business.common.WechatApiSession;
import data.driven.business.common.WechatApiSessionBean;
import data.driven.business.entity.wechat.WechatShareInfoEntity;
import data.driven.business.util.JSONUtil;
import data.driven.business.util.UUIDUtil;
import data.driven.business.vo.wechat.WechatUserInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static data.driven.business.util.JSONUtil.putMsg;

/**
 * 分享
 * @author hejinkai
 * @date 2018/7/4
 */
@Controller
@RequestMapping(path = "/wechatapi/share")
public class WechatShareApiController {
    private static final Logger logger = LoggerFactory.getLogger(WechatShareApiController.class);

    @Autowired
    private WechatShareInfoService wechatShareInfoService;
    @Autowired
    private WechatShareDetailService wechatShareDetailService;
    @Autowired
    private WechatUserService wechatUserService;

    @ResponseBody
    @RequestMapping(path = "/getShareId")
    public JSONObject getShareId(String sessionID){
        JSONObject result = JSONUtil.putMsg(true, "200", "获取成功");
        String shareId = UUIDUtil.getUUID();
        result.put("shareId", shareId);
        return result;
    }

    /**
     * 进行分享动作，记录分享
     * @param sessionID
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/execuShare")
    public JSONObject execuShare(String sessionID, String shareId, String content){
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        try{
            wechatShareInfoService.insertShare(shareId, wechatApiSessionBean.getUserInfo().getWechatUserId(), content, wechatApiSessionBean.getUserInfo().getAppInfoId());
            JSONObject result = JSONUtil.putMsg(true, "200", "分享成功");
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return putMsg(false, "101", "分享失败");
        }
    }

    @ResponseBody
    @RequestMapping(path = "/clickShareUrl")
    public JSONObject clickShareUrl(String sessionID, String shareId){
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        System.out.println("wechatApiSessionBean_"+JSONObject.toJSONString(wechatApiSessionBean));
        try{
            WechatShareInfoEntity shareInfoEntity = wechatShareInfoService.getEntityById(shareId);
            System.out.println("shareInfoEntity_"+JSONObject.toJSONString(shareInfoEntity));
            if(shareInfoEntity != null){
                WechatUserInfoVO fromUserInfo = wechatUserService.getUserInfoByUserIdAndAppInfoId(shareInfoEntity.getWechatUserId(), shareInfoEntity.getAppInfoId());
                System.out.println("fromUserInfo_"+JSONObject.toJSONString(fromUserInfo));
                WechatUserInfoVO toUserInfo = wechatApiSessionBean.getUserInfo();
                System.out.println("toUserInfo_"+JSONObject.toJSONString(toUserInfo));
                wechatShareDetailService.insertShareDetail(fromUserInfo, toUserInfo, shareInfoEntity);
                JSONObject result = JSONUtil.putMsg(true, "200", "分享点击记录成功");
                return result;
            }else{
                return putMsg(false, "101", "分享点击记录失败");
            }
        }catch (Exception e){
            logger.error("clickShareUrl报错_"+e.getMessage(), e);
            e.printStackTrace();
            return putMsg(false, "102", "分享点击记录失败");
        }
    }

}
