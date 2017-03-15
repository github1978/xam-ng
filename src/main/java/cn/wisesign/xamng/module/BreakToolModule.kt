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
        val wrongCharge = queryUnStrikeChargeRecord(breakDate,customerName,platformName,platformVersion,moduleName)
        // 2.2 修改查询出的收费记录
        // 2.2.1 计算冲正后的金额
        val strikeChargeStartDate = wrongCharge["THIS_CHARGE_STARTTIME"].toString()
        var strikeChargeDays = getDateSpace(strikeChargeStartDate, breakDate+" 00:00:00").toInt()
        if(getMonthDay(breakDate) == "0229" || getMonthDay(strikeChargeStartDate) == "0229"){
            strikeChargeDays -= 1
        }
        val relatedChargeBillObjectId = wrongCharge["REL_CHARGE"].toString()
        val yearMoney = getYearMoneyByRelatedChargeBills(relatedChargeBillObjectId,strikeChargeStartDate)
        val strikeChargeMoney = (yearMoney.toFloat()/365)*strikeChargeDays
        println(strikeChargeMoney)
        // 2.2.2 计算差额冲正
        val wrongChargeObjectId = wrongCharge["OBJECTID"].toString()
        val relatedReceivablesSql = Sqls
                .create("select * from $DM_RECEIVABLES where REL_CHARGE_RECORD='$wrongChargeObjectId'")
                .setCallback(Sqls.callback.maps())
        val listRelatedReceivables = dao.execute(relatedReceivablesSql).getList(Map::class.java)
        var sumRelatedReceivables = 0f
        listRelatedReceivables.forEach {
            sumRelatedReceivables += it["MONEY"].toString().toFloat()
        }
        val balanceMoney = sumRelatedReceivables - strikeChargeMoney
        println(balanceMoney)
        // 2.2.3 修改错账收费记录
        val chainWrongChargeRecord = org.nutz.dao.Chain.make("CHARGE_SUM",strikeChargeMoney).add("THIS_CHARGE_ENDTIME",breakDate+" 00:00:00")
        dao.update(DM_CHARGE_RECORD,chainWrongChargeRecord,Cnd.where("OBJECTID","=",wrongChargeObjectId))
        // 2.2.3 删除错帐收费记录
        dao.clear(DM_CHARGE_RECORD,Cnd.where("REL_CHARGE","=",relatedChargeBillObjectId).and("THIS_CHARGE_STARTTIME",">",breakDate+" 00:00:00"))
        // 2.2.4 生成差额冲正应收单
        val balanceReceivablesql = Sqls.create(
                "select * from $DM_RECEIVABLES ${Cnd
                        .where("REL_CHARGE_RECORD","=",wrongChargeObjectId)
                        .and("CHARGE_STARTTIME","<=",breakDate)
                        .and("CHARGE_ENDTIME",">=",breakDate)
                }"
        ).setCallback(Sqls.callback.maps())
        val listbalanceReceivable = dao.execute(balanceReceivablesql).getList(Map::class.java)
        if(listbalanceReceivable.size==1){
            val chainBalanceReceivable = org.nutz.dao.Chain.make("OBJECTID", "RECEIVABLES:$STRIKE_SIGN${System.currentTimeMillis()}")
            listbalanceReceivable[0].forEach {
                when(it.key.toString()){
                    "CREATEDATE"         -> chainBalanceReceivable.add(it.key.toString(), getToDayDate())
                    "CREATOR"            -> chainBalanceReceivable.add(it.key.toString(),"28caec88-1a96-45f4-99d5-6555ffbd31dd")
                    "LAST_MODIFIED_TIME" -> chainBalanceReceivable.add(it.key.toString(), getToDayDate())
                    "LAST_MODIFIED_NAME" -> chainBalanceReceivable.add(it.key.toString(), STRIKE_SIGN)
                    "MONEY"              -> chainBalanceReceivable.add(it.key.toString(), "-$balanceMoney")
                    "BUSINESS_DATE"      -> chainBalanceReceivable.add(it.key.toString(), getToDayDate())
                    "IS_EXPORT"          -> chainBalanceReceivable.add(it.key.toString(), "YESORNO:5e2595da-59d7-49f6-a183-3b1a6c0d9faa")
                    "OBJECTID"           -> it.value
                    else -> chainBalanceReceivable.add(it.key.toString(),it.value)
                }
            }
            dao.insert(DM_RECEIVABLES,chainBalanceReceivable)
        }
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
        val sql:Sql = Sqls.create(
                "select * from $DM_CHARGE_RECORD ${Cnd
                        .where("THIS_CHARGE_STARTTIME","<=",breakDate)
                        .and("THIS_CHARGE_ENDTIME",">=",breakDate)
                        .and("PLATFORM","=",platformName)
                        .and("MODULE","=",moduleName)
                        .and("VERSION","=",platformVersion)
                        .and("CLIENT_NAME","=",customerName)
                }"
        ).setCallback(Sqls.callback.maps())
        return dao.execute(sql).getList(Map::class.java)[0]
    }

    // 查询符合条件的待冲应收单
    private fun queryUnStrikeReceivables(
            breakDate:String,
            customerName:String,
            platformName:String,
            platformVersion:String,
            moduleName:String
    ):List<Map<*,*>>{
        val sql: Sql = Sqls.create("select * from $DM_RECEIVABLES ${Cnd
                .where("CHARGE_STARTTIME",">",breakDate)
                .and("CLIENT_NAME","=",customerName)
                .and("MODULE_NAME","=",moduleName)
                .and("PLATFORM_NAME","=",platformName)
                .and("VERSION_NAME","=",platformVersion)
                .and("LAST_MODIFIED_NAME","<>",STRIKE_SIGN)}"
        ).setCallback(Sqls.callback.maps())
        return dao.execute(sql).getList(Map::class.java)
    }

    // 生成红冲应收单
    private fun generateStrikeReceivables(listStrikeReceivables:List<Map<*,*>>) {
        val cndWhereUnReceivablesStr = mutableListOf<String>()
        listStrikeReceivables.forEach {
            val chainStrikeReceivables = org.nutz.dao.Chain.make("OBJECTID", "RECEIVABLES:$STRIKE_SIGN${System.currentTimeMillis()}")
            it.forEach {
                when(it.key.toString()){
                    "CREATEDATE"         -> chainStrikeReceivables.add(it.key.toString(), getToDayDate())
                    "CREATOR"            -> chainStrikeReceivables.add(it.key.toString(),"28caec88-1a96-45f4-99d5-6555ffbd31dd")
                    "LAST_MODIFIED_TIME" -> chainStrikeReceivables.add(it.key.toString(), getToDayDate())
                    "LAST_MODIFIED_NAME" -> chainStrikeReceivables.add(it.key.toString(), STRIKE_SIGN)
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
                org.nutz.dao.Chain.make("LAST_MODIFIED_NAME", STRIKE_SIGN),
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
        val sql:Sql = Sqls.create("select * from $DM_CHARGE_Bills where objectid='$chargeBillsObjectId'")
                .setCallback(Sqls.callback.maps())
        val strikeChargeBill = dao.execute(sql).getList(Map::class.java)[0]
        val strikeChargeBillStartDate = strikeChargeBill["TOLL_STARTTIME"].toString()

        val firstYearEndStamp = getDateByYearStamp(strikeChargeBillStartDate,1,COMMON_DATE_FORMAT)
        val secondYearEndStamp = getDateByYearStamp(strikeChargeBillStartDate,2,COMMON_DATE_FORMAT)
        val chargeRecordStartDateStamp = date2TimeStamp(chargeRecordStartDate,COMMON_DATE_FORMAT)

        return when{
            chargeRecordStartDateStamp<=firstYearEndStamp -> strikeChargeBill["FIRST_REAL_MONEY"].toString()
            chargeRecordStartDateStamp<=secondYearEndStamp -> strikeChargeBill["SECOND_REAL_MONEY"].toString()
            else -> strikeChargeBill["THIRD_REAL_MONEY"].toString()
        }
    }

}