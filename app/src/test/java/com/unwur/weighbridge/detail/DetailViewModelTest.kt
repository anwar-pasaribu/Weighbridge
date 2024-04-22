package com.unwur.weighbridge.detail

import com.unwur.weighbridge.MainCoroutineRule
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.repo.TicketRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private lateinit var repo: TicketRepo

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repo = mockk()
        viewModel = DetailViewModel(repo)
    }

    @Test
    fun `test getTicket`() = runTest {
        val ticketKey = "1"
        val ticket = Ticket(
            id = ticketKey,
            dateTime = "2022-04-22",
            licenseNumber = "ABC123",
            driverName = "Driver",
            inboundWeight = 100.0,
            outboundWeight = 50.0
        )
        every { repo.getTicket(any()) } returns flowOf(ticket)

        viewModel.getTicket(ticketKey)

        // Wait for viewModel.uiState to emit
        val uiState = viewModel.uiState.first()

        // Verify that the viewModel updates the uiState correctly
        assertEquals(ticket, uiState.item)
        assertEquals(false, uiState.isLoading)
    }
}
