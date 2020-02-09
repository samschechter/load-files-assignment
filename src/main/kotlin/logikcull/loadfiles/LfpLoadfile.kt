package logikcull.loadfiles

class LfpLoadfile(pathname: String): Loadfile(pathname) {
    override fun entries(): List<LoadfileEntry> {
        return reader.readLines().map { line ->
            val parts = line.split(",")
            val fileInfo = buildFileInfo(parts[4])
            LoadfileEntry(parts[1], fileInfo.volumeName, "${fileInfo.directoryName}${fileInfo.baseName}")
        }
    }

    private fun buildFileInfo(rawInfo: String): FileInfo {
        val parts = rawInfo.substring(1).split(";")
        return FileInfo(parts[0], parts[1], parts[2], parts[3].toInt())
    }
}

data class FileInfo(
        val volumeName: String,
        val directoryName: String,
        val baseName: String,
        val pageCount: Int
)