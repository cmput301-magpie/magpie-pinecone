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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.sp
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
private fun Date.toLocalDate(): LocalDate =
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatesBookedScreen(
    campsite: Campsite,
    reservations: List<Reservation>,
    onBack: () -> Unit
) {
    val booked = remember(reservations) { bookedDays(reservations) }
    var month by remember { mutableStateOf(YearMonth.now()) }

    Column(Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        PineconeTopBar("Dates Booked", onBack = { onBack() })
        Text(
            text = campsite.name.ifBlank { "Unnamed campsite" },
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                MonthHeader(
                    month = month,
                    onPrev = { month = month.minusMonths(1) },
                    onNext = { month = month.plusMonths(1) }
                )
                Spacer(Modifier.height(8.dp))
                MonthGrid(month = month, booked = booked)
            }
        }

        if (reservations.isEmpty()) {
            Text(
                "No upcoming bookings for this campsite.",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MonthHeader(month: YearMonth, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = onPrev) { Text("<") }
        Text(
            "${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.year}",
            fontSize = 18.sp
        )
        TextButton(onClick = onNext) { Text(">") }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MonthGrid(month: YearMonth, booked: Set<LocalDate>) {
    val today = LocalDate.now()
    val leadingBlanks = month.atDay(1).dayOfWeek.value % 7   // Sunday-first (Mon=1..Sun=7 -> Sun=0)
    val cells: List<LocalDate?> =
        List(leadingBlanks) { null } + (1..month.lengthOfMonth()).map { month.atDay(it) }

    Row(Modifier.fillMaxWidth()) {
        listOf("S", "M", "T", "W", "T", "F", "S").forEach {
            Text(it, Modifier.weight(1f), textAlign = TextAlign.Center)
        }
    }

    cells.chunked(7).forEach { week ->
        Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
            repeat(7) { i ->
                val date = week.getOrNull(i)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(2.dp)
                        .then(
                            if (date != null && date in booked)
                                Modifier.background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            else Modifier
                        )
                        .then(
                            if (date == today)
                                Modifier.border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary), CircleShape)
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (date != null) Text(date.dayOfMonth.toString())
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowDatesBookedScreenPreview() {
    DatesBookedScreen(
        reservations = listOf(),
        campsite = Campsite(),
        onBack = { false }
    )
}
