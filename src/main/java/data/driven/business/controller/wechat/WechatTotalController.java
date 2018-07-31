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
    @RequestMapping(path = "/coreData")
    public ModelAndView test(String appInfoId, String startDate, String endDate){
        ModelAndView mv = new ModelAndView("/data-statistics/core-data");
        dealTotalAll("activityNum", mv, wechatTotalService.totalActivityNum(appInfoId, startDate, endDate));
        dealTotalAll("spreadRangeNum", mv, wechatTotalService.totalSpreadRangeNum(appInfoId, startDate, endDate));
        dealTotalAll("fissionEffectNewPeopleNum", mv, wechatTotalService.totalFissionEffectNewPeopleNum(appInfoId, startDate, endDate));
        dealTotalAll("shareNum", mv, wechatTotalService.totalShareNum(appInfoId, startDate, endDate));
        dealTotalAll("sharePeopleNum", mv, wechatTotalService.totalSharePeopleNum(appInfoId, startDate, endDate));
        return mv;
    }

    /**
     * 处理json - totalAll
     * @param key
     * @param mv
     * @param temp
     */
    private void dealTotalAll(String key,  ModelAndView mv, JSONObject temp){
        if(temp.getBoolean("success")){
            mv.addObject(key, temp.getInteger("countNum"));
        }else{
            mv.addObject(key, 0);
        }
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
     * @param type  统计方式  A-C 在 B-A之前， 为0时按照A-C和B独立出，为1时按照A-C和B-A出现
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/totalSpreadTrajectory")
    public JSONObject totalSpreadTrajectory(String appInfoId, String startDate, String endDate, Integer type){
        return wechatTotalService.totalSpreadTrajectory(appInfoId, startDate, endDate, type);
    }


}
