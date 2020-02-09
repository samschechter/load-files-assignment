package logikcull.loadfiles

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileExtensionValidatorTest {
    private val subject = FileExtensionValidator()

    @Test
    fun testEmptyList_isValid() {
        assertTrue { subject.validate(emptyList()).isValid() }
    }

    @Test
    fun testValidList_isValid() {
        val valid = listOf(
                LoadfileEntry("test000001", "Import Test 01", "valid1.tif"),
                LoadfileEntry("test000001", "Import Test 01", "valid2.tif")
        )

        assertTrue { subject.validate(valid).isValid() }
    }

    @Test
    fun testInvalidList_isInvalid() {
        val invalid = listOf(
                LoadfileEntry("test000001", "Import Test 01", "valid1.tif"),
                LoadfileEntry("test000001", "Import Test 01", "invalid.jpeg")
        )

        assertFalse { subject.validate(invalid).isValid() }
    }
}