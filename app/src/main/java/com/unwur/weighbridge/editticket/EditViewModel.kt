package com.unwur.weighbridge.editticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.repo.TicketRepo
import com.unwur.weighbridge.util.Async
import com.unwur.weighbridge.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TicketUiState(
    val item: Ticket? = null,
    val isLoading: Boolean = false,
)

@HiltViewModel
class EditViewModel @Inject constructor(
    private val repo: TicketRepo
) : ViewModel() {

    private val _ticketKey = MutableStateFlow<String?>(null)
    private val _loading = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _ticketItem: Flow<Async<Ticket>> =
        _ticketKey.flatMapConcat { ticketKey ->
            repo.getTicket(ticketKey.orEmpty())
                .map { ticket ->
                    Async.Success(ticket)
                }.onStart {
                    _loading.value = true
                }
        }

    val uiState: StateFlow<TicketUiState> = combine(_loading, _ticketItem) { loading, ticketAsync ->
        when (ticketAsync) {
            Async.Loading -> {
                TicketUiState(isLoading = true)
            }

            is Async.Success -> {
                TicketUiState(item = ticketAsync.data, isLoading = loading)
            }

            is Async.Error -> TicketUiState()
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = TicketUiState(isLoading = true)
    )

    fun getTicket(key: String) {
        _loading.value = true
        _ticketKey.value = key
    }

    fun updateTicket(updatedTicket: Ticket) {
        viewModelScope.launch {
            repo.updateTicket(updatedTicket)
        }
    }

}