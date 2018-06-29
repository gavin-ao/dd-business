package data.driven.business.business.system;

import data.driven.business.entity.system.AppInfoEntity;

/**
 * 小程序信息service
 * @author hejinkai
 * @date 2018/6/27
 */
public interface AppInfoService {

    /**
     * 根据appid查询对象
     * @param appid
     * @return
     */
    public AppInfoEntity getAppInfo(String appid);
}
