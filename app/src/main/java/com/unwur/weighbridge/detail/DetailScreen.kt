package com.unwur.weighbridge.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.util.formatDoubleAsNeeded

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    ticketKey: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {

    var ticketItem by remember {
        mutableStateOf(Ticket())
    }

    viewModel.getTicket(ticketKey)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
                Text(text = "Detail")
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
            Card(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Driver Name: ${ticketItem.driverName ?: "-"}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Date/Time: ${ticketItem.dateTime ?: "-"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "License Number: ${ticketItem.licenseNumber ?: "-"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Inbound Weight: ${ticketItem.inboundWeight.formatDoubleAsNeeded()} T",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Outbound Weight: ${ticketItem.outboundWeight.formatDoubleAsNeeded()} T",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}