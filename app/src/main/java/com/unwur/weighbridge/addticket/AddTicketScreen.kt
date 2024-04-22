package com.unwur.weighbridge.addticket

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.unwur.weighbridge.R
import com.unwur.weighbridge.ui.component.TicketForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTicketScreen(
    onBack: () -> Unit,
    viewModel: AddTicketViewModel = hiltViewModel()
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
                Text(text = stringResource(R.string.title_add_ticket))
            }, navigationIcon = {
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) { paddingValues ->
        Column {
            Spacer(modifier = Modifier.padding(paddingValues))
            TicketForm(
                onSubmit = { ticket ->
                    viewModel.addNewTicket(ticket)
                    onBack()
                }
            )
        }
    }
}