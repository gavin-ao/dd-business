package data.driven.business.vo.tourism;

import data.driven.business.entity.wechat.UserInfoEntity;

/**
 * 用户信息vo，包含openId
 * @author hejinkai
 * @date 2018/6/27
 */
public class UserInfoVO extends UserInfoEntity {
    /** 用户对应小程序在微信中的唯一标识 **/
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 清楚敏感信息
     */
    public void clearSensitiveInfo(){
        setOpenId(null);
        setUnionId(null);
    }
}
