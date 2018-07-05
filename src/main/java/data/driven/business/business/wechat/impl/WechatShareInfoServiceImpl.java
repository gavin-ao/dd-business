package data.driven.business.business.wechat.impl;

import data.driven.business.business.wechat.WechatShareInfoService;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.entity.wechat.WechatShareInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author hejinkai
 * @date 2018/7/4
 */
@Service
public class WechatShareInfoServiceImpl implements WechatShareInfoService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public String insertShare(String shareId, String wechatUserId, String content, String appInfoId) {
        Date createAt = new Date();
        String sql = "insert into wechat_share_info(share_id,wechat_user_id,content,app_info_id,create_at) values(?,?,?,?,?)";
        jdbcBaseDao.executeUpdate(sql, shareId, wechatUserId, content, appInfoId, createAt);
        return shareId;
    }

    @Override
    public WechatShareInfoEntity getEntityById(String shareId) {
        String sql = "select share_id,wechat_user_id,content,app_info_id,create_at from wechat_share_info where share_id = ?";
        List<WechatShareInfoEntity> list = jdbcBaseDao.queryList(WechatShareInfoEntity.class, sql, shareId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
