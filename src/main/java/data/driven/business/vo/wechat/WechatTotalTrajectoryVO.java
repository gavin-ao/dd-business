package data.driven.business.vo.wechat;

import java.util.Date;

/**
 * 用于微信统计 - 轨迹图
 * @author hejinkai
 * @date 2018/7/18
 */
public class WechatTotalTrajectoryVO {
    private String fromUser;
    private String toUser;
    private Date totalDate;
    private Integer frequency;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public Date getTotalDate() {
        return totalDate;
    }

    public void setTotalDate(Date totalDate) {
        this.totalDate = totalDate;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
    public void setFrequency(Long frequency) {
        this.frequency = frequency.intValue();
    }
}
