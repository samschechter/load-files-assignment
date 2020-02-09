package logikcull.loadfiles

import java.io.BufferedReader
import java.io.Closeable
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path

abstract class Loadfile(pathname: String): Closeable {
    private val path: Path = FileSystems.getDefault().getPath(pathname)
    val reader: BufferedReader = Files.newBufferedReader(path)
    val entries: List<LoadfileEntry> by lazy { entries() }

    companion object {
        fun from(pathname: String): Loadfile {
            return when {
                pathname.endsWith(".opt") -> OptLoadfile(pathname)
                else -> throw IllegalArgumentException("Unrecognized file extension for file $pathname")
            }
        }
    }

    override fun close() {
        reader.close()
    }

    protected abstract fun entries(): List<LoadfileEntry>
}
