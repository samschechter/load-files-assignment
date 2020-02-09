package logikcull.loadfiles

class FileExtensionValidator: Loadfile.Validator() {
    private val validExtensions = listOf(".tif")

    override fun validate(entries: List<LoadfileEntry>): Loadfile.Validator.Result {
        val invalidEntries = entries.filterNot { entry ->
            validExtensions.any { extension -> entry.path.endsWith(extension) }
        }

        return Result(invalidEntries)
    }
}