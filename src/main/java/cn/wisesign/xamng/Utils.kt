package cn.wisesign.xamng

import org.apache.commons.collections.ExtendedProperties
import org.apache.velocity.runtime.resource.Resource
import org.apache.velocity.runtime.resource.loader.ResourceLoader
import java.io.InputStream
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.app.Velocity
import java.util.*


@Suppress("unused")
class VelocityLoader : ResourceLoader(){

    override fun init(p0: ExtendedProperties?) {
    }

    override fun getResourceStream(source: String?): InputStream? {
        return VelocityLoader::class.java.getResourceAsStream(source)
    }

    override fun getLastModified(p0: Resource?): Long {
        return 0
    }

    override fun isSourceModified(p0: Resource?): Boolean {
        return false
    }

}

fun getVelocityEngine(): VelocityEngine {
    val p = Properties()
    p.put("resource.loader", "srl")
    p.put("srl.resource.loader.class", "cn.wisesign.xamng.VelocityLoader")
    p.put(Velocity.ENCODING_DEFAULT, "UTF-8")
    p.put(Velocity.INPUT_ENCODING, "UTF-8")
    p.put(Velocity.OUTPUT_ENCODING, "UTF-8")
    val ve = VelocityEngine()
    ve.init(p)
    return ve
}

fun List<String>.decodeForSteps():String{
    var tmp = ""
    for (str in this){
        tmp += str + "\r\n"
    }
    return tmp
}