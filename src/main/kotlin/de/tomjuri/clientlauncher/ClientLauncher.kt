package de.tomjuri.clientlauncher

import java.lang.invoke.MethodHandle
import java.lang.reflect.Method

fun main(args: Array<String>) {
    val argsMap = mutableMapOf<String, MutableList<String>>()
    val newArgs = mutableListOf<String>()
    for (i in args.indices step 2) {
        val key = args[i]
        val value = args[i + 1]
        argsMap.getOrPut(key) { mutableListOf() }.add(value)
        if(key != "--launchTarget" && key != "--tweaker") {
            newArgs.add(key)
            newArgs.add(value)
        }
    }
    if (argsMap["--launchTarget"] == null || argsMap["--launchTarget"]!!.size != 1) {
        throw IllegalArgumentException("You must specify exactly one launch target!")
    }
    val launchTarget = argsMap["--launchTarget"]!![0]
    val tweakers = argsMap["--tweaker"] ?: emptyList()
    if (tweakers.isNotEmpty())
        println("Using tweakers: $tweakers")
    else
        println("No tweakers specified")
    val tweakerInstances = tweakers.map { Class.forName(it).getDeclaredConstructor().newInstance() as ITweaker }
    ClientLauncher().start(launchTarget, tweakerInstances, newArgs.toTypedArray())
}

class ClientLauncher {
    fun start(launchTarget: String, tweakers: List<ITweaker>, args: Array<String>) {
        println(javaClass.classLoader)
        val ccl = ClientClassLoader(javaClass.classLoader, tweakers)
        Thread.currentThread().contextClassLoader = ccl
        println("Starting $launchTarget with arguments: ${args.toList()}")
        Class.forName(launchTarget).getDeclaredMethod("main", Array<String>::class.java).invoke(null, args)
    }
}