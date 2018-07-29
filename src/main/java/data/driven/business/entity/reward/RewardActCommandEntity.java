package data.driven.business.entity.reward;

import java.util.Date;

/**
 * 活动奖励口令实体
 * @author hejinkai
 * @date 2018/7/24
 */
public class RewardActCommandEntity {

    /** 主键 **/
    private String commandId;
    /** 奖励的口令 **/
    private String command;
    /** 奖励口令类型，1 - 发起人奖励，2 - 参与人奖励 **/
    private String commandType;
    /** 活动id **/
    private String actId;
    /** 活动用户id **/
    private String userId;
    /** 小程序id **/
    private String appInfoId;
    /** 是否已使用  1 - 已使用 ， 0 - 未使用 **/
    private Integer used;
    /** 口令创建时间 **/
    private Date createAt;

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(String appInfoId) {
        this.appInfoId = appInfoId;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
