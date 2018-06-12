package data.driven.business.business.demo;

import data.driven.business.component.Page;
import data.driven.business.component.PageBean;

/**
 * 测试c3p0和mysql
 * @author 何晋凯
 * @date 2018/06/04
 */
public interface DemoService {
    public Page findPage(PageBean pageBean);
    public boolean insert(String userName);
}
