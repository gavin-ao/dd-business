package data.driven.business.controller.wechatapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.wechat.WechatHelpDetailService;
import data.driven.business.business.wechat.WechatHelpInfoService;
import data.driven.business.business.wechat.WechatUserService;
import data.driven.business.common.WechatApiSession;
import data.driven.business.common.WechatApiSessionBean;
import data.driven.business.entity.wechat.WechatHelpInfoEntity;
import data.driven.business.util.UUIDUtil;
import data.driven.business.vo.wechat.WechatHelpDetailUserVO;
import data.driven.business.vo.wechat.WechatUserInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static data.driven.business.util.JSONUtil.putMsg;

/**
 * 助力
 * @author hejinkai
 * @date 2018/7/13
 */
@Controller
@RequestMapping(path = "/wechatapi/help")
public class WechatHelpApiController {
    private static final Logger logger = LoggerFactory.getLogger(WechatShareApiController.class);

    @Autowired
    private WechatHelpInfoService wechatHelpInfoService;
    @Autowired
    private WechatHelpDetailService wechatHelpDetailService;
    @Autowired
    private WechatUserService wechatUserService;

    @ResponseBody
    @RequestMapping(path = "/getHelpId")
    public JSONObject getHelpId(String sessionID){
        JSONObject result = putMsg(true, "200", "获取成功");
        String helpId = UUIDUtil.getUUID();
        result.put("helpId", helpId);
        return result;
    }

    /**
     * 进行邀请好友助力，生成助力信息
     * @param sessionID
     * @param helpId
     * @param actId 活动id
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/execuHelp")
    public JSONObject execuHelp(String sessionID, String helpId, String actId){
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        String wechatUserId = wechatApiSessionBean.getUserInfo().getWechatUserId();
        try{
            WechatHelpInfoEntity wechatHelpInfoEntity = wechatHelpInfoService.getHelpInfoByActId(actId, wechatUserId);
            if(wechatHelpInfoEntity != null){
                return putMsg(false, "101", "邀请助力失败，已经邀请过了");
            }
            wechatHelpInfoService.insertHelp(helpId, actId, wechatUserId, wechatApiSessionBean.getUserInfo().getAppInfoId());
            JSONObject result = putMsg(true, "200", "邀请助力成功");
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return putMsg(false, "102", "邀请助力失败");
        }
    }

    private WechatApiSessionBean test(String openId){
        WechatApiSessionBean wechatApiSessionBean = new WechatApiSessionBean();
        WechatUserInfoVO userInfo = wechatUserService.getUserInfoByOpenId(openId);
        wechatApiSessionBean.setUserInfo(userInfo);
        return wechatApiSessionBean;
    }

    /**
     * 记录助力动作
     * @param sessionID
     * @param helpId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/clickHelpUrl")
    public JSONObject clickHelpUrl(String sessionID, String helpId){
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        try{
            WechatHelpInfoEntity helpInfoEntity = wechatHelpInfoService.getEntityById(helpId);
            if(helpInfoEntity != null){
                WechatUserInfoVO fromUserInfo = wechatUserService.getUserInfoByUserIdAndAppInfoId(helpInfoEntity.getWechatUserId(), helpInfoEntity.getAppInfoId());
                WechatUserInfoVO toUserInfo = wechatApiSessionBean.getUserInfo();
                String helpDetailId = wechatHelpDetailService.getHelpDetailId(fromUserInfo.getWechatMapId(), toUserInfo.getWechatMapId(), helpId);
                if(helpDetailId != null){
                    return putMsg(false, "101", "助力点击记录失败,已经助力过了");
                }else{
                    wechatHelpDetailService.insertHelpDetail(fromUserInfo, toUserInfo, helpInfoEntity);
                    JSONObject result = putMsg(true, "200", "助力点击记录成功");
                    return result;
                }
            }else{
                return putMsg(false, "102", "助力点击记录失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return putMsg(false, "103", "助力点击记录失败");
        }
    }

    /**
     * 获取助力的微信用户信息集合
     * @param sessionID
     * @param helpId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/findHelpDetailUserList")
    public JSONObject findHelpDetailUserList(String sessionID, String helpId){
        try{
            List<WechatHelpDetailUserVO> wechatHelpDetailUserVOList = wechatHelpDetailService.findHelpDetailUserListByHelpId(helpId);
            if(wechatHelpDetailUserVOList != null && wechatHelpDetailUserVOList.size() > 0){
                JSONObject result = putMsg(true, "200", "查询成功");
                result.put("data", JSONArray.parseArray(JSONArray.toJSONString(wechatHelpDetailUserVOList)));
                return result;
            }else{
                return putMsg(false, "101", "助力用户查询失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return putMsg(false, "102", "助力用户查询失败");
        }
    }

}
