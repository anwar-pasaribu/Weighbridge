package com.unwur.weighbridge

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.unwur.weighbridge.ui.theme.WeighbridgeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme = isSystemInDarkTheme()
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                    ) { darkTheme },
                )
                onDispose {}
            }
            WeighbridgeTheme(darkTheme = darkTheme) {
                AppNavigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterPrev() {
    WeighbridgeTheme {
        FilterBar(modifier = Modifier.fillMaxWidth(), onSearch = {}) {

        }
    }
}

@Composable
fun FilterBar(modifier: Modifier, onSearch: (String) -> Unit, onSort: (Int) -> Unit) {
    var searchedDriver by remember {
        mutableStateOf("")
    }
    var sortDropDownDisplayed by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(searchedDriver) {
        onSearch(searchedDriver)
    }

    Row(
        modifier = modifier.then(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(.75F),
            value = searchedDriver,
            onValueChange = {
                searchedDriver = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(searchedDriver) }),
            placeholder = { Text(text = "Search by name...") },
            shape = RoundedCornerShape(16.dp)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable { sortDropDownDisplayed = true  }
                .weight(.25F)
                .height(56.dp)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sort", color = MaterialTheme.colorScheme.primary)
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Filled.ArrowDropDown,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Sort"
                )
            }

            val sortMenuList = listOf("Sort by", "Driver Name", "Date", "License Number")

            DropdownMenu(
                expanded = sortDropDownDisplayed,
                onDismissRequest = { sortDropDownDisplayed = false }
            ) {
                sortMenuList.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        onClick = {
                            onSort(index)
                            sortDropDownDisplayed = false
                        },
                        enabled = index != 0,
                        text = {
                            Text(
                                text = item
                            )
                        }
                    )
                }
            }
        }
    }
}