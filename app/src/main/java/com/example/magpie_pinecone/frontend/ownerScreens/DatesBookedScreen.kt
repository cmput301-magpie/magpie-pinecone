package com.example.magpie_pinecone.frontend.ownerScreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

/*
 * DatesBookedScreen.kt contains all relevant functions for displaying the booked dates for the owner's specified campsite.
 *
 * Composable Functions:
 * DatesBookedScreen displays a calendar on which days that have booked reservations are highlighted.
 * MonthHeader displays the selected month with buttons for previous/next month.
 * MonthGrid displays the body of the calendar, highlighting booked days, indenting with blanks to align dates with days of the week.
 * DayCell displays one day of the calendar, as part of a booked band if it is booked.
 * CalendarKey displays what the "Booked" and "Today" markings mean.
 * ShowDatesBookedScreenPreview displays a preview of DatesBookedScreen for reference.
 */

@RequiresApi(Build.VERSION_CODES.O)
private fun Date.toLocalDate(): LocalDate =  // Get the local date on device
    toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

/** Every day covered by any reservation (start and end both inclusive). */
@RequiresApi(Build.VERSION_CODES.O)
private fun bookedDays(reservations: List<Reservation>): Set<LocalDate> =
    reservations.flatMap { r ->
        val start = r.startDate.toLocalDate()
        val end = r.endDate.toLocalDate()
        generateSequence(start) { it.plusDays(1) }
            .takeWhile { !it.isAfter(end) }
            .toList()
    }.toSet()

/**
 * Displays a calendar of the days that are booked for one of the owner's campsites.
 *
 * Booked days are joined into a lavender band, and today is circled.
 * The owner can move between months with the arrows.
 *
 * @param campsite The campsite whose bookings are shown.
 * @param reservations The campsite's active reservations.
 * @param onBack Returns to the previous screen.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatesBookedScreen(
    campsite: Campsite,
    reservations: List<Reservation>,
    onBack: () -> Unit
) {
    val booked = remember(reservations) { bookedDays(reservations) }
    var month by remember { mutableStateOf(YearMonth.now()) }   // Month shown on the calendar

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        PineconeTopBar(
            title = "Dates Booked",
            onBack = { onBack() }
        )

        Text(
            text = campsite.name.ifBlank { "Unnamed campsite" },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // Lavender calendar card, like Figma
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                MonthHeader(
                    month = month,
                    onPrev = { month = month.minusMonths(1) },
                    onNext = { month = month.plusMonths(1) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                MonthGrid(
                    month = month,
                    booked = booked
                )
            }
        }

        CalendarKey()

        if (reservations.isEmpty()) {
            Text(
                text = "No upcoming bookings for this campsite.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

/**
 * Month name on the left, previous and next arrows on the right.
 *
 * @param month The month being shown.
 * @param onPrev Moves the calendar back one month.
 * @param onNext Moves the calendar forward one month.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MonthHeader(
    month: YearMonth,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.year}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        IconButton(onClick = onPrev) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous month"
            )
        }
        IconButton(onClick = onNext) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next month"
            )
        }
    }
}

/**
 * The body of the calendar: the day letters, then one row per week.
 * Blank cells are added at the start so the 1st lines up with its weekday.
 *
 * @param month The month being shown.
 * @param booked Every day that is booked.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MonthGrid(
    month: YearMonth,
    booked: Set<LocalDate>
) {
    val today = LocalDate.now()
    val leadingBlanks = month.atDay(1).dayOfWeek.value % 7   // Sunday-first (Mon=1..Sun=7 -> Sun=0)
    val cells: List<LocalDate?> =
        List(leadingBlanks) { null } + (1..month.lengthOfMonth()).map { month.atDay(it) }

    // Day letters across the top
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        listOf("S", "M", "T", "W", "T", "F", "S").forEach { dayLetter ->
            Text(
                text = dayLetter,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // One row for each week of the month
    cells.chunked(7).forEach { week ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        ) {
            repeat(7) { i ->
                val date = week.getOrNull(i)
                DayCell(
                    date = date,
                    isBooked = date != null && date in booked,
                    // The band only joins to neighbours on the same row that are also booked
                    joinsLeft = i > 0 && date != null && date.minusDays(1) in booked,
                    joinsRight = i < 6 && date != null && date.plusDays(1) in booked,
                    isToday = date == today,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * One day of the calendar.
 *
 * Booked days are filled lavender. When the day before or after is also
 * booked, that side is left square so the days join into one band,
 * like the booked range in Figma. Today gets an outline circle.
 *
 * @param date The day, or null for a blank cell before the 1st.
 * @param isBooked Whether this day is booked.
 * @param joinsLeft Whether the band continues into the day on the left.
 * @param joinsRight Whether the band continues into the day on the right.
 * @param isToday Whether this day is today.
 * @param modifier Sets the cell's width in the row.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DayCell(
    date: LocalDate?,
    isBooked: Boolean,
    joinsLeft: Boolean,
    joinsRight: Boolean,
    isToday: Boolean,
    modifier: Modifier = Modifier
) {
    // Round only the ends of a booked band (50 = half the height = fully round)
    val bandShape = RoundedCornerShape(
        topStartPercent = if (joinsLeft) 0 else 50,
        bottomStartPercent = if (joinsLeft) 0 else 50,
        topEndPercent = if (joinsRight) 0 else 50,
        bottomEndPercent = if (joinsRight) 0 else 50
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .then(
                if (isBooked)
                    Modifier.background(MaterialTheme.colorScheme.primaryContainer, bandShape)
                else Modifier
            )
            .then(
                if (isToday)
                    Modifier.border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary), CircleShape)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (date != null) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isBooked) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

/**
 * Small key under the calendar explaining the two markings.
 */
@Composable
private fun CalendarKey() {
    Row(
        modifier = Modifier.padding(
            top = 12.dp,
            start = 8.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Filled dot = booked
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
        )
        Text(
            text = "Booked",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Outline circle = today
        Box(
            modifier = Modifier
                .size(14.dp)
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary), CircleShape)
        )
        Text(
            text = "Today",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ShowDatesBookedScreenPreview() {
    MagpiepineconeTheme {
        DatesBookedScreen(
            reservations = listOf(
                // A three-day booking starting today, so the band shows in the preview
                Reservation(
                    startDate = Date(),
                    endDate = Date(System.currentTimeMillis() + 2L * 24 * 60 * 60 * 1000)
                )
            ),
            campsite = Campsite(name = "Sample Campsite"),
            onBack = { }
        )
    }
}