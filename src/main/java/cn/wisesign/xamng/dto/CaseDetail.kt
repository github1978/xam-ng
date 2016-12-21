package cn.wisesign.xamng.dto

import cn.wisesign.xamng.po.HtmlObject

data class CaseDetail(
        var caseId:Int = 0,
        var caseName:String = "",
        var suiteId:Int = 0,
        var caseAuthor:String = "",
        var caseContext:List<CaseStep>
)

data class CaseStep(
        var stepName:String = "",
        var stepObject: HtmlObject,
        var stepAction:String = "",
        var stepExpectContext:String = ""
)