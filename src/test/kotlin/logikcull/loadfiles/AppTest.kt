/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package logikcull.loadfiles

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class AppTest {
    @Test
    fun testAppParse() {
        runBlocking {
            val subject = App()
            assertFails { subject.parse("/tmp/this/path/does/not/exist").await() }

            val opt = subject.parse(javaClass.getResource("/test.opt").path).await()
            assertEquals(opt.entries.size, 3)
        }
    }
}
