package cn.wisesign.xamng.module

import cn.wisesign.xamng.SeleniumHub
import cn.wisesign.xamng.excuteGroovyCase
import org.nutz.ioc.loader.annotation.IocBean
import org.nutz.lang.util.NutMap
import org.nutz.mvc.annotation.At
import org.nutz.mvc.annotation.Fail
import org.nutz.mvc.annotation.Ok
import cn.wisesign.xamng.po.UiCase
import org.nutz.dao.QueryResult

@Suppress("unused")
@IocBean
@At("/case")
@Ok("json")
@Fail("http:500")
class CaseModule : BaseModule() {
    
    @At fun query():NutMap {
        try {
            val cases:QueryResult = queryAny(UiCase().javaClass,null,null,"")
            when(cases.list.size){
                0 -> return ajaxOk("no data")
                else -> {
                    val case = cases.list[0] as UiCase
                    return ajaxOk(NutMap().setv("caseScript", case.script))
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ajaxFail(e.message)
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

    @At fun stopTestServer():NutMap{
        SeleniumHub.stop()
        return ajaxOk(NutMap())
    }

    @At fun startTestServer():NutMap{
        SeleniumHub.start()
        return ajaxOk(NutMap())
    }

    @At fun getSlaves():NutMap{
        var seleniumNodes = SeleniumHub.getSeleniumNodes()
        return ajaxOk(seleniumNodes)
    }

}