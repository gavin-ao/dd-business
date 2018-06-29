package data.driven.business.common;

import data.driven.business.vo.system.UserInfoVO;

/**
 * redis缓存的session实体内容
 * @author hejinkai
 * @date 2018/6/27
 */
public class SessionBean {
    /** sessionKey **/
    private String sessionKey;
    /** sessionKey **/
    private UserInfoVO userInfo;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public UserInfoVO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoVO userInfo) {
        this.userInfo = userInfo;
    }
}
