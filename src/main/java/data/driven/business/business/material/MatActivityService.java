package data.driven.business.business.material;

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

}
