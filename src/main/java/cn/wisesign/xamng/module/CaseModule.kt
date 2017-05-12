package cn.wisesign.xamng.module

import cn.wisesign.xamng.SeleniumHub
import cn.wisesign.xamng.dto.CaseDetail
import cn.wisesign.xamng.excuteGroovyCase
import org.nutz.ioc.loader.annotation.IocBean
import org.nutz.lang.util.NutMap
import cn.wisesign.xamng.pojo.UiCase
import cn.wisesign.xamng.service.CaseService
import org.nutz.dao.QueryResult
import org.nutz.mvc.adaptor.JsonAdaptor
import org.nutz.mvc.annotation.*

@Suppress("unused")
@IocBean
@At("/case")
@Ok("json")
@Fail("http:500")
class CaseModule : BaseModule() {
    
    @At fun queryCases():NutMap {
        try {
            val cases = CaseService().queryCaseList(null,null)
            when(cases.list.size){
                0 -> return ajaxOk("no data")
                else -> return ajaxOk(cases)
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ajaxFail(e.message)
        }
    }

    /*
      保存ui用例，数据格式如下：
       {
            caseId:"",
            steps:[
                {detail:"",element:"",operate:"",target:""},
                ...
            ]
       }
       保存成功，返回用例id
       保存失败，返回失败原因
     */
    @At
    @AdaptBy(type= JsonAdaptor::class)
    fun save(uicase:UiCase):NutMap{
        try {
            return ajaxOk(dao.insertWith(uicase, "steps").caseId)
        }catch(e:Exception){
            return ajaxFail(e.message)
        }
    }


    @At fun excute(
            @Param("slave") slave:String,
            @Param("browser") browser:String
    ):NutMap
    {
        val uicase = CaseDetail(1,"hehe",1,"", listOf())
        val result = excuteGroovyCase(uicase,slave,browser)
        if(result.startsWith("success")){
            return ajaxOk(result)
        }else{
            return ajaxFail(result)
        }
    }


    @At fun getSlaves():NutMap{
        return ajaxOk(SeleniumHub.getSeleniumNodes())
    }

}