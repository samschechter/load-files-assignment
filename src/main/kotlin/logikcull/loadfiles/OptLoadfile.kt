package logikcull.loadfiles

class OptLoadfile(pathname: String) : Loadfile(pathname) {
    override fun entries(): List<LoadfileEntry> {
        return reader.readLines().map { line ->
            val parts = line.split(",")
            LoadfileEntry(parts[0], parts[1], parts[2])
        }
    }
}
