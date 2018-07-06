package data.driven.business.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.wechat.WechatTotalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author hejinkai
 * @date 2018/7/4
 */
@Controller
@RequestMapping("/wechat/total")
public class WechatTotalController {

    private static final Logger logger = LoggerFactory.getLogger(WechatTotalController.class);

    @Autowired
    private WechatTotalService wechatTotalService;


    @RequestMapping(value = "/dataStatistics")
    public ModelAndView productManage(){
        ModelAndView mv = new ModelAndView("/data-statistics/index");
        return mv;
    }

    /**
     * 统计上面五个指标
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalAll")
    public JSONObject totalAll(String appInfoId, String startDate, String endDate){
        JSONObject result = new JSONObject();
        dealTotalAll("activityNum", result, wechatTotalService.totalActivityNum(appInfoId, startDate, endDate));
        dealTotalAll("spreadRangeNum", result, wechatTotalService.totalSpreadRangeNum(appInfoId, startDate, endDate));
        dealTotalAll("fissionEffectNewPeopleNum", result, wechatTotalService.totalFissionEffectNewPeopleNum(appInfoId, startDate, endDate));
        dealTotalAll("shareNum", result, wechatTotalService.totalShareNum(appInfoId, startDate, endDate));
        dealTotalAll("sharePeopleNum", result, wechatTotalService.totalSharePeopleNum(appInfoId, startDate, endDate));
        result.put("success", true);
        return result;
    }

    /**
     * 处理json - totalAll
     * @param key
     * @param result
     * @param temp
     */
    private void dealTotalAll(String key, JSONObject result, JSONObject temp){
        if(temp.getBoolean("success")){
            result.put(key, temp.getInteger("countNum"));
        }else{
            result.put(key, 0);
        }
    }

    /**
     * 根据时间范围统计活跃度
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalActivityNum")
    public JSONObject totalActivityNum(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalActivityNum(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计活跃度，返回数据走势图
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalActivityNumView")
    public JSONObject totalActivityNumView(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalActivityNumView(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计传播范围
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalSpreadRangeNum")
    public JSONObject totalSpreadRangeNum(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalSpreadRangeNum(appInfoId, startDate, endDate);
    }
    /**
     * 根据时间范围统计传播范围，返回数据走势图
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalSpreadRangeNumView")
    public JSONObject totalSpreadRangeNumView(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalSpreadRangeNumView(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计裂变效果新增人数
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalFissionEffectNewPeopleNum")
    public JSONObject totalFissionEffectNewPeopleNum(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalFissionEffectNewPeopleNum(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计裂变效果新增人数，返回数据走势图
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalFissionEffectNewPeopleNumView")
    public JSONObject totalFissionEffectNewPeopleNumView(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalFissionEffectNewPeopleNumView(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计分享次数
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalShareNum")
    public JSONObject totalShareNum(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalShareNum(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计分享次数，返回数据走势图
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalShareNumView")
    public JSONObject totalShareNumView(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalShareNumView(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计分享人数
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalSharePeopleNum")
    public JSONObject totalSharePeopleNum(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalSharePeopleNum(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计分享人数，返回数据走势图
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalSharePeopleNumView")
    public JSONObject totalSharePeopleNumView(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalSharePeopleNumView(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计新老用户占比
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalOldAndNewUser")
    public JSONObject totalOldAndNewUser(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalOldAndNewUser(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计传播轨迹
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalSpreadTrajectory")
    public JSONObject totalSpreadTrajectory(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalSpreadTrajectory(appInfoId, startDate, endDate);
    }


}
