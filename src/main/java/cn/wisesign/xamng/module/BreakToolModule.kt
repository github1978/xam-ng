package cn.wisesign.xamng.module

import org.nutz.ioc.loader.annotation.IocBean
import org.nutz.mvc.annotation.At
import org.nutz.mvc.annotation.Fail
import org.nutz.mvc.annotation.Ok

@Suppress("unused")
@IocBean
@At("/tool")
@Ok("json")
@Fail("http:500")
class BreakToolModule :BaseModule() {

    @At fun process() {

        println("hehe")

        // 生成红冲应收单


        // 处理因迁出导致错帐的收费记录


        // 删除错帐收费记录


        // 删除错帐收费单


    }

    // 生成红冲应收单
    private fun generateStrikeReceivables() {

        // 查询【应收开始日期大于迁出日期】的应收单


        // 生成红冲应收单

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