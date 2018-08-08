package data.driven.business.controller.wechatapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.reward.RewardActCommandService;
import data.driven.business.business.wechat.WechatHelpDetailService;
import data.driven.business.business.wechat.WechatHelpInfoService;
import data.driven.business.business.wechat.WechatUserService;
import data.driven.business.common.WechatApiSession;
import data.driven.business.common.WechatApiSessionBean;
import data.driven.business.entity.reward.RewardActCommandEntity;
import data.driven.business.entity.wechat.WechatHelpDetailEntity;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    private Lock lock = new ReentrantLock();

    @Autowired
    private WechatHelpInfoService wechatHelpInfoService;
    @Autowired
    private WechatHelpDetailService wechatHelpDetailService;
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private RewardActCommandService rewardActCommandService;

    /**
     * 根据actId和当前登录微信用户判断是否发起过助力，如果id为空则没有发起过
     * @param sessionID
     * @param actId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/getHelpId")
    public JSONObject getHelpId(String sessionID, String actId){
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        String wechatUserId = wechatApiSessionBean.getUserInfo().getWechatUserId();
        WechatHelpInfoEntity wechatHelpInfoEntity = wechatHelpInfoService.getHelpInfoByActId(actId, wechatUserId);
        String helpId = null;
        if(wechatHelpInfoEntity != null){
            helpId = wechatHelpInfoEntity.getHelpId();
            JSONObject result = putMsg(true, "200", "获取成功");
            result.put("helpId", helpId);
            return result;
        }else{
            return putMsg(false, "101", "获取失败");
        }
    }

    /**
     * 获取助力的微信用户信息集合和helpId
     * @param actId
     * @param sessionID
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/findHelpDetailUserList")
    public JSONObject findHelpDetailUserList(String sessionID, String actId){
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        String wechatUserId = wechatApiSessionBean.getUserInfo().getWechatUserId();
        WechatHelpInfoEntity wechatHelpInfoEntity = wechatHelpInfoService.getHelpInfoByActId(actId, wechatUserId);
        String helpId = null;
        JSONObject result = new JSONObject();
        if(wechatHelpInfoEntity != null){
            helpId = wechatHelpInfoEntity.getHelpId();
            result.putAll(putMsg(true, "200", "获取成功"));
            result.put("helpId", helpId);
        }else{
            result.putAll(putMsg(false, "101", "获取失败"));
        }
        if(helpId != null){
            try{
                List<WechatHelpDetailUserVO> wechatHelpDetailUserVOList = wechatHelpDetailService.findHelpDetailUserListByHelpId(helpId);
                if(wechatHelpDetailUserVOList != null && wechatHelpDetailUserVOList.size() > 0){
                    result.put("data", JSONArray.parseArray(JSONArray.toJSONString(wechatHelpDetailUserVOList)));
                }
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 进行邀请好友助力，生成助力信息
     * @param sessionID
     * @param actId 活动id
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/execuHelp")
    public JSONObject execuHelp(String sessionID, String actId){
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        String wechatUserId = wechatApiSessionBean.getUserInfo().getWechatUserId();
        try{
            String helpId = UUIDUtil.getUUID();
            WechatHelpInfoEntity wechatHelpInfoEntity = wechatHelpInfoService.getHelpInfoByActId(actId, wechatUserId);
            if(wechatHelpInfoEntity != null){
                helpId = wechatHelpInfoEntity.getHelpId();
            }else{
                wechatHelpInfoService.insertHelp(helpId, actId, wechatUserId, wechatApiSessionBean.getUserInfo().getAppInfoId());
            }
            JSONObject result = putMsg(true, "200", "邀请助力成功");
            result.put("helpId", helpId);
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
     * 判断当前用户是否助力过 - 在整个活动中 ,并且领取奖励
     * @param sessionID
     * @param actId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/existDoHelpByActId")
    public JSONObject existDoHelpByActId(String sessionID, String actId, String helpId){
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        WechatUserInfoVO toUserInfo = wechatApiSessionBean.getUserInfo();
        try{
            WechatHelpDetailEntity wechatHelpDetailEntity = wechatHelpDetailService.getWechatHelpDetailEntityByToUser(actId, toUserInfo.getWechatUserId());
            if(wechatHelpDetailEntity != null){
                JSONObject result = putMsg(true, "200", "已助力");
                // 判断助力后是否领取奖励
                JSONObject commondJson = getRewardActCommandByOther(actId, toUserInfo.getWechatUserId(), wechatHelpDetailEntity);
                result.put("command", commondJson.get("command"));
                result.put("commandType", 2);
                return result;
            }else{
                WechatHelpInfoEntity helpInfoEntity = wechatHelpInfoService.getEntityById(helpId);
                if(helpInfoEntity != null){
                    if(helpInfoEntity.getWechatUserId().equals(toUserInfo.getWechatUserId())){
                        JSONObject result =  putMsg(false, "102", "不能给自己点助力");
//                        // 判断是否够人数领取奖励
//                        JSONObject commondJson = getRewardActCommand(helpId, helpInfoEntity, wechatApiSessionBean);
//                        result.put("command", commondJson.get("command"));
//                        result.put("commandType", 1);
                        return result;
                    }
                }
                return putMsg(false, "101", "未助力");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return putMsg(false, "103", "查询失败");
        }
    }

    /**
     * 判断当前用户是否助力过 - 在整个活动中 ,一个人可以多次点击
     * @param sessionID
     * @param helpId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/existDoHelpNoLimit")
    public JSONObject existDoHelpNoLimit(String sessionID, String helpId){
        logger.info("sessionID="+sessionID+"-----helpId="+helpId);
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        try{
            WechatHelpInfoEntity helpInfoEntity = wechatHelpInfoService.getEntityById(helpId);
            if(helpInfoEntity != null){
                WechatUserInfoVO fromUserInfo = wechatUserService.getUserInfoByUserIdAndAppInfoId(helpInfoEntity.getWechatUserId(), helpInfoEntity.getAppInfoId());
                WechatUserInfoVO toUserInfo = wechatApiSessionBean.getUserInfo();
                if(fromUserInfo.getWechatMapId().equals(toUserInfo.getWechatMapId())){
                    return putMsg(false, "101", "不能给自己点助力");
                }
                String helpDetailId = wechatHelpDetailService.getHelpDetailId(fromUserInfo.getWechatMapId(), toUserInfo.getWechatMapId(), helpId);
                if(helpDetailId != null){
                    return putMsg(true, "200", "已助力");
                }else{
                    return putMsg(false, "102", "未助力");
                }
            }else{
                return putMsg(false, "103", "助力记录不存在，请确认助力id是否正确");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return putMsg(false, "104", "查询失败");
        }
    }

    /**
     * 判断当前用户是否助力过
     * @param sessionID
     * @param helpId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/existDoHelp")
    public JSONObject existDoHelp(String sessionID, String helpId){
        logger.info("sessionID="+sessionID+"-----helpId="+helpId);
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        try{
            WechatHelpInfoEntity helpInfoEntity = wechatHelpInfoService.getEntityById(helpId);
            if(helpInfoEntity != null){
                WechatUserInfoVO fromUserInfo = wechatUserService.getUserInfoByUserIdAndAppInfoId(helpInfoEntity.getWechatUserId(), helpInfoEntity.getAppInfoId());
                WechatUserInfoVO toUserInfo = wechatApiSessionBean.getUserInfo();
                if(fromUserInfo.getWechatMapId().equals(toUserInfo.getWechatMapId())){
                    return putMsg(false, "101", "不能给自己点助力");
                }
                String helpDetailId = wechatHelpDetailService.getHelpDetailId(fromUserInfo.getWechatMapId(), toUserInfo.getWechatMapId(), helpId);
                if(helpDetailId != null){
                    return putMsg(true, "200", "已助力");
                }else{
                    WechatHelpDetailEntity wechatHelpDetailEntity = wechatHelpDetailService.getWechatHelpDetailEntityByToUser(helpInfoEntity.getActId(), toUserInfo.getWechatUserId());
                    if(wechatHelpDetailEntity != null){
                        return putMsg(true, "105", "已经给别人助力过了，不能再为这个点助力了");
                    }
                    return putMsg(false, "102", "未助力");
                }
            }else{
                return putMsg(false, "103", "助力记录不存在，请确认助力id是否正确");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return putMsg(false, "104", "查询失败");
        }
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
                return putMsg(false, "102", "助力点击记录失败，助力信息不存在");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return putMsg(false, "103", "助力点击记录失败");
        }
    }

    /**
     * 根据助力信息获取活动奖励口令 - 发起人领取
     * @param helpId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/getRewardActCommand")
    public JSONObject getRewardActCommand(String sessionID, String helpId){
        WechatHelpInfoEntity helpInfoEntity = wechatHelpInfoService.getEntityById(helpId);
        if(helpInfoEntity != null){
            WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
            return getRewardActCommand(helpId, helpInfoEntity, wechatApiSessionBean);
        }else{
            return putMsg(false, "104", "奖励口令获取失败，助力信息不存在。");
        }
    }

    private JSONObject getRewardActCommand(String helpId, WechatHelpInfoEntity helpInfoEntity, WechatApiSessionBean wechatApiSessionBean) {
        if(!wechatApiSessionBean.getUserInfo().getWechatUserId().equals(helpInfoEntity.getWechatUserId())){
            return putMsg(false, "105", "奖励口令获取失败,领取奖励必须是本人。");
        }
        //判断是否已经达到活动领取奖励的标准
        List<WechatHelpDetailEntity> helpDetailEntityList = wechatHelpDetailService.findHelpDetailListByHelpId(helpId);
        //TODO 需要动态获取领取奖励的条件
        int max = 5;
        if(helpDetailEntityList != null && helpDetailEntityList.size() >= max){
            //根据助力获取是否已经关联过奖励口令
            String command = rewardActCommandService.getCommandByHelpId(helpId, wechatApiSessionBean.getUserInfo().getWechatUserId());
            JSONObject result = new JSONObject();
            if(command == null){
                RewardActCommandEntity rewardActCommandEntity = null;
                //锁住奖励口令的读写
                try{
                    lock.lock();
                    rewardActCommandEntity = rewardActCommandService.getNextRewardActCommandByActId(helpInfoEntity.getActId(), 1);
                    if(rewardActCommandEntity != null){
                        rewardActCommandService.updateRewardActCommandUsed(rewardActCommandEntity.getCommandId());
                        result.put("success", true);
                        result.put("command", rewardActCommandEntity.getCommand());
                    }else{
                        return putMsg(false, "101", "奖励口令获取失败,奖励领取完毕。");
                    }
                }catch (Exception e){
                    logger.error(e.getMessage(), e);
                    return putMsg(false, "102", "奖励口令获取失败,发生未知错误。");
                }finally {
                    lock.unlock();
                }
                //插入奖励口令和助力的关联关系
                rewardActCommandService.insertRewardActCommandHelpMapping(rewardActCommandEntity, helpInfoEntity);
            }else{
                result.put("success", true);
                result.put("command", command);
            }
            return result;
        }else{
            return putMsg(false, "103", "奖励口令获取失败,未达到领取要求。");
        }
    }

    /**
     * 根据助力信息获取活动奖励口令 - 助力人领取
     * @param actId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/getRewardActCommandByOther")
    public JSONObject getRewardActCommandByOther(String sessionID, String actId){
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        String currentUserId = wechatApiSessionBean.getUserInfo().getWechatUserId();
        WechatHelpDetailEntity wechatHelpDetailEntity = wechatHelpDetailService.getWechatHelpDetailEntityByToUser(actId, currentUserId);
        return getRewardActCommandByOther(actId, currentUserId, wechatHelpDetailEntity);
    }

    private JSONObject getRewardActCommandByOther(String actId, String currentUserId, WechatHelpDetailEntity wechatHelpDetailEntity) {
        if(wechatHelpDetailEntity != null){
            //根据助力获取是否已经关联过奖励口令
            String command = rewardActCommandService.getCommandByHelpId(wechatHelpDetailEntity.getHelpId(), currentUserId);
            JSONObject result = new JSONObject();
            if(command == null){
                WechatHelpInfoEntity helpInfoEntity = wechatHelpInfoService.getEntityById(wechatHelpDetailEntity.getHelpId());
                if(helpInfoEntity==null){
                    return putMsg(false, "103", "奖励口令获取失败，助力信息不存在。");
                }
                RewardActCommandEntity rewardActCommandEntity = null;
                //锁住奖励口令的读写
                try{
                    lock.lock();
                    rewardActCommandEntity = rewardActCommandService.getNextRewardActCommandByActId(actId, 2);
                    if(rewardActCommandEntity != null){
                        rewardActCommandService.updateRewardActCommandUsed(rewardActCommandEntity.getCommandId());
                        result.put("success", true);
                        result.put("command", rewardActCommandEntity.getCommand());
                    }else{
                        return putMsg(false, "101", "奖励口令获取失败,奖励领取完毕。");
                    }
                    helpInfoEntity.setWechatUserId(currentUserId);
                    //插入奖励口令和助力的关联关系
                    rewardActCommandService.insertRewardActCommandHelpMapping(rewardActCommandEntity, helpInfoEntity);
                }catch (Exception e){
                    logger.error(e.getMessage(), e);
                    return putMsg(false, "102", "奖励口令获取失败,发生未知错误。");
                }finally {
                    lock.unlock();
                }
            }else{
                result.put("success", true);
                result.put("command", command);
            }
            return result;
        }else{
            return putMsg(false, "104", "奖励口令获取失败，没有进行助力，无法领取奖励。");
        }
    }

}
