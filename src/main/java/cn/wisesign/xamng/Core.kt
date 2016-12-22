package cn.wisesign.xamng

import cn.wisesign.xamng.dto.CaseDetail
import org.testng.TestNG
import groovy.lang.GroovyClassLoader
import java.io.File

fun excuteGroovyCase(caseName:String){
    val groovyLoader = GroovyClassLoader(Thread.currentThread().contextClassLoader)
    val groovyNgClass = groovyLoader.parseClass(File("D:\\GitHub\\xam-ng\\src\\test\\groovy\\$caseName.groovy"))
    val tng = TestNG()
    tng.setUseDefaultListeners(false)
    tng.setTestClasses(arrayOf(groovyNgClass.newInstance().javaClass))
    tng.run()
}

fun converToGroovy(sourceCase: CaseDetail):String{
    return ""
}