package cn.wisesign.xamng.service

import cn.wisesign.xamng.pojo.UiCase
import org.nutz.dao.Condition
import org.nutz.dao.QueryResult
import org.nutz.dao.pager.Pager


class CaseService : BaseService() {

    fun queryCaseList(cnd: Condition?, pager:Pager?): QueryResult {
        return queryAny(UiCase().javaClass, cnd, pager)
    }

}