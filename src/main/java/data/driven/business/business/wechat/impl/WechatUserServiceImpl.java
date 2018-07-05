package data.driven.business.business.wechat.impl;

import data.driven.business.business.wechat.WechatUserService;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.entity.wechat.WechatUserInfoEntity;
import data.driven.business.util.UUIDUtil;
import data.driven.business.vo.wechat.WechatUserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author hejinkai
 * @date 2018/6/28
 */
@Service
public class WechatUserServiceImpl implements WechatUserService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public WechatUserInfoVO getUserInfoByOpenId(String openId) {
        String sql = "select u.wechat_user_id,u.nick_name,u.gender,u.language,u.city,u.province,u.country,u.avatar_url,u.union_id,u.create_at,aum.open_id,aum.app_info_id,aum.wechat_map_id from wechat_user_info u" +
                " left join wechat_app_user_mapping aum on aum.wechat_user_id = u.wechat_user_id" +
                " where aum.open_id = ?";
        List<WechatUserInfoVO> list = jdbcBaseDao.queryList(WechatUserInfoVO.class, sql, openId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public WechatUserInfoVO getUserInfoByUnionId(String unionId) {
        String sql = "select u.wechat_user_id,u.nick_name,u.gender,u.language,u.city,u.province,u.country,u.avatar_url,u.union_id,u.create_at from wechat_user_info u" +
                " where u.union_id = ?";
        List<WechatUserInfoVO> list = jdbcBaseDao.queryList(WechatUserInfoVO.class, sql, unionId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public String addUserInfo(WechatUserInfoVO userInfo) {
        String userInfoId = UUIDUtil.getUUID();
        WechatUserInfoEntity newUserInfo = new WechatUserInfoEntity();
        BeanUtils.copyProperties(userInfo, newUserInfo);
        newUserInfo.setWechatUserId(userInfoId);
        newUserInfo.setCreateAt(new Date());
        jdbcBaseDao.insert(newUserInfo, "wechat_user_info");
        return userInfoId;
    }

    @Override
    public String addUserAndAppMap(String appInfoId, String userInfoId, String openId) {
        String id = UUIDUtil.getUUID();
        String sql = "inser into (wechat_map_id,app_info_id,wechat_user_id,open_id,create_at) values(?,?,?,?,?)";
        jdbcBaseDao.executeUpdate(sql, id,appInfoId, userInfoId, openId, new Date());
        return id;
    }

    @Override
    public WechatUserInfoVO getUserInfoByUserIdAndAppInfoId(String userInfoId, String appInfoId) {
        String sql = "select u.wechat_user_id,u.nick_name,u.gender,u.language,u.city,u.province,u.country,u.avatar_url,u.union_id,u.create_at,u.open_id,aum.app_info_id,aum.wechat_map_id from wechat_user_info u" +
                " left join wechat_app_user_mapping aum on aum.wechat_user_id = u.wechat_user_id" +
                " where u.wechat_user_id = ? and aum.app_info_id = ?";
        List<WechatUserInfoVO> list = jdbcBaseDao.queryList(WechatUserInfoVO.class, sql, userInfoId, appInfoId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
