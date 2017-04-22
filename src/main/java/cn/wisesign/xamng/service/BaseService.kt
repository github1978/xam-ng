package cn.wisesign.xamng.service

import org.nutz.dao.Condition
import org.nutz.dao.Dao
import org.nutz.dao.QueryResult
import org.nutz.dao.pager.Pager
import org.nutz.ioc.loader.annotation.Inject
import org.nutz.mvc.Mvcs


open class BaseService(@Inject var dao: Dao = Mvcs.ctx().defaultIoc.get(Dao::class.java)){

    protected fun queryAny(klass: Class<Any>, cnd: Condition?, pager: Pager?): QueryResult {
        if (pager?.pageNumber?.compareTo(1) == -1) {
            pager?.pageNumber = 1
        }
        var roles = dao.query(klass, cnd, pager)
        dao.fetchLinks(roles, null)
        pager?.recordCount = dao.count(klass, cnd)
        return QueryResult(roles, pager)
    }

}
