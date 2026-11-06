package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * TO-DO
 * Implement Date Pickers (dates are currently typed in as MM/DD/YYYY)
 */

@Composable
fun EditLotteryScreen(
    isEdit: Boolean,
    lottery: Lottery,
    availableCampsites: List<Campsite>,  // the campsites the owner can add to the lottery
    onBack: () -> Boolean,
    onSubmit: (Lottery) -> Unit,
) {
    var numberOfWinners by remember { mutableStateOf(lottery.numberOfWinners.toString()) }
    var waitlistCapacity by remember { mutableStateOf(lottery.waitlistCapacity.toString()) }

    var registrationStart by remember { mutableStateOf(dateToText(lottery.registrationStartDate)) }
    var registrationEnd by remember { mutableStateOf(dateToText(lottery.registrationEndDate)) }
    var bookingStart by remember { mutableStateOf(dateToText(lottery.bookingStartDate)) }
    var bookingEnd by remember { mutableStateOf(dateToText(lottery.bookingEndDate)) }
    var activeStart by remember { mutableStateOf(dateToText(lottery.activeStartDate)) }
    var activeEnd by remember { mutableStateOf(dateToText(lottery.activeEndDate)) }

    // Campsites currently selected for this lottery
    val selectedCampsites = remember {
        mutableStateListOf<Campsite>().apply { addAll(lottery.campsites) }
    }

    // The lottery can only be submitted once everything is filled in correctly
    val winners = numberOfWinners.toIntOrNull()
    val capacity = waitlistCapacity.toIntOrNull()
    val canSubmit = selectedCampsites.isNotEmpty() &&
            winners != null && winners > 0 &&
            capacity != null && capacity > 0 &&
            isValidRange(registrationStart, registrationEnd) &&
            isValidRange(bookingStart, bookingEnd) &&
            isValidRange(activeStart, activeEnd)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = { onBack() }
            ) {
                Text("<--")
            }

            // Scrollable, since there are too many fields to fit on one screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = if (isEdit) "Update Lottery" else "Start Lottery",
                    fontSize = 36.sp
                )

                // Campsite selection
                Text("Campsites", fontSize = 20.sp)
                if (availableCampsites.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "You have no campsites yet. Register a campsite first.",
                        fontSize = 14.sp
                    )
                } else {
                    availableCampsites.forEach { campsite ->
                        val isSelected = campsite in selectedCampsites
                        val toggle = {
                            if (isSelected) selectedCampsites.remove(campsite)
                            else selectedCampsites.add(campsite)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { toggle() }
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null  // the whole row handles the click
                            )
                            Text(
                                modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp),
                                text = campsite.name.ifBlank { "Unnamed campsite" }
                            )
                        }
                    }
                }

                // Lottery Details Input Fields
                OutlinedTextField(
                    value = numberOfWinners,
                    onValueChange = { numberOfWinners = it },
                    label = { Text("Num. winners") },
                    singleLine = true,
                    isError = winners == null || winners <= 0,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.padding(top = 12.dp)
                )

                OutlinedTextField(
                    value = waitlistCapacity,
                    onValueChange = { waitlistCapacity = it },
                    label = { Text("Max. waitlist cap.") },
                    singleLine = true,
                    isError = capacity == null || capacity <= 0,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                // Date inputs, each period has a start and end date
                DateRangeFields(
                    label = "Registration Period",
                    start = registrationStart,
                    onStartChange = { registrationStart = it },
                    end = registrationEnd,
                    onEndChange = { registrationEnd = it }
                )

                DateRangeFields(
                    label = "Booking Period",
                    start = bookingStart,
                    onStartChange = { bookingStart = it },
                    end = bookingEnd,
                    onEndChange = { bookingEnd = it }
                )

                DateRangeFields(
                    label = "Active Period",
                    start = activeStart,
                    onStartChange = { activeStart = it },
                    end = activeEnd,
                    onEndChange = { activeEnd = it }
                )

                Button(
                    modifier = Modifier.padding(vertical = 24.dp),
                    enabled = canSubmit,
                    onClick = {
                        // Lottery is immutable apart from its id, so submit an updated copy.
                        // copy() keeps the id and waitlist/winners of the original.
                        onSubmit(
                            lottery.copy(
                                campsites = selectedCampsites.toMutableList(),
                                numberOfWinners = winners ?: 1,
                                waitlistCapacity = capacity ?: 100,
                                registrationStartDate = textToDate(registrationStart),
                                registrationEndDate = textToDate(registrationEnd),
                                bookingStartDate = textToDate(bookingStart),
                                bookingEndDate = textToDate(bookingEnd),
                                activeStartDate = textToDate(activeStart),
                                activeEndDate = textToDate(activeEnd)
                            )
                        )
                    }
                ) {
                    Text(if (isEdit) "Update Lottery" else "Start Lottery")
                }
            }
        }
    }
}

/**
 * A labelled pair of text fields for entering the start and end date of a period.
 */
@Composable
private fun DateRangeFields(
    label: String,
    start: String,
    onStartChange: (String) -> Unit,
    end: String,
    onEndChange: (String) -> Unit
) {
    val startDate = textToDate(start)
    val endDate = textToDate(end)

    // Only flag a field once the owner has typed something into it
    val startError = start.isNotBlank() && startDate == null
    val endError = end.isNotBlank() &&
            (endDate == null || (startDate != null && endDate.before(startDate)))

    Text(
        modifier = Modifier.padding(top = 12.dp),
        text = label,
        fontSize = 20.sp
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = start,
            onValueChange = onStartChange,
            label = { Text("Start") },
            supportingText = { Text("MM/DD/YYYY") },
            singleLine = true,
            isError = startError
        )
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = end,
            onValueChange = onEndChange,
            label = { Text("End") },
            supportingText = { Text(if (endError && endDate != null) "Before start" else "MM/DD/YYYY") },
            singleLine = true,
            isError = endError
        )
    }
}

// Date <-> text helpers. Dates are typed in as MM/DD/YYYY.
private const val DATE_PATTERN = "MM/dd/yyyy"

private fun dateToText(date: Date?): String =
    date?.let { SimpleDateFormat(DATE_PATTERN, Locale.US).format(it) } ?: ""

// Returns null if the text is not a real date in MM/DD/YYYY format
private fun textToDate(text: String): Date? {
    if (text.length != DATE_PATTERN.length) return null
    return runCatching {
        SimpleDateFormat(DATE_PATTERN, Locale.US).apply { isLenient = false }.parse(text)
    }.getOrNull()
}

// True if both dates are valid and the end is not before the start
private fun isValidRange(start: String, end: String): Boolean {
    val startDate = textToDate(start) ?: return false
    val endDate = textToDate(end) ?: return false
    return !endDate.before(startDate)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowEditLotteryScreenPreview() {
    EditLotteryScreen(
        isEdit = false,
        lottery = Lottery(),
        availableCampsites = emptyList(),
        onBack = { false },
        onSubmit = {}
    )
}