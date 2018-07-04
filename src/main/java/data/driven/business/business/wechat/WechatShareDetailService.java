package data.driven.business.business.wechat;

import data.driven.business.entity.wechat.WechatShareInfoEntity;
import data.driven.business.vo.wechat.WechatUserInfoVO;

/**
 * 分享详情
 * @author hejinkai
 * @date 2018/7/4
 */
public interface WechatShareDetailService {

    /**
     * 分享详情，如果是多次点击分享链接，就累计数据即可
     * @param fromUserInfo
     * @param toUserInfo
     * @param shareInfo
     * @return
     */
    public String insertShareDetail(WechatUserInfoVO fromUserInfo, WechatUserInfoVO toUserInfo, WechatShareInfoEntity shareInfo);

}
