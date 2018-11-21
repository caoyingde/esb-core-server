package org.esb.monitor.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SecurityInterceptor implements HandlerInterceptor {

    private List<String> excludeRequst;

    @Override
    public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        request.setAttribute("root", request.getContextPath());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
    	System.out.println(">>>Security");
//        String pageurl = request.getRequestURI();
//        AntPathMatcher matcher = new AntPathMatcher();
//        boolean needLogin = true;
//        /**
//         * 判断
//         */
//        for (String noAuthUrl : excludeRequst) {
//            if (matcher.match("/**/" + noAuthUrl, pageurl)) {
//                needLogin = false;   // 无需判断是否登录
//                break;
//            }
//        }
//        if (needLogin) {  // 添加判断是否需要判断登陆，如果无需这避免执行 getSession方法，这样就不会创建session
//            HttpSession session = request.getSession();
//            boolean login = session.getAttribute("UID") != null;
//            if (login) {
//                return true;
//            } else {
//                session.invalidate();
//                response.getWriter().write("<script>parent.window.location.href=\"" + request.getContextPath() + "/login.html\";</script>");
//                return false;
//            }
//        } else {
//            return true;
//        }

        return true;
    }

    public List<String> getExcludeRequst() {
        return excludeRequst;
    }

    public void setExcludeRequst(List<String> excludeRequst) {
        this.excludeRequst = excludeRequst;
    }


}
