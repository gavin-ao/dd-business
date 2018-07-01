package data.driven.business.business.tourism.impl;

import data.driven.business.business.tourism.PictureService;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.entity.tourism.PictureEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 图片文件信息Service
 * @author 何晋凯
 * @date 2018/06/07
 */
@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public boolean addPicture(PictureEntity picture) {
        picture.setCreateAt(new Date());
        jdbcBaseDao.insert(picture, "sys_picture");
        return true;
    }
}
