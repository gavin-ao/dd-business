package data.driven.business.business.wechat;

import data.driven.business.vo.tourism.UserInfoVO;

/**
 * 微信用户信息service
 * @author hejinkai
 * @date 2018/6/27
 */
public interface WechatUserService {

    /**
     * 根据openId 查询用户信息
     * @param openId
     * @return
     */
    public UserInfoVO getUserInfoByOpenId(String openId);

    /**
     * 根据unionId查询用户信息
     * @param unionId
     * @return
     */
    public UserInfoVO getUserInfoByUnionId(String unionId);

    /**
     * 新增用户- 返回userId
     * @param userInfo
     * @return
     */
    public String addUserInfo(UserInfoVO userInfo);

    /**
     * 新增app与用户的关联
     * @param appInfoId
     * @param userInfoId
     * @param openId
     * @return
     */
    public String addUserAndAppMap(String appInfoId, String userInfoId, String openId);
}
