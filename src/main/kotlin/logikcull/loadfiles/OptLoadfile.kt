package logikcull.loadfiles

import java.io.BufferedReader
import java.io.Reader

class OptLoadfile(_r: java.io.Reader) : java.io.Closeable {
    val r = BufferedReader(_r)
    val entries: List<LoadfileEntry> = r.readLines().map { line ->
        val parts = line.split(",")
        LoadfileEntry(parts[0], parts[1], parts[2])
    }

    override fun close() {
        r.close()
    }
}
