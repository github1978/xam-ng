package cn.wisesign.xamng

import org.nutz.dao.Dao
import org.nutz.dao.util.Daos
import org.nutz.mvc.NutConfig
import org.nutz.mvc.Setup
import org.nutz.mvc.annotation.IocBy
import org.nutz.mvc.annotation.Modules
import org.nutz.mvc.annotation.SetupBy
import org.nutz.mvc.ioc.provider.ComboIocProvider

@SetupBy(value=MainSetup::class)
@IocBy(type=ComboIocProvider::class, args = arrayOf("*js", "ioc/","*anno", "cn.wisesign.xamng","*tx","*async") )
@Modules(scanPackage=true)
class MainModule


class MainSetup(): Setup {

    companion object{
        var DAO_PACKAGE = "cn.wisesign.xamng"
    }

    override fun init(conf: NutConfig) {
        var dao: Dao = conf.ioc.get(Dao::class.java)
        Daos.createTablesInPackage(dao, DAO_PACKAGE, false)
        SeleniumHub.boot()
    }

    override fun destroy(conf: NutConfig) {
    }

}
