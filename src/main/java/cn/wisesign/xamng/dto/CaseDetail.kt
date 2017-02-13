package cn.wisesign.xamng.dto

import cn.wisesign.xamng.pojo.HtmlObject

data class CaseDetail(
        var caseId:Int = 0,
        var caseName:String = "",
        var suiteId:Int = 0,
        var caseAuthor:String = "",
        var caseContext:List<CaseStep>
)

data class CaseStep(
        var title:String = "",
        var element:String="",
        var action:String = "",
        var expect:String = ""
)