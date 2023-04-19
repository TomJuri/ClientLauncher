package de.tomjuri.clientlauncher

import java.io.File
import java.util.zip.ZipFile

class ClientClassLoader(private val parent: ClassLoader, private val tweakers: List<ITweaker>) : ClassLoader(parent) {
    override fun findClass(name: String): Class<*> {
        for (entry in System.getProperty("java.class.path").split(":")) {
            val file = File(entry)
            if (file.extension != "jar") {
                continue
            }
            val zf = ZipFile(file)
            val entries = zf.entries()
            entries.toList().forEach {
                if (it.name == name.replace(".", "/") + ".class") {
                    var bytes = ZipFile(file).getInputStream(it).readBytes()
                    tweakers.forEach {
                        bytes = it.transform(name, bytes)
                    }
                    zf.close()
                    return if(super.findLoadedClass(name) != null)
                        super.findLoadedClass(name)
                    else
                        super.defineClass(name, bytes, 0, bytes.size)
                }
            }
        }
        return super.findClass(name)
    }
}