package org.esb.monitor.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class UserSession implements Serializable {

    private static final long serialVersionUID = 7089581925813056601L;
    private String username;
    private String userid;
    private String deptName;
    private String deptId;
    private String password;
    private String userTheme = "metro-default";
    private Map<String, String> extend = new HashMap<String, String>();


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserTheme() {
        return userTheme;
    }

    public void setUserTheme(String userTheme) {
        this.userTheme = userTheme;
    }

    public String getExtend(String extendName) {
        if (extend != null)
            return extend.get(extendName);
        else
            return null;
    }


}
