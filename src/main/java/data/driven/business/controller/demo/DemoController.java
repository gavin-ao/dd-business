package data.driven.business.controller.demo;

import com.alibaba.fastjson.JSONObject;
import data.driven.business.business.demo.DemoService;
import data.driven.business.component.Page;
import data.driven.business.component.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 测试
 * @author 何晋凯
 * @date 2018/06/04
 */
@Controller
@RequestMapping(path = "/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @RequestMapping(path = "/queryPage")
    public ModelAndView queryPage(PageBean pageBean){
        if(pageBean == null || pageBean.getPageNo() == null || pageBean.getPageNo() < 1){
            pageBean = new PageBean();
        }
        ModelAndView modelAndView = new ModelAndView("demo/demo");
        Page page = demoService.findPage(pageBean);
        modelAndView.addObject("pageList", page.getResult());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(path = "/queryPageJson")
    public JSONObject queryPageJson(PageBean pageBean){
        if(pageBean == null || pageBean.getPageNo() == null || pageBean.getPageNo() < 1){
            pageBean = new PageBean();
        }
        JSONObject result = new JSONObject();
        Page page = demoService.findPage(pageBean);
        result.put("success", true);
        result.put("data", page);
        return result;
    }
    @ResponseBody
    @RequestMapping(path = "/insert")
    public JSONObject insert(String userName){
        JSONObject result = new JSONObject();
        boolean bl = demoService.insert(userName);
        result.put("success", bl);
        return result;
    }

}
