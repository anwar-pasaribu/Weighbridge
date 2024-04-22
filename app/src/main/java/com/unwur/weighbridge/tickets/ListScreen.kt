package com.unwur.weighbridge.tickets

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unwur.weighbridge.FilterBar
import com.unwur.weighbridge.R
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.util.formatDoubleAsNeeded

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListScreen(
    onAddNewTicket: () -> Unit,
    onOpenDetail: (String) -> Unit,
    onEditTicket: (String) -> Unit,
    viewModel: ListViewModel = hiltViewModel()
) {

    val orientation = LocalConfiguration.current.orientation
    val maxHeight = remember { mutableStateOf(0.dp) }
    DisposableEffect(key1 = orientation) {
        maxHeight.value = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 100.dp
        else 256.dp
        onDispose { }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(max = maxHeight.value)
            ) {

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    alignment = Alignment.BottomCenter,
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.drawable.truck),
                    contentDescription = "Home Hero"
                )

                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75F))
                        .padding(start = 16.dp, top = 8.dp),
                    text = "Weighbridge",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.background,
                            blurRadius = 16F
                        )
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.height(64.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { onAddNewTicket() }) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 8.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Filled.Add,
                        contentDescription = "New"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "New Ticket", style = MaterialTheme.typography.bodyLarge)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { contentPadding ->

        val uiState by viewModel.uiState.collectAsState()
        val listState = rememberLazyListState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding()),
        ) {
            FilterBar(
                modifier = Modifier,
                onSearch = { searchString ->
                    viewModel.searchByName(searchString)
                },
                onSort = { sortBy ->
                    viewModel.sortBy(sortBy)
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = uiState.items,
                    key = { it.id.orEmpty() }) { ticketItem ->
                    TicketItem(modifier = Modifier.animateItemPlacement(),
                        ticketItem = ticketItem,
                        onOpenDetail = { onOpenDetail(it) },
                        onEditTicket = { onEditTicket(it) })
                }
                item {
                    Spacer(modifier = Modifier.height(contentPadding.calculateBottomPadding() + 64.dp))
                }
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                }
            }
        }
    }
}

@Composable
fun TicketItem(
    modifier: Modifier,
    ticketItem: Ticket,
    onOpenDetail: (String) -> Unit,
    onEditTicket: (String) -> Unit
) {
    ElevatedCard(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .clickable {
                    onOpenDetail(ticketItem.id.orEmpty())
                }
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {

                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = ticketItem.driverName.orEmpty(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                OutlinedButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {
                        onEditTicket(ticketItem.id.orEmpty())
                    }) {
                    Icon(
                        modifier = Modifier.size(14.dp),
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Sort"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Edit")
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "License: ${ticketItem.licenseNumber.orEmpty()}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Inbound: ${ticketItem.inboundWeight.formatDoubleAsNeeded()} T",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Outbound: ${ticketItem.outboundWeight.formatDoubleAsNeeded()} T",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            val netWeight = (ticketItem.inboundWeight
                ?: 0.0) - (ticketItem.outboundWeight ?: 0.0)
            val formattedNetWeight = netWeight.formatDoubleAsNeeded()
            Text(
                text = "Net Weight: $formattedNetWeight T",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }

}