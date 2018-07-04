package data.driven.business.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.wechat.WechatTotalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hejinkai
 * @date 2018/7/4
 */
@RestController
@RequestMapping("/wechat/total")
public class WechatTotalController {

    private static final Logger logger = LoggerFactory.getLogger(WechatTotalController.class);

    @Autowired
    private WechatTotalService wechatTotalService;

    /**
     * 根据时间范围统计活跃度
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(path = "/totalActivityNum")
    public JSONObject totalActivityNum(String appInfoId, String startDate, String endDate){
        System.out.println("tijiaoceshidaima");
        return wechatTotalService.totalActivityNum(appInfoId, startDate, endDate);
    }

    /**
     * 根据时间范围统计活跃度，返回数据走势图
     * @param appInfoId
     * @param startDate
     * @param endDate
     * @return
     */
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
    @RequestMapping(path = "/totalSpreadTrajectory")
    public JSONObject totalSpreadTrajectory(String appInfoId, String startDate, String endDate){
        return wechatTotalService.totalSpreadTrajectory(appInfoId, startDate, endDate);
    }


}
