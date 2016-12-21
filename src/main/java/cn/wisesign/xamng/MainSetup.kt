package cn.wisesign.xamng


import org.nutz.dao.Dao
import org.nutz.dao.util.Daos
import org.nutz.mvc.NutConfig
import org.nutz.mvc.Setup


class MainSetup:Setup {

    override fun init(conf:NutConfig) {
        val dao:Dao = conf.ioc.get(Dao::class.java)
        Daos.createTablesInPackage(dao, "cn.wisesign.xamng", false)
    }

    override fun destroy(conf:NutConfig) {
    }

}
