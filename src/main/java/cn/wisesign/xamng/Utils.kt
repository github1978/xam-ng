package cn.wisesign.xamng

import org.apache.commons.collections.ExtendedProperties
import org.apache.velocity.runtime.resource.Resource
import org.apache.velocity.runtime.resource.loader.ResourceLoader
import java.io.InputStream
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.app.Velocity
import java.text.SimpleDateFormat
import java.util.*
import jdk.nashorn.internal.objects.NativeDate.getTime
import java.text.ParseException
import jdk.nashorn.internal.objects.NativeDate.getTime




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

fun getToDayDate():String{
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
}

fun getDateSpace(date1:String,date2:String):Long{
    var dstp1 = date2TimeStamp(date1,"yyyy-MM-dd HH:mm:ss")
    var dstp2 = date2TimeStamp(date2,"yyyy-MM-dd HH:mm:ss")
    return (dstp2-dstp1)/(60*60*24)
}

fun getMonthDay(date:String):String{
    val sdf = SimpleDateFormat("MMdd")
    return sdf.parse(date).toString()
}

fun getDateByYearStamp(date:String,years:Int,format: String):Long{
    val calendar = Calendar.getInstance()
    var sdf = SimpleDateFormat(format)
    calendar.time = sdf.parse(date)
    calendar.add(Calendar.YEAR,years)
    return calendar.time.time
}

fun timeStamp2Date(seconds: String?, format: String?): String {
    var format = format
    if (seconds == null || seconds.isEmpty() || seconds == "null") {
        return ""
    }
    if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss"
    val sdf = SimpleDateFormat(format)
    return sdf.format(Date(java.lang.Long.valueOf(seconds + "000")))
}

fun date2TimeStamp(date_str: String, format: String): Long {
    try {
        val sdf = SimpleDateFormat(format)
        return sdf.parse(date_str).time / 1000
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}