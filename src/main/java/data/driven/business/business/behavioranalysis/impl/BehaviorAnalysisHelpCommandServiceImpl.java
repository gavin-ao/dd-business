package data.driven.business.business.behavioranalysis.impl;

import data.driven.business.business.behavioranalysis.BehaviorAnalysisHelpCommandService;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author hejinkai
 * @date 2018/9/19
 */
@Service
public class BehaviorAnalysisHelpCommandServiceImpl implements BehaviorAnalysisHelpCommandService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public boolean openWindowInsert(String helpId, String actId, String appInfoId, String wechatUserId) {
        Date now = new Date();
        String sql = "insert into behavior_analysis_help_command(id, help_id, act_id, app_info_id, wechat_user_id, open_window, click_url, create_at) values(?,?,?,?,?,0,0,?)";
        jdbcBaseDao.executeUpdate(UUIDUtil.getUUID(), helpId, actId, appInfoId, wechatUserId, now);
        return true;
    }

    @Override
    public boolean updateClickUrl(String id) {
        String sql = "select id from behavior_analysis_help_command where id = ? and click_url = 0";
        Object objId = jdbcBaseDao.getColumn(sql, id);
        if(objId != null){
            sql = "update behavior_analysis_help_command set click_url = 1 where id = ?";
            jdbcBaseDao.executeUpdate(sql, id);
        }
        return true;
    }
}
