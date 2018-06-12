package data.driven.business.business.tourism;

import data.driven.business.entity.tourism.TravelsEntity;

/**
 * 景区游记service
 * @author 何晋凯
 * @date 2018/06/07
 */
public interface TravelsService {
    /**
     * 新增游记信息
     * @param travels
     * @param pictureIds
     * @return
     */
    public boolean addTravels(TravelsEntity travels, String pictureIds);

}
