package logikcull.loadfiles

import kotlin.test.Test
import kotlin.test.assertEquals

class LfpLoadfileTest {
    @Test
    fun testLfpLoadfile() {
        val subject = LfpLoadfile(javaClass.getResource("/test.lfp").path)

        assertEquals(subject.entries.size, 3)
        for (entry in subject.entries) {
            assert(entry.controlNumber.startsWith("test-00000"))
            assertEquals(entry.volumeName, "Import Test 01")
            assert(entry.path.startsWith("IMAGES/001/test-00000"))
            assert(entry.path.endsWith(".tif"))
        }
    }
}