package data.driven.business.controller.wechatapi.nologin;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.material.MatActivityService;
import data.driven.business.common.Constant;
import data.driven.business.controller.wechatapi.WechatShareApiController;
import data.driven.business.vo.material.MatActivityVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static data.driven.business.util.JSONUtil.putMsg;

/**
 * @author hejinkai
 * @date 2018/7/15
 */
@Controller
@RequestMapping(path = "/wechatapi/nologin/activity")
public class MatActivityApiNoLoginController {
    private static final Logger logger = LoggerFactory.getLogger(WechatShareApiController.class);

    @Autowired
    private MatActivityService matActivityService;

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
        if(matActivityInfo != null){
            JSONObject result = putMsg(true, "200", "获取成功");
            result.put("url", Constant.STATIC_FILE_PATH + matActivityInfo.getFilePath());
            result.put("actRule", matActivityInfo.getActRule());
            result.put("exchangeRule", matActivityInfo.getExchangeRule());
            result.put("partakeNum", matActivityInfo.getPartakeNum());
            result.put("actShareTitle", matActivityInfo.getActShareTitle());
            result.put("actTitle", matActivityInfo.getActTitle());
            result.put("actReply", matActivityInfo.getActReply());
            return result;
        }else{
            return putMsg(false, "101", "获取失败");
        }
    }

}
