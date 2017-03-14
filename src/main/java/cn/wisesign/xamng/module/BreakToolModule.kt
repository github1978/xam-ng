package cn.wisesign.xamng.module

import cn.wisesign.xamng.getToDayDate
import org.nutz.dao.Cnd
import org.nutz.dao.Sqls
import org.nutz.dao.sql.Sql
import org.nutz.ioc.loader.annotation.IocBean
import org.nutz.mvc.annotation.*

@Suppress("unused")
@IocBean
@At("/tool")
@Ok("json")
@Fail("http:500")
class BreakToolModule :BaseModule() {

    //  xamng/tool/process?breakDate=2016-04-12 00:00:00&customerName=保定银行股份有限公司&moduleName=移动网银模块&platformName=网银平台&platformVersion=一代

    companion object{
        var STRIKE_SIGN:String = "FIXED"
        var DM_RECEIVABLES:String = "dm_receivables"
    }

    @At
    @AdaptBy
    fun process(
            @Param("breakDate") breakDate:String,
            @Param("customerName") customerName:String,
            @Param("platformName") platformName:String,
            @Param("platformVersion") platformVersion:String,
            @Param("moduleName") moduleName:String
    ) {

        // 1. 生成红冲应收单:
        // 1.1 查询应冲应收单
        var listStrikeReceivables = queryUnStrikeReceivables(breakDate,customerName,platformName,platformVersion,moduleName)
        println(listStrikeReceivables)
        // 1.2 生成红冲
        if(listStrikeReceivables.isNotEmpty()){
            generateStrikeReceivables(listStrikeReceivables)
        }

        // 2. 处理因迁出导致错帐的收费记录
        // 2.1


        // 删除错帐收费记录


        // 删除错帐收费单


    }

    // 查询符合条件的待冲应收单
    private fun queryUnStrikeReceivables(
            breakDate:String,
            customerName:String,
            platformName:String,
            platformVersion:String,
            moduleName:String
    ):List<Map<*,*>>{
        var sql: Sql = Sqls.create(
                "select * from $DM_RECEIVABLES where CHARGE_STARTTIME > '$breakDate' and CLIENT_NAME='$customerName'"+
                        " and MODULE_NAME='$moduleName'"+ " and PLATFORM_NAME='$platformName' "+
                        "and VERSION_NAME='$platformVersion' and LAST_MODIFIED_NAME <> '$STRIKE_SIGN'"
        )
        sql.setCallback(Sqls.callback.maps())
        dao.execute(sql)
        return sql.getList(Map::class.java)
    }

    // 生成红冲应收单
    private fun generateStrikeReceivables(listStrikeReceivables:List<Map<*,*>>) {
        var cndWhereUnReceivablesStr = mutableListOf<String>()
        listStrikeReceivables.forEach {
            var chainStrikeReceivables = org.nutz.dao.Chain.make("OBJECTID", "RECEIVABLES:$STRIKE_SIGN${System.currentTimeMillis()}")
            it.forEach {
                when(it.key.toString()){
                    "CREATEDATE"         -> chainStrikeReceivables.add(it.key.toString(), getToDayDate())
                    "CREATOR"            -> chainStrikeReceivables.add(it.key.toString(),"28caec88-1a96-45f4-99d5-6555ffbd31dd")
                    "LAST_MODIFIED_TIME" -> chainStrikeReceivables.add(it.key.toString(), getToDayDate())
                    "LAST_MODIFIED_NAME" -> chainStrikeReceivables.add(it.key.toString(), "$STRIKE_SIGN")
                    "MONEY"              -> chainStrikeReceivables.add(it.key.toString(), "-${it.value}")
                    "BUSINESS_DATE"      -> chainStrikeReceivables.add(it.key.toString(), getToDayDate())
                    "IS_EXPORT"          -> chainStrikeReceivables.add(it.key.toString(), "YESORNO:5e2595da-59d7-49f6-a183-3b1a6c0d9faa")
                    "OBJECTID"           -> it.value
                    else -> chainStrikeReceivables.add(it.key.toString(),it.value)
                }
            }
            dao.insert(DM_RECEIVABLES,chainStrikeReceivables)
            cndWhereUnReceivablesStr.add("'${it["OBJECTID"]}'")
        }
        dao.update(DM_RECEIVABLES,
                org.nutz.dao.Chain.make("LAST_MODIFIED_NAME","$STRIKE_SIGN"),
                Cnd.where("OBJECTID","in",cndWhereUnReceivablesStr.joinToString(",")))
    }


    // 处理因迁出导致错帐的收费记录
    private fun processChargeRecords() {
        // 删除错帐收费记录


        // 修改错帐收费记录


        // 修改错帐收费记录所关联的收费单信息


        // 生成因修改收费记录起止时间产生的应收差额的应收单
    }

    // 删除错帐收费记录
    private fun dropChargeRecords() {

    }

    // 删除错帐收费单
    private fun dropChargeBills() {

    }

    // 通用的生成应收单方法
    private fun generateReceivables() {

    }
}