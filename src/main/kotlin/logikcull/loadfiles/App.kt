/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package logikcull.loadfiles

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.system.exitProcess

class App {
    suspend fun parse(pathname: String): Deferred<Loadfile> {
        return Loadfile.fromAsync(pathname)
    }
}

fun main(args: Array<String>) {
    var startTime = System.currentTimeMillis()

    if (args.isEmpty()) {
        System.err.println("usage: gradle run --args 'path/to/loadfile1.opt path/to/loadfile2.opt'")
    }

    GlobalScope.async {
        for (path in args) {
            try {

                    val lf: Loadfile = App().parse(path).await()
                    lf.use { loadfile ->
                        println("| Loadfile: %-66s |\n".format(path))
                        println("| Control Number      | Volume              | Path                             |")
                        println("| ------------------- | ------------------- | -------------------------------- |")
                        for (entry in loadfile.entries) {
                            println("| %-19s | %-19s | %-32s |\n".format(entry.controlNumber, entry.volumeName, entry.path))
                        }

                        val validatorResult = loadfile.validate().await()
                        val isValid = validatorResult.all { entry -> entry.second.isEmpty() }
                        println("| Loadfile is valid: $isValid\n")

                        if(!isValid) {
                            println("| Invalid Entries: \n")
                            for(entry in validatorResult) {
                                println("| ${entry.first}")
                                println("| ------------------- | ------------------- | -------------------------------- |")
                                entry.second.forEach { println("| %-19s | %-19s | %-32s |\n".format(it.controlNumber, it.volumeName, it.path)) }
                            }
                        }

                        println("")
                    }
            } catch (e: Exception) {
                e.printStackTrace(System.err)
            }
        }
    }.invokeOnCompletion { exitProcess(0) }

    while(System.currentTimeMillis() < startTime + 10000) {
        Thread.sleep(100)
    }

    exitProcess(1)
}
