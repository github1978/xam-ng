package cn.wisesign.xamng

import java.net.URL


fun httpGet(url:String,param:String=""):String{
    return URL(url+"?"+param).readText()
}