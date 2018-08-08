package data.driven.business.business.reward.impl;

import data.driven.business.business.reward.RewardActContentService;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.entity.reward.RewardActContentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动奖励文案Service
 * @author hejinkai
 * @date 2018/8/8
 */
@Service
public class RewardActContentServiceImpl implements RewardActContentService{

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public RewardActContentEntity getRewardActContentByActAndType(String actId, Integer commandType) {
        String sql = "select content_id,content_title,content_head,content_mid,content_foot,content_btn from reward_act_content where act_id = ? and command_type = ? order by create_at,content_id limit 1";
        List<RewardActContentEntity> list = jdbcBaseDao.queryList(RewardActContentEntity.class, sql, actId, commandType);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
