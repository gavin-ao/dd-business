package data.driven.business.business.demo.impl;

import data.driven.business.business.demo.DemoService;
import data.driven.business.component.Page;
import data.driven.business.component.PageBean;
import data.driven.business.dao.JDBCBaseDao;
import data.driven.business.entity.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 测试
 * @author 何晋凯
 * @date 2018/06/04
 */
@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public Page findPage(PageBean pageBean) {
        String sql = "select * from users order by create_at desc";
        return jdbcBaseDao.queryPage(Demo.class, pageBean, sql);
    }

    @Override
    public boolean insert(String userName) {
        Demo demo = new Demo();
        demo.setName(userName);
        demo.setCreateAt(new Date());
        jdbcBaseDao.insert(demo, "users");
        return true;
    }
}
