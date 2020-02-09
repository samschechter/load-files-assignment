package logikcull.loadfiles

import java.io.File
import java.lang.IllegalArgumentException
import java.nio.file.FileSystems

class DocumentsExistValidator(val parentPath: String): Loadfile.Validator() {
    override fun validate(entries: List<LoadfileEntry>): Result {
        val invalidEntries = entries.filterNot { entry ->
            val path = normalizePathSeparators(parentPath + File.separator + entry.path)
            FileSystems.getDefault()?.getPath(path)?.toFile()?.exists() ?: false
        }

        return Result(invalidEntries)
    }

    private fun normalizePathSeparators(path: String): String {
        return when (File.separator) {
            "/" -> {
                path.replace("\\", File.separator)
            }
            "\\" -> {
                path.replace("/", File.separator)
            }
            else -> {
                throw IllegalArgumentException("Unknown file path separator")
            }
        }
    }
}