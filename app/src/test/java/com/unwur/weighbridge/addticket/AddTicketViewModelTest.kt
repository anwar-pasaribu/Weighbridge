package com.unwur.weighbridge.addticket

import com.unwur.weighbridge.MainCoroutineRule
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.repo.TicketRepo
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddTicketViewModelTest {

    private lateinit var viewModel: AddTicketViewModel
    private lateinit var repo: TicketRepo

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repo = mockk()
        viewModel = AddTicketViewModel(repo)
    }

    @Test
    fun `test addNewTicket`() = runTest {
        val newTicket = Ticket(
            id = "1",
            dateTime = "2022-04-22",
            licenseNumber = "ABC123",
            driverName = "Driver",
            inboundWeight = 100.0,
            outboundWeight = 50.0
        )

        coEvery { repo.addNewTicket(any()) } just Runs

        viewModel.addNewTicket(newTicket)

        // Verify that the addNewTicket method calls the repo's addNewTicket method with the correct parameter
        coVerify { repo.addNewTicket(newTicket) }
    }
}
