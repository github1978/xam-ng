package cn.wisesign.xamng.service

import cn.wisesign.xamng.pojo.UiCase


class CaseService : BaseService() {

    fun queryCaseList(): List<UiCase> {
        return dao.query(UiCase().javaClass, null, null)
    }

}