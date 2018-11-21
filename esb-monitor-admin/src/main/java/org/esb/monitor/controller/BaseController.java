package org.esb.monitor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public abstract class BaseController {
    private Logger logger = Logger.getLogger(BaseController.class);

    @Autowired
    protected HttpServletRequest request;

    protected Object getSessionAttr(String attr) {
        HttpSession session = request.getSession();
        return session.getAttribute(attr);
    }


    protected void setSessionAttr(String attrName, Object attr) {
        request.getSession().setAttribute(attrName, attr);
    }

    protected void clearSession() {
        request.getSession().invalidate();
    }

    public String getPara(String paraName) {
        return request.getParameter(paraName);
    }


    public String getContextPath() {
        return request.getContextPath();
    }


}

