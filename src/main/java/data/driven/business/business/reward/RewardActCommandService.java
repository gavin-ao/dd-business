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

    /**
     * 记录是否领取奖励打开了窗口
     * @param helpId
     * @param wechatUserId
     * @param commandType   奖励口令类型，1 - 发起人奖励，2 - 参与人奖励
     */
    public void updateRewardActCommandHelpMappingOpenWindow(String helpId, String wechatUserId, Integer commandType);

    /**
     * 根据helpId判断是否打开过
     * @param helpId
     * @param wechatUserId
     * @return
     */
    public boolean getCommandByHelpIdOpenWindow(String helpId, String wechatUserId);

}
