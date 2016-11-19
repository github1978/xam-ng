package cn.wisesign.xamng.module;

import java.util.List;

import org.nutz.dao.Condition;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import cn.wisesign.xamng.bean.Case;

@IocBean
@At("/case")
@Ok("json")
@Fail("http:500")
public class CaseModule extends BaseModule {
    
    @At
    public NutMap count() {
    	return ajaxOk(new NutMap().setv("caseCount", dao.count(Case.class)));
    }

}