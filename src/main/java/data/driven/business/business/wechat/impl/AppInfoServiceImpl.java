package data.driven.business.business.wechat.impl;

import data.driven.business.business.wechat.AppInfoService;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.entity.wechat.AppInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hejinkai
 * @date 2018/6/28
 */
@Service
public class AppInfoServiceImpl implements AppInfoService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public AppInfoEntity getAppInfo(String appid) {
        String sql = "select app_info_id,app_name,appid,secret,create_at,creator from sys_app_info where appid = ?";
        List<AppInfoEntity> list = jdbcBaseDao.queryList(AppInfoEntity.class, sql, appid);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
