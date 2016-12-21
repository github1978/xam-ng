package cn.wisesign.xamng.module

import cn.wisesign.xamng.excuteGroovyCase
import org.nutz.ioc.loader.annotation.IocBean
import org.nutz.lang.util.NutMap
import org.nutz.mvc.annotation.At
import org.nutz.mvc.annotation.Fail
import org.nutz.mvc.annotation.Ok
import cn.wisesign.xamng.po.Case
import org.nutz.dao.QueryResult

@IocBean
@At("/case")
@Ok("json")
@Fail("http:500")
class CaseModule : BaseModule() {
    
    @At fun query():NutMap {
        val cases:List<Case> = dao.query(Case::class.java,null)
        val case = cases[0]
    	return ajaxOk(NutMap().setv("caseCount", case.script))
    }

    @At fun save():NutMap{
        return ajaxOk(NutMap())
    }

    @At fun delete():NutMap{
        return ajaxOk(NutMap())
    }

    @At fun excute():NutMap{
        return ajaxOk(NutMap().setv("result",excuteGroovyCase("TestNGGroovy")))
//        return ajaxOk(NutMap().setv("result",Excutor.run("TestNGGroovy")))
    }

}