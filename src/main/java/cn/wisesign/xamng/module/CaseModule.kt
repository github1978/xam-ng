package cn.wisesign.xamng.module

import cn.wisesign.xamng.SeleniumHub
import cn.wisesign.xamng.dto.CaseDetail
import cn.wisesign.xamng.dto.CaseStep
import cn.wisesign.xamng.excuteGroovyCase
import org.nutz.ioc.loader.annotation.IocBean
import org.nutz.lang.util.NutMap
import cn.wisesign.xamng.pojo.UiCase
import org.nutz.dao.QueryResult
import org.nutz.mvc.adaptor.JsonAdaptor
import org.nutz.mvc.annotation.*

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

    @At
    @AdaptBy(type= JsonAdaptor::class)
    fun save(
            @Param("caseId") caseId:String,
            @Param("step") step:CaseStep
    ):NutMap{
        print(caseId)
        print(step)
        return ajaxOk(NutMap())
    }

    @At fun delete():NutMap{
        return ajaxOk(NutMap())
    }

    @At fun saveObject(@Param("productId") productId:String):NutMap{
        return ajaxOk(NutMap())
    }

    @At fun queryObject(@Param("productId") productId:String):NutMap{
        return ajaxOk(NutMap())
    }

    @At fun excute(
            @Param("slave") slave:String,
            @Param("browser") browser:String
    ):NutMap
    {
        val uicase = CaseDetail(1,"hehe",1,"", listOf())
        excuteGroovyCase(uicase,slave,browser)
        return ajaxOk("a")
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