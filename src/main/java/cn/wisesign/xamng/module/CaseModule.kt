package cn.wisesign.xamng.module

import cn.wisesign.xamng.excuteGroovyCase
import org.nutz.ioc.loader.annotation.IocBean
import org.nutz.lang.util.NutMap
import org.nutz.mvc.annotation.At
import org.nutz.mvc.annotation.Fail
import org.nutz.mvc.annotation.Ok
import cn.wisesign.xamng.po.Case
import com.sun.xml.internal.bind.v2.schemagen.episode.Klass
import org.nutz.dao.QueryResult

@IocBean
@At("/case")
@Ok("json")
@Fail("http:500")
class CaseModule : BaseModule() {
    
    @At fun query():NutMap {
        try {
            val cases:QueryResult = queryAny(Case().javaClass,null,null,"")
            when(cases.list.size){
                0 -> return ajaxOk("no data")
                else -> {
                    val case = cases.list[0] as Case
                    return ajaxOk(NutMap().setv("caseScript", case.script))
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ajaxFail("query case is failed!system error!")
        }
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