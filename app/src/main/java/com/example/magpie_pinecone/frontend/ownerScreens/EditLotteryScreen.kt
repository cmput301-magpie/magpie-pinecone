package com.example.magpie_pinecone.frontend.ownerScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.PineconeTextField
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import com.example.magpie_pinecone.ui.theme.PineconePurple
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/*
 * EditLotteryScreen.kt contains all relevant functions for displaying the screen to start/update a lottery.
 *
 * Composable Functions:
 * EditLotteryScreen displays the screen to start/update a lottery, with fields for each required detail.
 * CampsiteCheckboxRow displays one campsite that can be ticked to add it to the lottery.
 * FieldError displays a small red hint under a field that is not filled in correctly.
 * DateRangeField displays a period's start and end dates, and opens a calendar to pick them.
 * ShowEditLotteryScreenPreview displays a preview of EditLotteryScreen for reference.
 */

/**
 * Displays the screen where an owner starts a new lottery or updates an existing one.
 *
 * The owner ticks the campsites in the lottery, sets the number of winners
 * and the waitlist capacity, and picks the registration, booking and active
 * periods from a calendar. The button stays off until everything is valid.
 *
 * @param ownerId ID of the owner the lottery belongs to.
 * @param isEdit True when updating an existing lottery, false when starting a new one.
 * @param lottery The lottery being started or updated.
 * @param availableCampsites The campsites the owner can add to the lottery.
 * @param onBack Returns to the previous screen.
 * @param onSubmit Called with the filled-in lottery when the owner submits.
 */
@Composable
fun EditLotteryScreen(
    ownerId: String,
    isEdit: Boolean,
    lottery: Lottery,
    availableCampsites: List<Campsite>,
    onBack: () -> Unit,
    onSubmit: (Lottery) -> Unit,
) {
    var numberOfWinners by remember { mutableStateOf(lottery.numberOfWinners.toString()) }
    var waitlistCapacity by remember { mutableStateOf(lottery.waitlistCapacity.toString()) }

    // Each period has a start and end date (null = not picked yet)
    var registrationStart by remember { mutableStateOf(lottery.registrationStartDate) }
    var registrationEnd by remember { mutableStateOf(lottery.registrationEndDate) }
    var bookingStart by remember { mutableStateOf(lottery.bookingStartDate) }
    var bookingEnd by remember { mutableStateOf(lottery.bookingEndDate) }
    var activeStart by remember { mutableStateOf(lottery.activeStartDate) }
    var activeEnd by remember { mutableStateOf(lottery.activeEndDate) }

    // Campsites ticked for this lottery
    val selectedCampsites = remember {
        mutableStateListOf<Campsite>().apply { addAll(lottery.campsites) }
    }

    val winners = numberOfWinners.toIntOrNull()
    val capacity = waitlistCapacity.toIntOrNull()

    // The button only turns on when everything is filled in correctly
    val canSubmit = selectedCampsites.isNotEmpty() &&
            winners != null && winners > 0 &&
            capacity != null && capacity > 0 &&
            isValidRange(registrationStart, registrationEnd) &&
            isValidRange(bookingStart, bookingEnd) &&
            isValidRange(activeStart, activeEnd)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        // Everything above the button scrolls, so all the fields fit
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PineconeTopBar(
                title = if (isEdit) "Update Lottery" else "Start Lottery",
                onBack = onBack
            )

            Text(
                text = "Campsites",
                style = MaterialTheme.typography.titleMedium
            )

            if (availableCampsites.isEmpty()) {
                Text(
                    text = "You have no campsites yet. Register a campsite first.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                availableCampsites.forEach { campsite ->
                    // Compare by ID, because the lottery's copy of a campsite is a different object
                    val isSelected = selectedCampsites.any { it.ID == campsite.ID }

                    CampsiteCheckboxRow(
                        name = campsite.name.ifBlank { "Unnamed campsite" },
                        isSelected = isSelected,
                        onToggle = {
                            if (isSelected) {
                                selectedCampsites.removeAll { it.ID == campsite.ID }
                            } else {
                                selectedCampsites.add(campsite)
                            }
                        }
                    )
                }
            }

            PineconeTextField(
                value = numberOfWinners,
                onValueChange = { numberOfWinners = it },
                label = "Num. winners",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            if (winners == null || winners <= 0) {
                FieldError(
                    text = "Enter at least 1 winner"
                )
            }

            PineconeTextField(
                value = waitlistCapacity,
                onValueChange = { waitlistCapacity = it },
                label = "Max. waitlist cap.",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            if (capacity == null || capacity <= 0) {
                FieldError(
                    text = "Enter a capacity of at least 1"
                )
            }

            DateRangeField(
                label = "Registration period",
                start = registrationStart,
                end = registrationEnd,
                onRangePicked = { start, end ->
                    registrationStart = start
                    registrationEnd = end
                }
            )
            DateRangeField(
                label = "Booking period",
                start = bookingStart,
                end = bookingEnd,
                onRangePicked = { start, end ->
                    bookingStart = start
                    bookingEnd = end
                }
            )
            DateRangeField(
                label = "Active period",
                start = activeStart,
                end = activeEnd,
                onRangePicked = { start, end ->
                    activeStart = start
                    activeEnd = end
                }
            )
        }

        // Submit button pinned at the bottom of the screen
        PillButton(
            text = if (isEdit) "Update Lottery" else "Start Lottery",
            onClick = {
                lottery.ownerId = ownerId
                lottery.numberOfWinners = winners ?: 0
                lottery.waitlistCapacity = capacity ?: 0
                lottery.registrationStartDate = registrationStart
                lottery.registrationEndDate = registrationEnd
                lottery.bookingStartDate = bookingStart
                lottery.bookingEndDate = bookingEnd
                lottery.activeStartDate = activeStart
                lottery.activeEndDate = activeEnd
                lottery.campsites = selectedCampsites.toMutableList()
                onSubmit(lottery)
            },
            enabled = canSubmit,
            containerColor = PineconePurple,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(52.dp)
        )
    }
}

/**
 * One campsite with a checkbox. Tapping anywhere on the row ticks it.
 *
 * @param name Name of the campsite.
 * @param isSelected Whether the campsite is in the lottery.
 * @param onToggle Called when the row is tapped.
 */
@Composable
private fun CampsiteCheckboxRow(
    name: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = isSelected,
                onValueChange = { onToggle() },
                role = Role.Checkbox
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = null   // The whole row handles the click
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Small red hint shown under a field that is not filled in correctly.
 *
 * @param text What the owner needs to fix.
 */
@Composable
private fun FieldError(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(start = 16.dp)
    )
}

/**
 * Field showing a period's dates, like "01/01/2027 - 01/31/2027".
 * Tapping it opens a calendar where the owner picks the start and end day.
 *
 * @param label Name of the period, e.g. "Registration period".
 * @param start Start of the period, or null if not picked yet.
 * @param end End of the period, or null if not picked yet.
 * @param onRangePicked Called with the new start and end when the owner presses OK.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangeField(
    label: String,
    start: Date?,
    end: Date?,
    onRangePicked: (start: Date?, end: Date?) -> Unit
) {
    var showCalendar by remember { mutableStateOf(false) }

    val shownText = if (start != null && end != null) {
        "${formatDate(start)} - ${formatDate(end)}"
    } else {
        ""
    }

    Box {
        OutlinedTextField(
            value = shownText,
            onValueChange = { },
            readOnly = true,   // Dates are picked from the calendar, not typed
            label = { Text(label) },
            placeholder = { Text("Pick dates") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Invisible layer on top of the field, so tapping anywhere opens the calendar
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showCalendar = true }
        )
    }

    if (showCalendar) {
        // The calendar works in UTC days, so convert our dates for it
        val pickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = start?.let { toPickerMillis(it) },
            initialSelectedEndDateMillis = end?.let { toPickerMillis(it) }
        )

        DatePickerDialog(
            onDismissRequest = { showCalendar = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRangePicked(
                            pickerState.selectedStartDateMillis?.let { fromPickerMillis(it) },
                            pickerState.selectedEndDateMillis?.let { fromPickerMillis(it) }
                        )
                        showCalendar = false
                    },
                    // Only allow OK once both a start and an end day are picked
                    enabled = pickerState.selectedEndDateMillis != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCalendar = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DateRangePicker(
                state = pickerState,
                title = {
                    Text(
                        text = label,
                        modifier = Modifier.padding(
                            start = 24.dp,
                            top = 16.dp
                        )
                    )
                },
                showModeToggle = false,   // Calendar only, no typing mode
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Formats a date as MM/dd/yyyy.
 */
private fun formatDate(
    date: Date
): String {
    return SimpleDateFormat("MM/dd/yyyy", Locale.US).format(date)
}

/**
 * A period is valid when both dates are picked and the end is not before the start.
 */
private fun isValidRange(
    start: Date?,
    end: Date?
): Boolean {
    if (start == null || end == null) {
        return false
    }
    return !end.before(start)
}

/**
 * Turns a date into the calendar's format: midnight UTC of the same day.
 */
private fun toPickerMillis(
    date: Date
): Long {
    val local = Calendar.getInstance().apply { time = date }
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        clear()
        set(local.get(Calendar.YEAR), local.get(Calendar.MONTH), local.get(Calendar.DAY_OF_MONTH))
    }
    return utc.timeInMillis
}

/**
 * Turns the calendar's day (midnight UTC) back into midnight of that day on this phone.
 */
private fun fromPickerMillis(
    millis: Long
): Date {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = millis }
    val local = Calendar.getInstance().apply {
        clear()
        set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH))
    }
    return local.time
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShowEditLotteryScreenPreview() {
    MagpiepineconeTheme {
        EditLotteryScreen(
            ownerId = "",
            isEdit = false,
            lottery = Lottery(),
            availableCampsites = listOf(
                Campsite(ID = "campsite_1", name = "Sample Campsite")
            ),
            onBack = { },
            onSubmit = { }
        )
    }
}