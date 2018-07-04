package data.driven.business.business.wechat;

import data.driven.business.entity.wechat.WechatShareInfoEntity;

/**
 * 分享Service
 * @author hejinkai
 * @date 2018/6/27
 */
public interface WechatShareInfoService {

    /**
     * 新增分享内容
     * @param wechatUserId
     * @param content
     * @param appInfoId
     */
    public String insertShare(String wechatUserId, String content, String appInfoId);

    /**
     * 根据分享id获取分享信息
     * @param shareId
     * @return
     */
    public WechatShareInfoEntity getEntityById(String shareId);
}
