package data.driven.business.business.system.impl;

import data.driven.business.business.system.UserInfoService;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.util.UUIDUtil;
import data.driven.business.vo.system.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author hejinkai
 * @date 2018/6/28
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public UserInfoVO getUserInfoByOpenId(String openId) {
        String sql = "select u.user_info_id,u.nick_name,u.gender,u.language,u.city,u.province,u.country,u.avatar_url,u.union_id,u.create_at,u.open_id from sys_user_info u" +
                " left join sys_app_user_mapping aum on aum.user_info_id = u.user_info_id" +
                " where aum.open_id = ?";
        List<UserInfoVO> list = jdbcBaseDao.queryList(UserInfoVO.class, sql, openId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public UserInfoVO getUserInfoByUnionId(String unionId) {
        String sql = "select u.user_info_id,u.nick_name,u.gender,u.language,u.city,u.province,u.country,u.avatar_url,u.union_id,u.create_at from sys_user_info u" +
                " where u.union_id = ?";
        List<UserInfoVO> list = jdbcBaseDao.queryList(UserInfoVO.class, sql, unionId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public String addUserInfo(UserInfoVO userInfo) {
        String userInfoId = UUIDUtil.getUUID();
        userInfo.setUserInfoId(userInfoId);
        userInfo.setCreateAt(new Date());
        jdbcBaseDao.insert(userInfo, "sys_user_info");
        return userInfoId;
    }

    @Override
    public String addUserAndAppMap(String appInfoId, String userInfoId, String openId) {
        String id = UUIDUtil.getUUID();
        String sql = "inser into (sys_map_id,app_info_id,user_info_id,open_id,create_at) values(?,?,?,?,?)";
        jdbcBaseDao.executeUpdate(sql, id,appInfoId, userInfoId, openId, new Date());
        return id;
    }
}
