package cn.wisesign.xamng.module

import org.nutz.dao.Condition
import org.nutz.dao.Dao
import org.nutz.dao.QueryResult
import org.nutz.dao.pager.Pager
import org.nutz.ioc.loader.annotation.Inject
import org.nutz.lang.util.NutMap
import org.nutz.mvc.Mvcs


open class BaseModule(@Inject var dao: Dao = Mvcs.ctx().defaultIoc.get(Dao::class.java)) {

    protected fun query(klass:Class<Any>,  cnd:Condition,  pager:Pager?, regex:String ):QueryResult {
        if (pager?.pageNumber?.compareTo(1) == -1) {
            pager?.pageNumber = 1
        }
        var roles = dao.query(klass, cnd, pager)
        dao.fetchLinks(roles, null)
        pager?.recordCount = dao.count(klass, cnd)
        return QueryResult(roles, pager)
    }

    protected fun ajaxOk(data: NutMap):NutMap  {
        return NutMap().setv("ok", true).setv("data", data)
    }

    protected fun ajaxFail(msg:String):NutMap {
        return NutMap().setv("ok", false).setv("msg", msg)
    }

}