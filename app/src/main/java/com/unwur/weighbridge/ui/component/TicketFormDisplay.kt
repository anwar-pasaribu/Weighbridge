package com.unwur.weighbridge.ui.component

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.unwur.weighbridge.data.model.Ticket
import com.unwur.weighbridge.ui.theme.WeighbridgeTheme
import com.unwur.weighbridge.util.formatDoubleAsNeeded
import java.text.ParseException
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun TicketForm(
    modifier: Modifier = Modifier,
    ticket: Ticket = Ticket(),
    editMode: Boolean = false,
    onSubmit: (Ticket) -> Unit = {},
) {

    var driverName by remember { mutableStateOf("") }
    var datetimeForDb by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }
    var licenseNumberError by remember { mutableStateOf(false) }
    var inboundWeight by remember { mutableStateOf("") }
    var inboundWeightEmpty by remember { mutableStateOf(false) }
    var outboundWeight by remember { mutableStateOf("") }
    var outboundWeightEmpty by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.then(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    ) {

        if (!ticket.id.isNullOrEmpty()) {
            driverName = ticket.driverName.orEmpty()
            datetimeForDb = ticket.dateTime.orEmpty()
            licenseNumber = ticket.licenseNumber.orEmpty()
            inboundWeight = ticket.inboundWeight.formatDoubleAsNeeded()
            outboundWeight = ticket.outboundWeight.formatDoubleAsNeeded()
        }

        InputDateTime(
            modifier = Modifier.fillMaxWidth(),
            initialDateTime = ticket.dateTime.orEmpty(),
            onDateTimePicked = {
                datetimeForDb = it
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = driverName,
            onValueChange = {
                driverName = it
                ticket.driverName = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            placeholder = { Text(text = "Ex: John") },
            label = { Text(text = "Driver Name") })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(modifier = Modifier.fillMaxWidth(),
            value = licenseNumber,
            onValueChange = {
                licenseNumber = it
                ticket.licenseNumber = it
                licenseNumberError = it.isEmpty()
            },
            singleLine = true,
            isError = licenseNumberError,
            supportingText = {
                OptionalTextView(licenseNumberError, "Please enter License Number")
            },
            placeholder = { Text(text = "Ex: 123") },
            label = { Text(text = "License Number") })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(modifier = Modifier.fillMaxWidth(),
            value = inboundWeight,
            onValueChange = {
                if (it.isDigitsOnly()) {
                    inboundWeight = it
                    ticket.inboundWeight = it.toDoubleOrNull()
                    inboundWeightEmpty = it.isEmpty()
                }
            },
            suffix = { Text(text = "Ton") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = inboundWeightEmpty,
            supportingText = {
                OptionalTextView(inboundWeightEmpty, "Please enter Inbound Weight")
            },
            placeholder = { Text(text = "Ex: 12") },
            label = { Text(text = "Inbound Weight") })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(modifier = Modifier.fillMaxWidth(),
            value = outboundWeight,
            onValueChange = {
                if (it.isDigitsOnly()) {
                    outboundWeight = it
                    ticket.outboundWeight = it.toDoubleOrNull()
                    outboundWeightEmpty = it.isEmpty()
                }
            },
            suffix = { Text(text = "Ton") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = outboundWeightEmpty,
            supportingText = {
                OptionalTextView(outboundWeightEmpty, "Please enter Outbound Weight")
            },
            placeholder = { Text(text = "Ex: 12") },
            label = { Text(text = "Outbound Weight") })
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (licenseNumber.isEmpty()) {
                    licenseNumberError = true
                    return@Button
                }
                if (inboundWeight.isEmpty() || inboundWeight.toDoubleOrNull() == null) {
                    inboundWeightEmpty = true
                    return@Button
                }
                if (outboundWeight.isEmpty() || outboundWeight.toDoubleOrNull() == null) {
                    outboundWeightEmpty = true
                    return@Button
                }
                val newTicket = Ticket().apply {
                    this.driverName = driverName
                    this.dateTime = datetimeForDb
                    this.licenseNumber = licenseNumber
                    this.inboundWeight = inboundWeight.toDoubleOrNull()
                    this.outboundWeight = outboundWeight.toDoubleOrNull()
                }

                onSubmit(newTicket)

            }
        ) {
            val buttonText = if (editMode) "Update Ticket" else "Add New Ticket"
            Text(text = buttonText)
        }
    }
}

@Composable
fun OptionalTextView(shouldShow: Boolean, text: String) {
    if (shouldShow) {
        Text(text = text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDateTime(
    modifier: Modifier,
    onDateTimePicked: (String) -> Unit,
    initialDateTime: String = ""
) {

    var dateTimeForDisplay by remember { mutableStateOf("") }
    var dateTimeForDatabase by remember { mutableStateOf("") }
    var datePickerDisplayed by remember { mutableStateOf(false) }
    var timePickerDisplayed by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis
    )
    var timePickerState by remember {
        mutableStateOf(
            TimePickerState(
                initialHour = calendar[Calendar.HOUR_OF_DAY],
                initialMinute = calendar[Calendar.MINUTE],
                true
            )
        )
    }

    LaunchedEffect(initialDateTime) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        try {
            calendar.time = inputFormat.parse(initialDateTime) ?: Date()
            datePickerState.setSelection(calendar.timeInMillis)
            timePickerState = TimePickerState(
                initialHour = calendar[Calendar.HOUR_OF_DAY],
                initialMinute = calendar[Calendar.MINUTE],
                true
            )
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(
        datePickerState.selectedDateMillis,
        timePickerState.hour,
        timePickerState.minute
    ) {
        val formattedDate = datePickerState.selectedDateMillis?.let {
            val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            formatter.format(Date(it))
        }
        val formattedDateDatabase = datePickerState.selectedDateMillis?.let {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            formatter.format(Date(it))
        }
        dateTimeForDisplay = "$formattedDate, ${timePickerState.hour}:${timePickerState.minute}"
        dateTimeForDatabase =
            "$formattedDateDatabase ${timePickerState.hour}:${timePickerState.minute}:00"
        onDateTimePicked(dateTimeForDatabase)
    }

    if (datePickerDisplayed) {
        DatePickerDialog(
            onDismissRequest = { datePickerDisplayed = false },
            confirmButton = {
                Button(onClick = {
                    datePickerDisplayed = false
                    timePickerDisplayed = true
                }) {
                    Text(text = "Select Time")
                }
            }) {
            DatePicker(state = datePickerState, showModeToggle = false)
        }
    }

    if (timePickerDisplayed) {
        TimePickerDialog(onDismissRequest = {
            datePickerDisplayed = false
            timePickerDisplayed = false
        }, confirmButton = {
            Button(onClick = {
                datePickerDisplayed = false
                timePickerDisplayed = false
            }) {
                Text(text = "Done")
            }
        }) {
            TimePicker(state = timePickerState)
        }
    }

    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(4.dp)
    ) {

        Box(modifier = modifier.then(
            Modifier.clickable {
                datePickerDisplayed = true
            }
        )) {
            Column(Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)) {
                Text(
                    text = "Date and time",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = dateTimeForDisplay,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Date"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketFormPrev() {
    WeighbridgeTheme {
        TicketForm()
    }
}
