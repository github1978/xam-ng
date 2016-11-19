package cn.wisesign.xamng;

import java.util.Date;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import cn.wisesign.xamng.bean.Case;

public class MainSetup implements Setup {

    public void init(NutConfig conf) {
        Ioc ioc = conf.getIoc();
        Dao dao = ioc.get(Dao.class);
        Daos.createTablesInPackage(dao, "cn.wisesign.xamng", false);
        
        if (dao.count(Case.class) == 0) {
        	Case mycase = new Case();
        	mycase.setName("admin");
        	mycase.setBelongProject("123456");
        	mycase.setScript("123123");
        	mycase.setCreatedate(new Date());
            dao.insert(mycase);
        }
    }

    public void destroy(NutConfig conf) {
    }

}
