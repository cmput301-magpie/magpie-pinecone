package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.DeclineRed
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import com.example.magpie_pinecone.ui.theme.ReserveGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/*
 * CampsiteReserveScreen.kt contains all relevant functions for displaying a reserve campsite screen.
 *
 * Composable Functions:
 * CampsiteReserveScreen displays the reserve campsite screen, where a lottery winner confirms their reservation.
 * ReserveDatePicker displays a calendar where only the campsite's available dates can be picked.
 * RulesSection displays the campsite's rules and cancellation policy.
 * ShowCampsiteReserveScreenPreview displays a preview of CampsiteReserveScreen for reference.
 */

/**
 * Displays the screen where a lottery winner reserves a campsite.
 *
 * The campsite's available dates, rules and cancellation policy are loaded
 * from the database. The entrant must confirm they read the rules before
 * Reserve can be pressed. Decline returns to the previous screen.
 *
 * @param onBack Returns to the previous screen.
 * @param onUserAcceptedRules Records that the entrant accepted the campsite rules.
 * @param campsiteId ID of the campsite being reserved.
 * @param onReserved Called after the entrant reserves the campsite.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampsiteReserveScreen(
    onBack: () -> Boolean,
    onUserAcceptedRules: () -> Unit,
    campsiteId: String = "",
    onReserved: () -> Unit = {}
) {
    var checked by remember { mutableStateOf(false) }   // Whether the rules box is ticked
    val campsiteDB = remember { CampsiteDB() }
    var campsiteName by remember { mutableStateOf("Campsite") }
    var rules by remember { mutableStateOf("Loading rules...") }
    var availableDates by remember { mutableStateOf<List<Date>>(emptyList()) }
    var isLoaded by remember { mutableStateOf(false) }  // Whether the campsite has finished loading

    // Load the campsite's name, rules and available dates
    LaunchedEffect(campsiteId) {
        campsiteDB.readCampsite(campsiteId) { campsite ->
            campsiteName = campsite?.name?.ifBlank { null } ?: "Campsite"
            rules = campsite?.rulesAndPolicy?.ifBlank { null }
                ?: "No rules provided for this campsite."
            availableDates = campsite?.availableDates ?: emptyList()
            isLoaded = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        // Calendar and rules scroll, so they fit on small phones
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            PineconeTopBar(
                title = campsiteName,
                onBack = { onBack() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ReserveDatePicker(
                availableDates = availableDates
            )

            // Only say "no dates" after loading, so it doesn't flash on screen first
            if (isLoaded && availableDates.isEmpty()) {
                Text(
                    text = "This campsite has no available dates yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            RulesSection(
                rules = rules
            )
        }

        // Agreement and buttons pinned at the bottom of the screen
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Tapping anywhere on the row ticks the box, not just the small square
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = checked,
                        onValueChange = { checked = it },
                        role = Role.Checkbox
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = null   // The whole row handles the click
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "I have read the campsite rules and cancellation policy",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            PillButton(
                text = "Reserve",
                onClick = {
                    // Record that this user confirmed the rules, then reserve
                    onUserAcceptedRules()
                    onReserved()
                },
                containerColor = ReserveGreen,
                enabled = checked,   // Cannot reserve until the rules are confirmed
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )

            PillButton(
                text = "Decline",
                onClick = { onBack() },
                containerColor = DeclineRed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )
        }
    }
}

/**
 * Lavender calendar where only the campsite's available dates can be tapped.
 * It opens on the month of the first available date.
 *
 * Saving the picked date belongs to the reservation story, so for now
 * the calendar shows the entrant which dates are open.
 *
 * @param availableDates Dates the campsite can be reserved on.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReserveDatePicker(
    availableDates: List<Date>
) {
    // key() rebuilds the calendar once the dates arrive from the database
    key(availableDates) {
        val datePickerState = rememberDatePickerState(
            initialDisplayedMonthMillis = availableDates.minOrNull()?.time,
            selectableDates = availableDatesOnly(availableDates)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,   // Calendar only, no typing mode
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    }
}

/**
 * Small grey heading with a divider, followed by the campsite's rules.
 * Matches the section style of the campsite details screen.
 *
 * @param rules The campsite's rules and cancellation policy.
 */
@Composable
private fun RulesSection(
    rules: String
) {
    Text(
        text = "Campsite rules and cancellation policy",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(
            top = 16.dp,
            bottom = 4.dp
        )
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = rules,
        style = MaterialTheme.typography.bodyMedium
    )
}

/**
 * Builds the rule the calendar uses to decide which days can be tapped.
 * Only days that are in the campsite's available dates are allowed.
 *
 * @param availableDates Dates the campsite can be reserved on.
 */
@OptIn(ExperimentalMaterial3Api::class)
private fun availableDatesOnly(
    availableDates: List<Date>
): SelectableDates {
    // Turn each available date into text like "2027-06-17" so days are easy to compare
    val localFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val availableDays = availableDates.map { localFormat.format(it) }.toSet()

    // The calendar gives each day as milliseconds in UTC, so read it in UTC
    val utcFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    return object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcFormat.format(Date(utcTimeMillis)) in availableDays
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShowCampsiteReserveScreenPreview() {
    MagpiepineconeTheme {
        CampsiteReserveScreen(
            onBack = { false },
            onUserAcceptedRules = {}
        )
    }
}