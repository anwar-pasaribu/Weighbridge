package com.unwur.weighbridge.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.unwur.weighbridge.HiltTestActivity
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.repo.TicketRepo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@ExperimentalCoroutinesApi
class DetailScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repo: TicketRepo

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun displayTicketDetail() = runTest {
        // GIVEN - new ticket to db with key "1"
        val ticketKey = "1"
        val ticket = Ticket(
            id = ticketKey,
            driverName = "Gina",
            licenseNumber = "ABC",
            inboundWeight = 12.0,
            outboundWeight = 11.0
        )
        repo.addNewTicket(ticket)

        // WHEN - Detail screen is opened
        composeTestRule.setContent {
            DetailScreen(
                ticketKey = ticketKey,
                onBack = { /*TODO*/ },
                viewModel = DetailViewModel(repo)
            )
        }

        // THEN - Detail screen show ticket detail
        composeTestRule.onNodeWithText("Driver Name: Gina").assertIsDisplayed()
        composeTestRule.onNodeWithText("License Number: ABC").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inbound Weight: 12 T").assertIsDisplayed()
        composeTestRule.onNodeWithText("Outbound Weight: 11 T").assertIsDisplayed()
    }

}