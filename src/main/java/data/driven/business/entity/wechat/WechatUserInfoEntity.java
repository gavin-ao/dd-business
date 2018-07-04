package data.driven.business.entity.wechat;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * 微信用户信息
 * @author hejinkai
 * @date 2018/6/26
 */
public class WechatUserInfoEntity {

    public static void main(String[] args){
        String data = "{\"nickName\":\"Band\",\"gender\":1,\"language\":\"zh_CN\",\"city\":\"Guangzhou\",\"province\":\"Guangdong\",\"country\":\"CN\",\"avatarUrl\":\"http:wx.qlogo.cn/mmopen/vi_32/aSKcBBPpibyKNicHNTMM0qJVh8Kjgiak2AHWr8MHM4WgMEm7GFhsf8OYrySdbvAMvTsw3mo8ibKicsnfN5pRjl1p8HQ/0\",\"unionId\":\"ocMvos6NjeKLIBqg5Mr9QjxrP1FA\",\"watermark\":{\"timestamp\":1477314187,\"appid\":\"wx4f4bc4dec97d474b\"}}";
        System.out.println(data);
        WechatUserInfoEntity u = JSONObject.parseObject(data, WechatUserInfoEntity.class);
        System.out.println(JSONObject.toJSONString(u));

    }

    /** 用户主键 **/
    private String wechatUserId;
    /** 用户昵称 **/
    private String nickName;
    /** gender **/
    private Integer gender;
    /** language **/
    private String language;
    /** 城市 **/
    private String city;
    /** 省份 **/
    private String province;
    /** 国家 **/
    private String country;
    /** 用户头像 **/
    private String avatarUrl;
    /** 用户微信平台唯一标识 **/
    private String unionId;
    /** 创建时间 **/
    private Date createAt;

    public String getWechatUserId() {
        return wechatUserId;
    }

    public void setWechatUserId(String wechatUserId) {
        this.wechatUserId = wechatUserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
