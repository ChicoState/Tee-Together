package com.example.tee_together

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.tee_together", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class ScoreCardHandlerTest {
    private lateinit var handler: ScoreCardHandler
    private lateinit var context: android.content.Context

    // Set up our testing environment. Get context and initialize the handler
    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        handler = ScoreCardHandler()
    }

}