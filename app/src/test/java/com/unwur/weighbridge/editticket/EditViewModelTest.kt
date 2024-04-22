import com.unwur.weighbridge.MainCoroutineRule
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.repo.TicketRepo
import com.unwur.weighbridge.editticket.EditViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EditViewModelTest {

    private lateinit var viewModel: EditViewModel
    private lateinit var repo: TicketRepo

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repo = mockk()
        viewModel = EditViewModel(repo)
    }

    @Test
    fun `test getTicket`() = runBlocking {
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
    }

    @Test
    fun `test updateTicket`() = runBlocking {
        val ticket = Ticket(
            id = "1",
            dateTime = "2022-04-22",
            licenseNumber = "ABC123",
            driverName = "Driver",
            inboundWeight = 100.0,
            outboundWeight = 50.0
        )

        coEvery { repo.updateTicket(any()) } just Runs

        viewModel.updateTicket(ticket)

        // Verify that the updateTicket method calls the repo's updateTicket method with the correct parameter
        coVerify { repo.updateTicket(ticket) }
    }
}
