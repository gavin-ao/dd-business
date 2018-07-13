package data.driven.business.business.material.impl;

import data.driven.business.business.material.MatProductService;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.vo.material.MatProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hejinkai
 * @date 2018/7/13
 */
@Service
public class MatProductServiceImpl implements MatProductService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public List<MatProductVO> findMatProductListByCatgLevelCode(String levelCode) {
        String sql = "select mp.prod_id,mp.catg_id,mp.level_code,mp.prod_name,mp.prod_introduce,spic.file_path,mp.create_at from mat_product mp" +
                " left join sys_picture spic on spic.picture_id = mp.picture_id" +
                " where mp.level_code like ? order by mp.create_at desc,mp.prod_id";
        List<MatProductVO> list = jdbcBaseDao.queryList(MatProductVO.class, sql, levelCode+"%");
        return list;
    }
}
