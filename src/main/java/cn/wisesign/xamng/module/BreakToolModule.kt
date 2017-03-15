package cn.wisesign.xamng.module

import cn.wisesign.xamng.*
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
        var DM_CHARGE_RECORD:String = "dm_charge_record"
        var DM_CHARGE_Bills:String = "dm_charge_bills"
        var COMMON_DATE_FORMAT:String = "yyyy-MM-dd HH:mm:ss"
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
        // 2.1 查询迁出日期所在的收费记录
        var wrongCharge = queryUnStrikeChargeRecord(breakDate,customerName,platformName,platformVersion,moduleName)
        // 2.2 修改查询出的收费记录
        // 2.2.1 计算冲正后的金额
        var strikeChargeStartDate = wrongCharge["THIS_CHARGE_STARTTIME"].toString()
        var strikeChargeDays = getDateSpace(strikeChargeStartDate, breakDate+" 00:00:00").toInt()
        if(getMonthDay(breakDate).equals("0229") || getMonthDay(strikeChargeStartDate).equals("0229")){
            strikeChargeDays = strikeChargeDays - 1
        }
        var yearMoney = getYearMoneyByRelatedChargeBills(wrongCharge["REL_CHARGE"].toString(),strikeChargeStartDate)
        var strikeChargeMoney = (yearMoney.toFloat()/365)*strikeChargeDays
        println(strikeChargeMoney)
        // 2.2.2 计算差额冲正

        // 2.2.3

        // 删除错帐收费记录


        // 删除错帐收费单


    }

    // 查询迁出日期所在的收费记录
    private fun queryUnStrikeChargeRecord(
            breakDate:String,
            customerName:String,
            platformName:String,
            platformVersion:String,
            moduleName:String
    ):Map<*,*>{
        var sql:Sql = Sqls.create(
                "select * from $DM_CHARGE_RECORD where THIS_CHARGE_STARTTIME<='$breakDate' and "+
                        "THIS_CHARGE_ENDTIME>='$breakDate' and PLATFORM='$platformName' and MODULE='$moduleName'"+
                        " and VERSION='$platformVersion' and CLIENT_NAME='$customerName'"
        )
        sql.setCallback(Sqls.callback.maps())
        dao.execute(sql)
        var listWrongCharge = sql.getList(Map::class.java)
        return listWrongCharge[0]
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

    private fun getYearMoneyByRelatedChargeBills(
            chargeBillsObjectId:String,chargeRecordStartDate:String):String{
        var sql:Sql = Sqls.create("select * from $DM_CHARGE_Bills where objectid='$chargeBillsObjectId'")
        sql.setCallback(Sqls.callback.maps())
        var strikeChargeBill = dao.execute(sql).getList(Map::class.java)[0]
        var strikeChargeBillStartDate = strikeChargeBill["TOLL_STARTTIME"].toString()

        var firstYearEndStamp = getDateByYearStamp(strikeChargeBillStartDate,1,COMMON_DATE_FORMAT)
        var secondYearEndStamp = getDateByYearStamp(strikeChargeBillStartDate,2,COMMON_DATE_FORMAT)
        var chargeRecordStartDateStamp = date2TimeStamp(chargeRecordStartDate,COMMON_DATE_FORMAT)

        return when{
            chargeRecordStartDateStamp<=firstYearEndStamp -> strikeChargeBill["FIRST_REAL_MONEY"].toString()
            chargeRecordStartDateStamp<=secondYearEndStamp -> strikeChargeBill["SECOND_REAL_MONEY"].toString()
            else -> strikeChargeBill["THIRD_REAL_MONEY"].toString()
        }
    }
}