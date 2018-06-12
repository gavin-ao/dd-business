package data.driven.business.controller.tourism;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.tourism.ScenicSpotService;
import data.driven.business.entity.tourism.ScenicSpotEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 景区controller
 * @author 何晋凯
 * @date 2018/06/06
 */
@Controller
@RequestMapping(path = "/tourism/scenic-spot")
public class ScenicSpotController {
    private static final Logger logger = LoggerFactory.getLogger(ScenicSpotController.class);

    @Autowired
    private ScenicSpotService scenicSpotService;

    @ResponseBody
    @RequestMapping(path = "/getScenicSpotJson")
    public JSONObject getScenicSpot(String scenicSpotId){
        JSONObject result = new JSONObject();
        ScenicSpotEntity scenicSpot = scenicSpotService.getScenicSpot(scenicSpotId);
        if(scenicSpot != null){
            result.put("success", true);
            result.put("msg", scenicSpot.getScenicSpotName());
        }else{
            result.put("success", false);
            result.put("msg", "查询的景区信息为空");
        }
        return result;
    }

}
