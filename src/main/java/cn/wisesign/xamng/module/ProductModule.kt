package cn.wisesign.xamng.module

import org.nutz.ioc.loader.annotation.IocBean
import org.nutz.mvc.annotation.At
import org.nutz.mvc.annotation.Fail
import org.nutz.mvc.annotation.Ok

@Suppress("unused")
@IocBean
@At("/product")
@Ok("json")
@Fail("http:500")
class ProductModule : BaseModule() {

}