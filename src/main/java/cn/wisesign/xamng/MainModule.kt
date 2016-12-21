package cn.wisesign.xamng

import org.nutz.mvc.annotation.IocBy
import org.nutz.mvc.annotation.Modules
import org.nutz.mvc.annotation.SetupBy
import org.nutz.mvc.ioc.provider.ComboIocProvider

@SetupBy(value=MainSetup::class)
@IocBy(type=ComboIocProvider::class, args = arrayOf("*js", "ioc/","*anno", "cn.wisesign.xamng","*tx","*async") )
@Modules(scanPackage=true)
class MainModule