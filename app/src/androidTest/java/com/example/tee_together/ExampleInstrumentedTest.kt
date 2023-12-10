package com.example.tee_together

import android.os.Looper
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

    @Test
    fun verifyMultipleHolesCanBeCreated() {
        val linearlayout = LinearLayout(context)
        // Create a new hole
        handler.createNewHole(linearlayout, context)
        handler.createNewHole(linearlayout, context)

        val firstHole = linearlayout.getChildAt(0) as LinearLayout
        val secondHole = linearlayout.getChildAt(1) as LinearLayout

        val firstHoleCount = firstHole.getChildAt(0) as TextView
        val secondHoleCount = secondHole.getChildAt(0) as TextView

        assertEquals("Hole 1", firstHoleCount.text.toString())
        assertEquals("Hole 2", secondHoleCount.text.toString())
    }

    @Test
    fun verifyMultipleHolesCanBeDecrementedIncremented() {
        val linearlayout = LinearLayout(context)
        // Create a new hole
        handler.createNewHole(linearlayout, context)
        handler.createNewHole(linearlayout, context)

        val firstHole = linearlayout.getChildAt(0) as LinearLayout
        val secondHole = linearlayout.getChildAt(1) as LinearLayout

        val firstScoreCount = firstHole.getChildAt(3) as TextView
        val secondScoreCount = secondHole.getChildAt(3) as TextView

        val firstScoreInc = firstHole.getChildAt(1) as ImageButton
        val secondScoreInc = secondHole.getChildAt(1) as ImageButton

        val firstScoreDec = firstHole.getChildAt(2) as ImageButton
        val secondScoreDec = secondHole.getChildAt(2) as ImageButton


        assertEquals("Score: 1", firstScoreCount.text.toString())
        assertEquals("Score: 1", secondScoreCount.text.toString())

        // Increment Both To Begin
        firstScoreInc.performClick()
        secondScoreInc.performClick()

        assertEquals("Score: 2", firstScoreCount.text.toString())
        assertEquals("Score: 2", secondScoreCount.text.toString())

        //Increment Just the Second
        secondScoreInc.performClick()
        // Verify first hole score didn't increment as well
        assertEquals("Score: 2", firstScoreCount.text.toString())
        // Verify second hole score did increment
        assertEquals("Score: 3", secondScoreCount.text.toString())

        //Decrement Both
        firstScoreDec.performClick()
        secondScoreDec.performClick()
        assertEquals("Score: 1", firstScoreCount.text.toString())
        assertEquals("Score: 2", secondScoreCount.text.toString())

        // Decrement Just the second hole
        secondScoreDec.performClick()

        // Verify first hole score didn't decrement as well
        assertEquals("Score: 1", firstScoreCount.text.toString())
        // Verify second hole score did decrement
        assertEquals("Score: 1", secondScoreCount.text.toString())
    }

    @Test
    fun verifyMaximumHolesNotExceeded() {
        val linearlayout = LinearLayout(context)
        // We have to prepare the looper lol, complains that Toasts can't be made if we don't do this first, again whatever
        Looper.prepare()
        // Create a new hole
        for (i in 0..17){
            handler.createNewHole(linearlayout, context)
        }
        handler.createNewHole(linearlayout, context)

        val nineteenthHole = linearlayout.getChildAt(18)
        // Apparently Android studio thinks this is more readable lol, whatever it says
        var maxHolesRespected: Boolean = nineteenthHole == null

        assertEquals(true, maxHolesRespected)

    }
    @Test
    fun verifyScoresListBeingUpdated() {
        val linearlayout = LinearLayout(context)
        // Create a new hole
        for (i in 0..14){
            handler.createNewHole(linearlayout, context)
        }

        val strokes = handler.getStrokesForHoles()

        assertEquals(15,strokes.size)

    }
    
}