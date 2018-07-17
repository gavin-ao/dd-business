package data.driven.business.controller.wechatapi.nologin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.wechat.WechatHelpDetailService;
import data.driven.business.business.wechat.WechatHelpInfoService;
import data.driven.business.business.wechat.WechatUserService;
import data.driven.business.controller.wechatapi.WechatShareApiController;
import data.driven.business.vo.wechat.WechatHelpDetailUserVO;
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
@RequestMapping(path = "/wechatapi/nologin/help")
public class WechatHelpApiNoLoginController {
    private static final Logger logger = LoggerFactory.getLogger(WechatShareApiController.class);

    @Autowired
    private WechatHelpInfoService wechatHelpInfoService;
    @Autowired
    private WechatHelpDetailService wechatHelpDetailService;
    @Autowired
    private WechatUserService wechatUserService;


    /**
     * 获取助力的微信用户信息集合
     * @param helpId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/findHelpDetailUserList")
    public JSONObject findHelpDetailUserList(String helpId){
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
