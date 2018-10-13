package data.driven.business.controller.wechatapi.nologin;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.material.BtnCopywritingService;
import data.driven.business.business.material.MatActivityService;
import data.driven.business.business.wechat.WechatAppInfoService;
import data.driven.business.common.Constant;
import data.driven.business.entity.wechat.WechatAppInfoEntity;
import data.driven.business.util.JSONUtil;
import data.driven.business.vo.material.MatActivityVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static data.driven.business.util.JSONUtil.putMsg;

/**
 * @author hejinkai
 * @date 2018/7/15
 */
@Controller
@RequestMapping(path = "/wechatapi/nologin/activity")
public class MatActivityApiNoLoginController {
    private static final Logger logger = LoggerFactory.getLogger(MatActivityApiNoLoginController.class);

    @Autowired
    private MatActivityService matActivityService;
    @Autowired
    private WechatAppInfoService wechatAppInfoService;
    @Autowired
    private BtnCopywritingService btnCopywritingService;

    @ResponseBody
    @RequestMapping(path = "/getActivityPicture")
    public JSONObject getActivityPicture(String actId){
        String filePath = matActivityService.getMatActivityPictureUrl(actId);
        if(filePath != null){
            JSONObject result = putMsg(true, "200", "获取成功");
            result.put("url", Constant.STATIC_FILE_PATH + filePath);
            return result;
        }else{
            return putMsg(false, "101", "获取失败");
        }
    }

    @ResponseBody
    @RequestMapping(path = "/getActivityInfo")
    public JSONObject getActivityInfo(String actId){
        MatActivityVO matActivityInfo = matActivityService.getMatActivityInfo(actId);
        return putActivityInfoJson(matActivityInfo);
    }

    /**
     * 根据appid获取当前有效的任务
     * @param appid
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/getCurrentActivityInfo")
    public JSONObject getCurrentActivityInfo(String appid){
        WechatAppInfoEntity appInfoEntity = wechatAppInfoService.getAppInfo(appid);
        if(appInfoEntity == null){
            return JSONUtil.putMsg(false, "102", "小程序为空");
        }
        MatActivityVO matActivityInfo = matActivityService.getMatActivityInfoByApp(appInfoEntity.getAppInfoId());
        return putActivityInfoJson(matActivityInfo);
    }

    /**
     * 根据活动信息，设置有效信息返回
     * @param matActivityInfo
     * @return
     */
    private JSONObject putActivityInfoJson(MatActivityVO matActivityInfo) {
        if(matActivityInfo != null){
            JSONObject result = putMsg(true, "200", "获取成功");
            result.put("url", Constant.STATIC_FILE_PATH + matActivityInfo.getFilePath());
            result.put("actId", matActivityInfo.getActId());
            result.put("actRule", matActivityInfo.getActRule());
            result.put("exchangeRule", matActivityInfo.getExchangeRule());
            result.put("partakeNum", matActivityInfo.getPartakeNum());
            result.put("actShareTitle", matActivityInfo.getActShareTitle());
            result.put("actShareCopywriting", matActivityInfo.getActShareCopywriting());
            result.put("actTitle", matActivityInfo.getActTitle());
            result.put("actReply", matActivityInfo.getActReply());
            Map<String, String> btnCopyWritingMap = btnCopywritingService.findBtnCopyWritingMapByActId(matActivityInfo.getActId());
            result.put("btnText", btnCopyWritingMap);
            return result;
        }else{
            return putMsg(false, "101", "获取失败");
        }
    }

    /**
     * 根据actId获取活动按钮文案
     * @param actId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/getBtnCopywritingMap")
    public JSONObject getBtnCopywritingMap(String actId){
        Map<String, String> btnCopyWritingMap = btnCopywritingService.findBtnCopyWritingMapByActId(actId);
        if(btnCopyWritingMap != null && btnCopyWritingMap.size() > 0){
            JSONObject result = putMsg(true, "200", "获取成功");
            result.putAll(btnCopyWritingMap);
            return result;
        }else{
            return putMsg(false, "101", "获取失败");
        }
    }

}
