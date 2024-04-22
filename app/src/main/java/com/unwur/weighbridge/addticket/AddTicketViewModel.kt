package com.unwur.weighbridge.addticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.repo.TicketRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddTicketViewModel @Inject constructor(
    private val repo: TicketRepo
) : ViewModel() {

    fun addNewTicket(newTicket: Ticket) {
        viewModelScope.launch {
            repo.addNewTicket(newTicket)
        }
    }
}