package data.driven.business.component;

import data.driven.business.common.SessionBean;
import data.driven.business.common.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤非法请求或者失效请求
 * @author hejinkai
 * @date 2018/6/27
 */
public class AuthorityFilter implements Filter{

    private static Logger logger = LoggerFactory.getLogger(AuthorityFilter.class);

    /** 静态资源文件不拦截 **/
    private static final String STATIC_FOLDER = "/static";
    /** 静态资源文件不拦截 **/
    private static final String LOGIN_PATH = "/login";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("AuthorityFilter init ...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        if(uri.startsWith(LOGIN_PATH) || uri.startsWith(STATIC_FOLDER)){
            filterChain.doFilter(request, response);
        }else{
            String sessionID = request.getParameter("sessionID");
            if(sessionID == null){
                noAuthority(request);
            }
            SessionBean sessionBean = UserSession.getSessionBean(sessionID);
            if(sessionBean != null && sessionBean.getUserInfo() != null && sessionBean.getUserInfo().getUserInfoId() != null){
                filterChain.doFilter(request, response);
            }else{
                filterChain.doFilter(request, response);
            }
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
