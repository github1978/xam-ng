package cn.wisesign.xamng

import cn.wisesign.xamng.dto.CaseDetail
import cn.wisesign.xamng.dto.SeleniumNode
import org.testng.TestNG
import groovy.lang.GroovyClassLoader
import org.jsoup.Jsoup
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration
import org.openqa.grid.web.Hub
import java.io.File
import java.net.URL
import java.util.*

fun excuteGroovyCase(caseName: String) {
    val groovyLoader = GroovyClassLoader(Thread.currentThread().contextClassLoader)
    val groovyNgClass = groovyLoader.parseClass(File("D:\\GitHub\\xam-ng\\src\\test\\groovy\\$caseName.groovy"))
    val tng = TestNG()
    tng.setUseDefaultListeners(false)
    tng.setTestClasses(arrayOf(groovyNgClass.newInstance().javaClass))
    tng.run()
}

fun converToGroovy(sourceCase: CaseDetail): String {

    return ""
}


class SeleniumHub {
    companion object {
        var server: Hub = Hub(GridHubConfiguration())
        var server_config = GridHubConfiguration()
        fun boot() {
            server_config.port = 7474
            server = Hub(server_config)
            server.start()
        }

        fun start() {
            server.start()
        }

        fun stop() {
            server.stop()
        }

        fun getSeleniumNodes(): List<SeleniumNode>? {
            val resultlist: MutableList<SeleniumNode> = ArrayList()
            val doc = Jsoup.parse(URL(getServerContext() + "/grid/console"), 50000)
            val seNodes = doc.select("div.proxy")
            for (seNode in seNodes) {
                val seleniumNode = SeleniumNode()
                seleniumNode.id = seNode.select("div[type=config]")[0].getElementsMatchingText("^id:").text().split("id:")[1].trim()
                seleniumNode.host = seNode.select("div[type=config]")[0].getElementsMatchingText("^host:")[0].text().split("host:")[1].trim()
                seleniumNode.version = seNode.select("p.proxyname").text().trim()
                resultlist.add(seleniumNode)
            }
            return resultlist
        }

        fun getServerContext(): String {
            return "http://" + server_config.host + ":" + server_config.port
        }
    }
}

