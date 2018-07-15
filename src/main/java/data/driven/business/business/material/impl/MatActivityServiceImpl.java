package data.driven.business.business.material.impl;

import data.driven.business.business.material.MatActivityService;
import data.driven.business.dao.JDBCBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 活动-素材service
 * @author hejinkai
 * @date 2018/7/15
 */
@Service
public class MatActivityServiceImpl implements MatActivityService{

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public String getMatActivityPictureUrl(String actId) {
        String sql = "select p.file_path from mat_activity ma left join sys_picture p on p.picture_id = ma.picture_id where ma.act_id = ?";
        Object filePath = jdbcBaseDao.getColumn(sql, actId);
        if(filePath != null){
            return filePath.toString();
        }
        return null;
    }
}
