package cn.wisesign.xamng.module

import org.nutz.ioc.loader.annotation.IocBean
import org.nutz.lang.util.NutMap
import org.nutz.mvc.annotation.At
import org.nutz.mvc.annotation.Fail
import org.nutz.mvc.annotation.Ok
import cn.wisesign.xamng.po.Case

@IocBean
@At("/case")
@Ok("json")
@Fail("http:500")
class CaseModule : BaseModule() {
    
    @At fun query():NutMap {
    	return ajaxOk(NutMap().setv("caseCount", dao.count(Case::class.java)))
    }

    @At fun save():NutMap{
        return ajaxOk(NutMap())
    }

    @At fun delete():NutMap{
        return ajaxOk(NutMap())
    }

}