package com.example.tee_together

import android.os.Looper
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

@RunWith(AndroidJUnit4::class)
class PreviousGamesTester{
    @Test
    fun verifyPreviousGamesStoredForKnownUser() {
        val auth = FirebaseAuth.getInstance()
        val userCredential = Tasks.await(auth.signInWithEmailAndPassword("topnolan1@gmail.com", "password"))
        var db = FirebaseFirestore.getInstance()
        var userid = auth.uid.toString()
        db.collection("users").document(userid).collection("games").get().addOnSuccessListener {
                querySnapshot ->
            assertNotNull(querySnapshot)
        }
    }
}

@RunWith(AndroidJUnit4::class)
class LoginTester{
    private lateinit var activity: ActivityScenario<LoginActivity>

    @Before
    fun setUp() {
        activity = ActivityScenario.launch(LoginActivity::class.java)
    }

    @Test
    fun validSignIn() {
        val auth = FirebaseAuth.getInstance()
        // SignOut if not signed out already
        auth.signOut()
        // Let firebase sign out
        Thread.sleep(5000)
        activity = ActivityScenario.launch(LoginActivity::class.java)
        // Type valid credentials and click login button
        // These are testing credentials, idc about them being exposed pshhhhh
        onView(withId(R.id.usernameEditText)).perform(typeText("topnolan1@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        // Sleep for a few seconds to allow firebase to do its thing
        Thread.sleep(5000)

        // Verify that ProfileActivity is displayed, just try and look for some id that should be on profile if we logged in
        onView(withId(R.id.contentFrame)).check(matches(isDisplayed()))
    }

    @Test
    fun validActionByDefault() {
        val auth = FirebaseAuth.getInstance()
        // If no current user, check if this matched
        if (auth.currentUser == null){
            onView(withId(R.id.usernameEditText)).check(matches(isDisplayed()))
        } else{ // else we should be on profile view
            onView(withId(R.id.contentFrame)).check(matches(isDisplayed()))
        }
    }
    @Test
    fun invalidSignIn() {
        val auth = FirebaseAuth.getInstance()
        // SignOut if not signed out already
        auth.signOut()

        // Let firebase sign out
        Thread.sleep(5000)
        activity = ActivityScenario.launch(LoginActivity::class.java)
        // Type valid credentials and click login button
        onView(withId(R.id.usernameEditText)).perform(typeText("topnolanbloasdahdkjw2@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("passsadadsaword"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        // Sleep for a few seconds to allow firebase to do its thing
        Thread.sleep(5000)

        // Verify that ProfileActivity is displayed, just try and look for some id on login activity to show that we didn't go to profile activity
        onView(withId(R.id.usernameEditText)).check(matches(isDisplayed()))
    }
}

@RunWith(AndroidJUnit4::class)
class CreateAccountTester {
    private lateinit var activity: ActivityScenario<CreateAccountActivity>

    @Before
    fun setUp() {
        activity = ActivityScenario.launch(CreateAccountActivity::class.java)
    }

    @Test
    fun invalidEmailCreateAccount() {
        // Type valid credentials and click login button
        // These are testing credentials, idc about them being exposed pshhhhh
        onView(withId(R.id.editTextDisplayName)).perform(typeText("blahthousand100"), closeSoftKeyboard())
        onView(withId(R.id.editTextFirstName)).perform(typeText("blah"), closeSoftKeyboard())
        onView(withId(R.id.editTextLastName)).perform(typeText("thousand"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(typeText("nonothisisbadandwillnotworkuhoh"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("uhuhnono"), closeSoftKeyboard())
        onView(withId(R.id.buttonCreateAccount)).perform(click())
        Thread.sleep(5000)
        // Since we failed to login this should check as we haven;t switched pages
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
    }
    @Test
    fun invalidPasswordCreateAccount() {
        // Type valid credentials and click login button
        // These are testing credentials, idc about them being exposed pshhhhh
        onView(withId(R.id.editTextDisplayName)).perform(typeText("blahthousand100"), closeSoftKeyboard())
        onView(withId(R.id.editTextFirstName)).perform(typeText("blah"), closeSoftKeyboard())
        onView(withId(R.id.editTextLastName)).perform(typeText("thousand"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(typeText("nonothisisbadandwillnotworkuhoh@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("uhuh"), closeSoftKeyboard())
        onView(withId(R.id.buttonCreateAccount)).perform(click())
        Thread.sleep(5000)
        // Since we failed to login this should check as we haven;t switched pages
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
    }
    @Test
    fun invalidPasswordEmptyCreateAccount() {
        // Type valid credentials and click login button
        // These are testing credentials, idc about them being exposed pshhhhh
        onView(withId(R.id.editTextDisplayName)).perform(typeText("blahthousand100"), closeSoftKeyboard())
        onView(withId(R.id.editTextFirstName)).perform(typeText("blah"), closeSoftKeyboard())
        onView(withId(R.id.editTextLastName)).perform(typeText("thousand"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(typeText("nonothisisbadandwillnotworkuhoh@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.buttonCreateAccount)).perform(click())
        Thread.sleep(5000)
        // Since we failed to login this should check as we haven;t switched pages
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
    }
    @Test
    fun noFirstNameCreateAccount() {
        // Type valid credentials and click login button
        // These are testing credentials, idc about them being exposed pshhhhh
        onView(withId(R.id.editTextDisplayName)).perform(typeText("blahthousand100"), closeSoftKeyboard())
        onView(withId(R.id.editTextFirstName)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.editTextLastName)).perform(typeText("thousand"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(typeText("nonothisisbadandwillnotworkuhoh@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("uhuuhuhuhu"), closeSoftKeyboard())
        onView(withId(R.id.buttonCreateAccount)).perform(click())
        Thread.sleep(5000)
        // Since we failed to login this should check as we haven;t switched pages
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
    }
    @Test
    fun noLastNameCreateAccount() {
        // Type valid credentials and click login button
        // These are testing credentials, idc about them being exposed pshhhhh
        onView(withId(R.id.editTextDisplayName)).perform(typeText("blahthousand100"), closeSoftKeyboard())
        onView(withId(R.id.editTextFirstName)).perform(typeText("blah"), closeSoftKeyboard())
        onView(withId(R.id.editTextLastName)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(typeText("nonothisisbadandwillnotworkuhoh@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("uhuuhuhuhu"), closeSoftKeyboard())
        onView(withId(R.id.buttonCreateAccount)).perform(click())
        Thread.sleep(5000)
        // Since we failed to login this should check as we haven;t switched pages
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
    }

    @Test
    fun noDisplayNameCreateAccount() {
        // Type valid credentials and click login button
        // These are testing credentials, idc about them being exposed pshhhhh
        onView(withId(R.id.editTextDisplayName)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.editTextFirstName)).perform(typeText("blah"), closeSoftKeyboard())
        onView(withId(R.id.editTextLastName)).perform(typeText("thousand"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(typeText("nonothisisbadandwillnotworkuhoh@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("uhuuhuhuhu"), closeSoftKeyboard())
        onView(withId(R.id.buttonCreateAccount)).perform(click())
        Thread.sleep(5000)
        // Since we failed to login this should check as we haven;t switched pages
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
    }
    @Test
    fun alreadyExistsCreateAccount() {
        // Type valid credentials and click login button
        // These are testing credentials, idc about them being exposed pshhhhh
        onView(withId(R.id.editTextDisplayName)).perform(typeText("Nolan"), closeSoftKeyboard())
        onView(withId(R.id.editTextFirstName)).perform(typeText("Top"), closeSoftKeyboard())
        onView(withId(R.id.editTextLastName)).perform(typeText("Nolan"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(typeText("topnolan1@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.buttonCreateAccount)).perform(click())
        Thread.sleep(5000)
        // Since we failed to login this should check as we haven;t switched pages
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
    }
}
@RunWith(AndroidJUnit4::class)
class MainActivityTester{
    private lateinit var activity: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        activity = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun expectedRenderOfMainActivity() {
        // Verify that MainActivity has the two buttons
        onView(withId(R.id.button)).check(matches(isDisplayed()))
        onView(withId(R.id.button2)).check(matches(isDisplayed()))
    }
    @Test
    fun travelToCreateAccountActivityFromMainActivity() {
        onView(withId(R.id.button)).perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
    }
    @Test
    fun travelToLoginActivityFromMainActivity() {
        // Make sure we are signed out before moving to login page for consistency sake
        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        onView(withId(R.id.button2)).perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

}
@RunWith(AndroidJUnit4::class)
class ProfileActivityTester{
    private lateinit var activity: ActivityScenario<ProfileActivity>

    @Before
    fun setUp() {
        activity = ActivityScenario.launch(ProfileActivity::class.java)
    }

    @Test
    fun verifyProfileRenderedCorrectly() {
        // Make sure we sign in just in case
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword("topnolan1@gmail.com", "password")
        Thread.sleep(5000)
        activity = ActivityScenario.launch(ProfileActivity::class.java)

        onView(withId(R.id.profileUsername)).check(matches(isDisplayed()))
        onView(withId(R.id.signOutButton)).check(matches(isDisplayed()))
        onView(withId(R.id.btnPreviousGames)).check(matches(isDisplayed()))
        onView(withId(R.id.btnNewScorecard)).check(matches(isDisplayed()))
    }
    @Test
    fun verifySignoutButtonWorksCorrectly() {
        // Make sure we sign in just in case
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword("topnolan1@gmail.com", "password")
        Thread.sleep(5000)
        activity = ActivityScenario.launch(ProfileActivity::class.java)

        onView(withId(R.id.signOutButton)).perform(click())
        Thread.sleep(5000)
        assertNull(auth.currentUser)
        onView(withId(R.id.usernameEditText)).check(matches(isDisplayed()))
    }
    @Test
    fun verifyNewScorecardButtonWorksCorrectly() {
        // Make sure we sign in just in case
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword("topnolan1@gmail.com", "password")
        Thread.sleep(5000)
        activity = ActivityScenario.launch(ProfileActivity::class.java)

        onView(withId(R.id.btnNewScorecard)).perform(click())
        onView(withId(R.id.add_hole)).check(matches(isDisplayed()))
    }
}


