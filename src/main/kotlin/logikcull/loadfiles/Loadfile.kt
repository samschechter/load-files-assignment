package logikcull.loadfiles

import java.io.BufferedReader
import java.io.Closeable
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path

abstract class Loadfile(pathname: String): Closeable {
    private val path: Path = FileSystems.getDefault().getPath(pathname)
    private val validators: List<Validator> = listOf(
            DocumentsExistValidator(path.parent.toAbsolutePath().toString())
    )

    val reader: BufferedReader = Files.newBufferedReader(path)
    val entries: List<LoadfileEntry> by lazy { entries() }
    val validate = {
        Validator.Result(validators.flatMap { validator -> validator.validate(entries).invalidEntries })
    }

    companion object {
        fun from(pathname: String): Loadfile {
            return when {
                pathname.endsWith(".opt") -> OptLoadfile(pathname)
                pathname.endsWith(".lfp") -> LfpLoadfile(pathname)
                else -> throw IllegalArgumentException("Unrecognized file extension for file $pathname")
            }
        }
    }

    override fun close() {
        reader.close()
    }

    protected abstract fun entries(): List<LoadfileEntry>

    abstract class Validator {
        abstract fun validate(entries: List<LoadfileEntry>): Result

        data class Result(
                val invalidEntries: List<LoadfileEntry>
        ) {
            fun isValid(): Boolean {
                return invalidEntries.isEmpty()
            }
        }
    }
}
