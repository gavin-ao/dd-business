package data.driven.business.component;

import data.driven.business.common.WechatApiSessionBean;
import data.driven.business.common.WechatApiSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤微信的非法请求或者失效请求
 * @author hejinkai
 * @date 2018/6/27
 */
public class WechatApiFilter implements Filter{

    private static Logger logger = LoggerFactory.getLogger(WechatApiFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("WechatApiFilter init ...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();

        String sessionID = request.getParameter("sessionID");
        if(sessionID == null){
            noAuthority(request);
        }
        WechatApiSessionBean wechatApiSessionBean = WechatApiSession.getSessionBean(sessionID);
        if(wechatApiSessionBean != null && wechatApiSessionBean.getUserInfo() != null && wechatApiSessionBean.getUserInfo().getUserInfoId() != null){
            filterChain.doFilter(request, response);
        }else{
            filterChain.doFilter(request, response);
        }

    }

    /**
     * 无权限请求处理
     * @param request
     */
    private void noAuthority(HttpServletRequest request){
        request.setAttribute("success", false);
        request.setAttribute("msg", "noAuthority");
    }

    @Override
    public void destroy() {

    }
}
