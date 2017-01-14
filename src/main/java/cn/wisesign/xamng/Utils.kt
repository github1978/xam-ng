package cn.wisesign.xamng

import org.apache.commons.collections.ExtendedProperties
import org.apache.velocity.runtime.resource.Resource
import org.apache.velocity.runtime.resource.loader.ResourceLoader
import java.io.InputStream


class VelocityLoader : ResourceLoader(){

    override fun init(p0: ExtendedProperties?) {
    }

    override fun getResourceStream(source: String?): InputStream {
        return VelocityLoader::class.java.getResourceAsStream(source)
    }

    override fun getLastModified(p0: Resource?): Long {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isSourceModified(p0: Resource?): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}