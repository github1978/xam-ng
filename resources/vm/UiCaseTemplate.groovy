package vm

import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import org.openqa.selenium.remote.DesiredCapabilities

class UiCaseTemplate{

    RemoteWebDriver driver

    @BeforeClass
    setUp(){
        def dc = DesiredCapabilities.internetExplorer()
        driver = new RemoteWebDriver(new URL("http://192.168.234.1:5555/wd/hub"),dc)
        driver.get("http://baidu.com")
    }

    @Test
    void create(){
        ${steps}
        driver.findElement(By.id("kw")).sendKeys("hehe")
    }

    @AfterClass
    tearDown(){

    }

}