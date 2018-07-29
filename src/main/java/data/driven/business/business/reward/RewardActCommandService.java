package data.driven.business.business.reward;

import data.driven.business.entity.reward.RewardActCommandEntity;
import data.driven.business.entity.wechat.WechatHelpInfoEntity;

/**
 * 活动奖励口令service
 * @author hejinkai
 * @date 2018/7/24
 */
public interface RewardActCommandService {

    /**
     * 根据活动id获取未使用的奖励口令
     * @param actId
     * @param commandType
     * @return
     */
    public RewardActCommandEntity getNextRewardActCommandByActId(String actId, Integer commandType);

    /**
     * 根据helpId获取奖励口令文本
     * @param helpId
     * @param wechatUserId
     * @return
     */
    public String getCommandByHelpId(String helpId, String wechatUserId);

    /**
     * 将奖励口令状态修改为已使用
     * @param commandId
     */
    public void updateRewardActCommandUsed(String commandId);

    /**
     * 存储口令与活动助力的关联关系
     * @param command
     * @param helpInfoEntity
     */
    public void insertRewardActCommandHelpMapping(RewardActCommandEntity command, WechatHelpInfoEntity helpInfoEntity);

}
