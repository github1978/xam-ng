package cn.wisesign.xamng


import org.nutz.dao.Dao
import org.nutz.dao.util.Daos
import org.nutz.mvc.NutConfig
import org.nutz.mvc.Setup
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration
import org.openqa.grid.web.Hub


class MainSetup():Setup {

    companion object{
        var selenium_server:Hub = Hub(GridHubConfiguration())
        fun booSeleniumServer(){
            var selenium_server_config = GridHubConfiguration()
            selenium_server_config.port=7474
            selenium_server = Hub(selenium_server_config)
            selenium_server.start()
        }
    }

    override fun init(conf:NutConfig) {
        var dao:Dao = conf.ioc.get(Dao::class.java)
        Daos.createTablesInPackage(dao, "cn.wisesign.xamng", false)
        booSeleniumServer()
    }

    override fun destroy(conf:NutConfig) {
    }

}
