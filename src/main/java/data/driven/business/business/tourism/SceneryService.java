package data.driven.business.business.tourism;

import data.driven.business.vo.tourism.SceneryVO;

import java.util.List;

/**
 * 景区景观service
 * @author 何晋凯
 * @date 2018/06/06
 */
public interface SceneryService {

    /**
     * 根据景区获取景观列表
     * @param scenicSpotId
     * @return
     */
    public List<SceneryVO> findSceneryList(String scenicSpotId);

}
