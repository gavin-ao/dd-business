package data.driven.business.business.material;

import data.driven.business.vo.material.MatActivityVO;

/**
 * 活动-素材service
 * @author hejinkai
 * @date 2018/7/15
 */
public interface MatActivityService {

    /**
     * 根据活动id获取图片的url
     * @param actId
     * @return
     */
    public String getMatActivityPictureUrl(String actId);

    /**
     * 根据活动id获取图片的url，活动规则，兑换规则
     * @param actId
     * @return
     */
    public MatActivityVO getMatActivityInfo(String actId);
    /**
     * 根据活动id获取图片的url，活动规则，兑换规则
     * @param appInfoId
     * @return
     */
    public MatActivityVO getMatActivityInfoByApp(String appInfoId);

}
