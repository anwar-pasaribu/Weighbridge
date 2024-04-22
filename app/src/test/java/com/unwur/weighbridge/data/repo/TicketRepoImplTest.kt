package com.unwur.weighbridge.data.repo

import com.google.common.truth.Truth.assertThat
import com.unwur.weighbridge.data.source.network.FakeDataSource
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.source.local.FakeTicketDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TicketRepoImplTest {

    private lateinit var dataSource: FakeDataSource
    private lateinit var localDataSource: FakeTicketDao
    private lateinit var repo: TicketRepoImpl

    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        dataSource = FakeDataSource()
        localDataSource = FakeTicketDao()
        repo = TicketRepoImpl(
            networkDataSource = dataSource,
            localDataSource = localDataSource,
            dispatcher = testDispatcher,
            scope = testScope
        )
    }

    @Test
    fun `Get all ticket should return empty list at first`() = testScope.runTest {

        val ticketListSize = repo.getList().first().size

        assertThat(ticketListSize).isEqualTo(0)

    }

    @Test
    fun `Add first new ticket should return 1 list item`() = testScope.runTest {

        repo.addNewTicket(Ticket(driverName = "Anwar"))
        val ticketListSize = dataSource.getTicketList().first().size

        assertThat(ticketListSize).isEqualTo(1)

    }

    @Test
    fun `Edit ticket should update item in the list `() = testScope.runTest {

        repo.addNewTicket(Ticket(driverName = "Anwar", licenseNumber = "123"))
        repo.addNewTicket(Ticket(driverName = "Budi", licenseNumber = "114"))
        val ticket1 = dataSource.getTicketList().first().first()
        val ticketListSize = dataSource.getTicketList().first().size

        repo.updateTicket(Ticket(id = ticket1.id, licenseNumber = "456"))
        val updatedTicket = dataSource.getTicket(ticket1.id.orEmpty()).first()

        assertThat(ticketListSize).isEqualTo(2)
        assertThat(updatedTicket.licenseNumber).isEqualTo("456")

    }

}