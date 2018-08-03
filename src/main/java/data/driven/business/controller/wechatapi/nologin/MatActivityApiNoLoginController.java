package data.driven.business.controller.wechatapi.nologin;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.material.MatActivityService;
import data.driven.business.common.Constant;
import data.driven.business.controller.wechatapi.WechatShareApiController;
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
        long start = System.currentTimeMillis();
        String filePath = matActivityService.getMatActivityPictureUrl(actId);
        if(filePath != null){
            JSONObject result = putMsg(true, "200", "获取成功");
            result.put("url", Constant.STATIC_FILE_PATH + filePath);
            long end = System.currentTimeMillis();
            logger.warn("耗时："+(end-start)/1000.0 + "秒");
            return result;
        }else{
            return putMsg(false, "101", "获取失败");
        }
    }

}
