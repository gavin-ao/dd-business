package data.driven.business.business.behavioranalysis;

/**
 * 行为分析_助力打开奖励窗口埋点service
 * @author hejinkai
 * @date 2018/9/26
 */
public interface BehaviorAnalysisHelpOpenUrlService {

    /**
     * 插入数据
     * @param appInfoId
     * @param actId
     * @param kfOpenId
     * @return
     */
    public boolean insert(String appInfoId, String actId, String kfOpenId);

}
