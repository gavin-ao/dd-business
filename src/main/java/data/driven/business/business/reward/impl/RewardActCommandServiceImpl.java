package data.driven.business.business.reward.impl;

import data.driven.business.business.reward.RewardActCommandService;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.entity.reward.RewardActCommandEntity;
import data.driven.business.entity.wechat.WechatHelpInfoEntity;
import data.driven.business.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 活动奖励口令service
 * @author hejinkai
 * @date 2018/7/24
 */
@Service
public class RewardActCommandServiceImpl implements RewardActCommandService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public RewardActCommandEntity getNextRewardActCommandByActId(String actId, Integer commandType) {
        String sql = "select command_id,command,act_id,user_id,app_info_id,used,create_at from reward_act_command where act_id = ? and used = 0 and command_type = ? order by create_at,command_id";
        List<RewardActCommandEntity> list = jdbcBaseDao.queryList(RewardActCommandEntity.class, sql, actId, commandType);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public String getCommandByHelpId(String helpId, String wechatUserId) {
        String sql = "select c.command from reward_act_command_help_mapping m left join reward_act_command c on c.command_id = m.command_id where m.help_id = ? and m.wechat_user_id = ? limit 1";
        Object command = jdbcBaseDao.getColumn(sql, helpId, wechatUserId);
        if(command != null){
            return command.toString();
        }
        return null;
    }
    @Override
    public void updateRewardActCommandUsed(String commandId){
        String sql = "update reward_act_command set used = 1 where command_id = ?";
        jdbcBaseDao.executeUpdate(sql, commandId);
    }

    @Override
    public void insertRewardActCommandHelpMapping(RewardActCommandEntity command, WechatHelpInfoEntity helpInfoEntity) {
        String mapId = UUIDUtil.getUUID();
        Date createAt = new Date();
        String sql = "insert into reward_act_command_help_mapping(map_id,help_id,command_id,act_id,app_info_id,wechat_user_id,create_at) values(?,?,?,?,?,?,?)";
        jdbcBaseDao.executeUpdate(sql, mapId, helpInfoEntity.getHelpId(), command.getCommandId(), helpInfoEntity.getActId(), helpInfoEntity.getAppInfoId(), helpInfoEntity.getWechatUserId(), createAt);
    }

}
