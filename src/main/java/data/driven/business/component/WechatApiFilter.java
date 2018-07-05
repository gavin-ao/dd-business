package data.driven.business.component;

import data.driven.business.common.WechatApiSession;
import data.driven.business.common.WechatApiSessionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 过滤微信的非法请求或者失效请求
 * @author hejinkai
 * @date 2018/6/27
 */
public class WechatApiFilter implements Filter{

    private static Logger logger = LoggerFactory.getLogger(WechatApiFilter.class);

    /** 不过滤的url集合 **/
    public static Set<String> EXCLUDE_URL_SET = new HashSet<String>();

    static{
        //静态资源文件不需要过滤
        EXCLUDE_URL_SET.add("/static");
        //微信登录和同步接口不需要过滤
        EXCLUDE_URL_SET.add("/wechatapi/service/");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("WechatApiFilter init ...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        if(canFilter(uri)){
            String sessionID = request.getParameter("sessionID");
            if(sessionID == null){
                noAuthority(request);
                return;
            }else{

                WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
                if(wechatApiSessionBean == null || wechatApiSessionBean.getUserInfo() == null || wechatApiSessionBean.getUserInfo().getWechatUserId()== null){
                    noAuthority(request);
                    return;
                }
            }
        }
        System.out.println("------WechatApiFilter过滤器通过成功-------");
        filterChain.doFilter(request, response);


    }

    /**
     * 无权限请求处理
     * @param request
     */
    private void noAuthority(HttpServletRequest request){
        request.setAttribute("success", false);
        request.setAttribute("msg", "noAuthority");
    }
    /**
     * 判断该次请求是否需要过滤
     * @return  需要过滤 true， 不需要过滤 - false
     */
    public boolean canFilter(String uri){
        for(String url : EXCLUDE_URL_SET){
            if(uri.startsWith(url)){
                return false;
            }
        }
        return true;
    }


    @Override
    public void destroy() {

    }
}
