package de.tomjuri.clientlauncher

/**
 * This interface is used to transform classes before they are loaded by the JVM.
 * If specified the class the tweaker is applied to will be transformed.
 * It will be called on every class, so you need to filter out the ones that you want.
 */
interface ITweaker {
    /**
     * @param className The name of the class that is about to be loaded.
     * @param classBytes The bytes of the class that is about to be loaded.
     * @return The transformed bytes of the class.
     */
    fun transform(className: String, classBytes: ByteArray): ByteArray
}