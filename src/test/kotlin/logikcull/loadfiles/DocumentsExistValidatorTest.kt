package logikcull.loadfiles

import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DocumentsExistValidatorTest {
    private val subject = DocumentsExistValidator(Paths.get("src","test","resources").toAbsolutePath().toString())

    @Test
    fun testEmptyList_isValid() {
        assertTrue { subject.validate(emptyList()).isValid() }
    }

    @Test
    fun testValidList_isValid() {
        val valid = listOf(
                LoadfileEntry("test000001", "Import Test 01", "IMAGES/001/test-000001.tif"),
                LoadfileEntry("test000001", "Import Test 01", "IMAGES/001/test-000002.tif"),
                LoadfileEntry("test000001", "Import Test 01", "IMAGES/001/test-000003.tif")
        )

        assertTrue { subject.validate(valid).isValid() }
    }

    @Test
    fun testInvalidList_isInvalid() {
        val invalid = listOf(
                LoadfileEntry("test000001", "Import Test 01", "invalid_path")
        )

        assertFalse { subject.validate(invalid).isValid() }
    }
}