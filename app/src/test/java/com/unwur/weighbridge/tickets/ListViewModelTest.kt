package com.unwur.weighbridge.tickets

import com.google.common.truth.Truth.assertThat
import com.unwur.weighbridge.MainCoroutineRule
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.model.TicketSorterType
import com.unwur.weighbridge.data.repo.TicketRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListViewModelTest {

    private val ticket1 = Ticket("1", "2022-04-22", "ABC123", "Dedi", 100.0, 50.0)
    private val ticket2 = Ticket("2", "2022-04-24", "XYZ789", "Budi", 80.0, 30.0)
    private val ticket3 = Ticket("3", "2022-04-25", "VYZ789", "Anwar", 90.0, 30.0)

    private lateinit var viewModel: ListViewModel
    private val repo: TicketRepo = mockk()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {

        every {
            repo.getList()
        }.returns(flowOf(listOf(ticket1, ticket2, ticket3)))

        viewModel = ListViewModel(repo)

    }

    @Test
    fun `test sortBy None`() = runTest {

        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        // Filter by none
        viewModel.sortBy(TicketSorterType.NONE.value)

        // Then progress indicator is shown
        assertThat(viewModel.uiState.first().isLoading).isTrue()

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertThat(viewModel.uiState.first().items).hasSize(3)
        assertThat(viewModel.uiState.first().items[0].driverName).isEqualTo(ticket1.driverName)

    }

    @Test
    fun `test sortBy Name`() = runTest {

        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        // Filter by none
        viewModel.sortBy(TicketSorterType.NAME.value)

        // Then progress indicator is shown
        assertThat(viewModel.uiState.first().isLoading).isTrue()

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertThat(viewModel.uiState.first().items[0].driverName).startsWith("A")

    }

    @Test
    fun `test sortBy License Number`() = runTest {

        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        // Filter by none
        viewModel.sortBy(TicketSorterType.LICENSE_NUMBER.value)

        // Then progress indicator is shown
        assertThat(viewModel.uiState.first().isLoading).isTrue()

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertThat(viewModel.uiState.first().items[0].licenseNumber).startsWith("A")

    }

    @Test
    fun `test searchByName`() = runTest {

        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        // Filter by none
        viewModel.searchByName("Anwar")

        // Then progress indicator is shown
        assertThat(viewModel.uiState.first().isLoading).isTrue()

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertThat(viewModel.uiState.first().items[0]).isEqualTo(ticket3)

    }

    @Test
    fun `test searchByName without result`() = runTest {

        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        // Filter by none
        viewModel.searchByName("Nana")

        // Then progress indicator is shown
        assertThat(viewModel.uiState.first().isLoading).isTrue()

        // Execute pending coroutines actions
        advanceUntilIdle()

        assertThat(viewModel.uiState.first().items).isEmpty()

    }

    @Test
    fun `test error result`() = runTest {

        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        every {
            repo.getList()
        }.returns(flowOf())

        viewModel.sortBy(TicketSorterType.NONE.value)

        assertThat(viewModel.uiState.first().isLoading).isTrue()

        advanceUntilIdle()

        assertThat(viewModel.uiState.first().isLoading).isFalse()

        assertThat(viewModel.uiState.first().items).hasSize(3)

    }

    @After
    fun after() {
        unmockkAll()
    }

}
