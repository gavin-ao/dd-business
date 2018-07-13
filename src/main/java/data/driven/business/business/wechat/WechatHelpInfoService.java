package data.driven.business.business.wechat;

import data.driven.business.entity.wechat.WechatHelpInfoEntity;

/**
 * 助力service
 * @author hejinkai
 * @date 2018/7/13
 */
public interface WechatHelpInfoService {
    /**
     * 新增助力
     * @param helpId
     * @param actId
     * @param wechatUserId
     * @param appInfoId
     */
    public String insertHelp(String helpId, String actId, String wechatUserId, String appInfoId);

    /**
     * 根据助力id获取助力信息
     * @param helpId
     * @return
     */
    public WechatHelpInfoEntity getEntityById(String helpId);

    /**
     * 根据活动id和用户id获取助力信息
     * @param actId
     * @param wechatUserId
     * @return
     */
    public WechatHelpInfoEntity getHelpInfoByActId(String actId, String wechatUserId);
}
