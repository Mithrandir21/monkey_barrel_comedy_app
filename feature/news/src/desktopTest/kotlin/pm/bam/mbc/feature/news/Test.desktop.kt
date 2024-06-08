package pm.bam.mbc.feature.news

import org.junit.Assert.assertTrue
import org.junit.Test

class DesktopGreetingTest {

    @Test
    fun testExample() {
        assertTrue("Check Java is mentioned", Greeting().greet().contains("Java"))
    }
}