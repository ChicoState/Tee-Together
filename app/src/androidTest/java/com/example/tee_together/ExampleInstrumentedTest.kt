package com.example.tee_together

import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
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

    @Test
    fun verifySingleHoleRenderedCorrect() {
        val linearlayout = LinearLayout(context)
        // Create a new hole
        handler.createNewHole(linearlayout, context)

        // This should be a linear layout according to how our handler works, so convert the view to it
        val firstHole = linearlayout.getChildAt(0) as LinearLayout
        // The first widget in linear layout is the hole number
        val firstHoleNumberView = firstHole.getChildAt(0) as TextView
        // The second widget in linear layout is the score
        val firstHoleScore = firstHole.getChildAt(3) as TextView

        assertEquals("Hole 1", firstHoleNumberView.text.toString())
        assertEquals("Score: 1", firstHoleScore.text.toString())

    }

    @Test
    fun verifySingleHoleRendersCorrectlyOnSingleIncrement() {
        val linearlayout = LinearLayout(context)
        // Create a new hole
        handler.createNewHole(linearlayout, context)

        // This should be a linear layout according to how our handler works, so convert the view to it
        val firstHole = linearlayout.getChildAt(0) as LinearLayout
        val incrementFirstHole = firstHole.getChildAt(1) as ImageButton
        // The second widget in linear layout is the score
        val firstHoleScore = firstHole.getChildAt(3) as TextView

        assertEquals("Score: 1", firstHoleScore.text.toString())
        incrementFirstHole.performClick()
        assertEquals("Score: 2", firstHoleScore.text.toString())

    }

    @Test
    fun verifySingleHoleRendersCorrectlyOnManyIncrement() {
        val linearlayout = LinearLayout(context)
        // Create a new hole
        handler.createNewHole(linearlayout, context)

        // This should be a linear layout according to how our handler works, so convert the view to it
        val firstHole = linearlayout.getChildAt(0) as LinearLayout
        val incrementFirstHole = firstHole.getChildAt(1) as ImageButton
        // The second widget in linear layout is the score
        val firstHoleScore = firstHole.getChildAt(3) as TextView

        // Click increment 100 times
        for(i in 1 .. 100){
            incrementFirstHole.performClick()
            val currScore = i + 1
            assertEquals("Score: $currScore", firstHoleScore.text.toString())
        }

    }

    @Test
    fun verifySingleHoleRendersCorrectlyOnSingleDecrement() {
        val linearlayout = LinearLayout(context)
        // Create a new hole
        handler.createNewHole(linearlayout, context)

        // This should be a linear layout according to how our handler works, so convert the view to it
        val firstHole = linearlayout.getChildAt(0) as LinearLayout
        val decrementFirstHole = firstHole.getChildAt(2) as ImageButton
        // The second widget in linear layout is the score
        val firstHoleScore = firstHole.getChildAt(3) as TextView

        assertEquals("Score: 1", firstHoleScore.text.toString())
        decrementFirstHole.performClick()
        assertEquals("Score: 0", firstHoleScore.text.toString())
    }

    @Test
    fun verifySingleHoleRendersCorrectlyOnManyDecrement() {
        val linearlayout = LinearLayout(context)
        // Create a new hole
        handler.createNewHole(linearlayout, context)

        // This should be a linear layout according to how our handler works, so convert the view to it
        val firstHole = linearlayout.getChildAt(0) as LinearLayout
        val incrementFirstHole = firstHole.getChildAt(1) as ImageButton
        val decrementFirstHole = firstHole.getChildAt(2) as ImageButton
        // The second widget in linear layout is the score
        val firstHoleScore = firstHole.getChildAt(3) as TextView
        // Increment score 100 times first
        for (i in 1 .. 100){
            incrementFirstHole.performClick()
        }
        // Click decrement 100 times
        for(i in 101 downTo 1){
            decrementFirstHole.performClick()
            val currScore = i - 1
            assertEquals("Score: $currScore", firstHoleScore.text.toString())
        }

    }
    @Test
    fun verifyDecrementWontGoBelowZero() {
        val linearlayout = LinearLayout(context)
        // Create a new hole
        handler.createNewHole(linearlayout, context)

        // This should be a linear layout according to how our handler works, so convert the view to it
        val firstHole = linearlayout.getChildAt(0) as LinearLayout
        val decrementFirstHole = firstHole.getChildAt(2) as ImageButton
        // The second widget in linear layout is the score
        val firstHoleScore = firstHole.getChildAt(3) as TextView
        // Score should be 1 before decrement, 0 after
        decrementFirstHole.performClick()
        assertEquals("Score: 0", firstHoleScore.text.toString())
        // Score should be 0 before & after decrement
        decrementFirstHole.performClick()
        assertEquals("Score: 0", firstHoleScore.text.toString())
    }




}