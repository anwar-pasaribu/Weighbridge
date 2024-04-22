package com.unwur.weighbridge.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unwur.weighbridge.R
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.data.model.TicketSorterType
import com.unwur.weighbridge.data.repo.TicketRepo
import com.unwur.weighbridge.util.Async
import com.unwur.weighbridge.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ListUiState(
    val items: List<Ticket> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class ListViewModel @Inject constructor(
    repo: TicketRepo
) : ViewModel() {

    private val _sortBy = MutableStateFlow(TicketSorterType.DATE)
    private val _searchByName = MutableStateFlow("")
    private val _loading = MutableStateFlow(false)
    private val _allList =
        combine(repo.getList(), _sortBy, _searchByName) { tickets, sorter, keyword ->
            val ticketToShow = ArrayList<Ticket>()
            when (sorter) {
                TicketSorterType.DATE -> {
                    ticketToShow.addAll(tickets.sortedBy { it.dateTime })
                }
                TicketSorterType.NAME -> {
                    ticketToShow.addAll(tickets.sortedBy { it.driverName })
                }
                TicketSorterType.LICENSE_NUMBER -> {
                    ticketToShow.addAll(tickets.sortedBy { it.licenseNumber })
                }
                else -> {
                    ticketToShow.addAll(tickets)
                }
            }

            ticketToShow.filter {
                it.driverName?.contains(keyword, ignoreCase = true) ?: false
            }
        }
            .map {
                Async.Success(it)
            }
            .catch<Async<List<Ticket>>> {
                emit(Async.Error(R.string.loading_list_error))
            }

    val uiState: StateFlow<ListUiState> = combine(_loading, _allList) { loading, listAsync ->
        when (listAsync) {
            Async.Loading -> {
                ListUiState(isLoading = true)
            }

            is Async.Success -> {
                ListUiState(items = listAsync.data, isLoading = loading)
            }

            is Async.Error -> ListUiState(isLoading = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = ListUiState(isLoading = true)
    )

    fun sortBy(sorting: Int) {
        _sortBy.value = TicketSorterType.fromValue(sorting) ?: TicketSorterType.NONE
    }

    fun searchByName(keyword: String) {
        _searchByName.value = keyword
    }

}