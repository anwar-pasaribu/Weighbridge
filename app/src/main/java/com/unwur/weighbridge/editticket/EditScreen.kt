package com.unwur.weighbridge.editticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.ui.component.TicketForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    ticketKey: String,
    onBack: () -> Unit,
    viewModel: EditViewModel = hiltViewModel()
) {

    var ticketItem by remember {
        mutableStateOf(Ticket())
    }

    viewModel.getTicket(ticketKey)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
                Text(text = "Edit")
            }, navigationIcon = {
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsState()

        uiState.item?.let {
            ticketItem = it
        }

        Column {
            Spacer(modifier = Modifier.padding(paddingValues))
            TicketForm(
                ticket = ticketItem,
                editMode = true,
                onSubmit = { updatedTicket ->
                    viewModel.updateTicket(updatedTicket.apply { id = ticketKey })
                    onBack()
                }
            )
        }
    }
}