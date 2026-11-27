package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.campsiteClasses.localDateToUtcMillis
import com.example.magpie_pinecone.ui.components.CampsiteImage
import com.example.magpie_pinecone.ui.components.PineconeTextField
import com.example.magpie_pinecone.ui.components.StarRating
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/*
 * SearchCampsiteScreen.kt contains all relevant functions for displaying the campsite search screen.
 *
 * Composable Functions:
 * SearchCampsiteScreen displays the campsite search screen, with options to search by campsite name, location, date range, and guest number.
 * CampsiteSearchBar displays the pill-shaped search bar.
 * SearchFiltersMenu displays the available filter options.
 * SearchFilterDialog allows the entrant to enter a location or number of guests.
 * AvailableDatesDialog allows the entrant to select a booking date range.
 * SearchResultCard displays one campsite in the results, with its picture, price, occupancy and rating.
 */

private enum class SearchFilter {
    LOCATION,
    GUESTS
}

/**
 * Displays a list of campsites that entrants can search through.
 *
 * All campsites are shown when the screen opens. Entrants can search by
 * name, or open Filters to narrow the results by location, date range and
 * number of guests.
 *
 * @param navController Controls navigation between entrant screens.
 * @param onCampsiteClick Opens the details of the campsite that was tapped.
 * @param searchCampsites Optional search function used for testing without Firebase.
 */
@Composable
fun SearchCampsiteScreen(
    navController: NavController,
    onCampsiteClick: (Campsite) -> Unit,
    searchCampsites: ((
        String,
        String,
        Date?,
        Date?,
        Int?,
        (List<Campsite>) -> Unit
    ) -> Unit)? = null
) {
    val search = if (searchCampsites == null) {
        val campsiteDB = remember { CampsiteDB() }
        campsiteDB::searchCampsitesInRange
    } else {
        searchCampsites
    }

    val focusManager = LocalFocusManager.current

    var campsites by remember { mutableStateOf<List<Campsite>>(emptyList()) }
    var isLoaded by remember { mutableStateOf(false) }

    var searchText by remember { mutableStateOf("") }
    var locationText by remember { mutableStateOf("") }
    var selectedStartDate by remember { mutableStateOf<Date?>(null) }
    var selectedEndDate by remember { mutableStateOf<Date?>(null) }
    var guests by remember { mutableStateOf<Int?>(null) }

    var showFilters by remember { mutableStateOf(false) }
    var editingFilter by remember { mutableStateOf<SearchFilter?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Prevent an older search from replacing the results of a newer search
    var searchRequest by remember { mutableStateOf(0) }

    /**
     * Searches the database using the search bar text and all the filters.
     * Blank or null filters are ignored.
     */
    fun runSearch() {
        val request = ++searchRequest

        search(
            searchText.trim(),
            locationText.trim(),
            selectedStartDate,
            selectedEndDate,
            guests
        ) { results ->
            if (request == searchRequest) {
                campsites = results
                isLoaded = true
            }
        }

        focusManager.clearFocus()
    }

    // Show every campsite when the screen is first displayed
    LaunchedEffect(Unit) {
        runSearch()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            CampsiteSearchBar(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onSearch = { runSearch() },
                modifier = Modifier.weight(1f)
            )
        }

        Box(
            modifier = Modifier.align(Alignment.End)
        ) {
            TextButton(
                onClick = { showFilters = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Filters")
            }

            SearchFiltersMenu(
                expanded = showFilters,
                location = locationText,
                startDate = selectedStartDate,
                endDate = selectedEndDate,
                guests = guests,
                onDismiss = { showFilters = false },
                onLocationClick = {
                    showFilters = false
                    editingFilter = SearchFilter.LOCATION
                },
                onDateClick = {
                    showFilters = false
                    showDatePicker = true
                },
                onGuestsClick = {
                    showFilters = false
                    editingFilter = SearchFilter.GUESTS
                }
            )
        }

        // Only say "no results" after searching, so it doesn't flash on screen first
        if (isLoaded && campsites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No campsites match your search.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(campsites) { campsite ->
                    SearchResultCard(
                        campsite = campsite,
                        onClick = { onCampsiteClick(campsite) }
                    )
                }
            }
        }
    }

    editingFilter?.let { filter ->
        SearchFilterDialog(
            filter = filter,
            initialValue = if (filter == SearchFilter.LOCATION) {
                locationText
            } else {
                guests?.toString() ?: ""
            },
            onDismiss = { editingFilter = null },
            onApply = { value ->
                if (filter == SearchFilter.LOCATION) {
                    locationText = value
                } else {
                    guests = value.toIntOrNull()
                }

                editingFilter = null
                runSearch()
            }
        )
    }

    if (showDatePicker) {
        AvailableDatesDialog(
            startDate = selectedStartDate,
            endDate = selectedEndDate,
            onDismiss = { showDatePicker = false },
            onDateSelected = { start, end ->
                selectedStartDate = start
                selectedEndDate = end
                showDatePicker = false
                runSearch()
            }
        )
    }
}

/**
 * Pill-shaped search bar. Pressing the search icon or the keyboard's
 * search key runs the search.
 *
 * @param searchText Text currently in the search bar.
 * @param onSearchTextChange Called when the text changes.
 * @param onSearch Called when the entrant asks to search.
 * @param modifier Controls the width of the search bar.
 */
@Composable
private fun CampsiteSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = { Text("Search campsites") },
        singleLine = true,
        shape = RoundedCornerShape(50),
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onSearchTextChange("")
                            onSearch()
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Cancel,
                            contentDescription = "Clear"
                        )
                    }
                }

                IconButton(onClick = onSearch) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }
        },
        // Show a "search" key on the keyboard and run the search when it is pressed
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Dropdown menu containing the three campsite filters.
 * The currently selected values are shown alongside their filter names.
 */
@Composable
private fun SearchFiltersMenu(
    expanded: Boolean,
    location: String,
    startDate: Date?,
    endDate: Date?,
    guests: Int?,
    onDismiss: () -> Unit,
    onLocationClick: () -> Unit,
    onDateClick: () -> Unit,
    onGuestsClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    if (location.isBlank()) {
                        "Location"
                    } else {
                        "Location: $location"
                    }
                )
            },
            onClick = onLocationClick
        )

        DropdownMenuItem(
            text = {
                Text(
                    if (startDate == null || endDate == null) {
                        "Date"
                    } else {
                        val format = SimpleDateFormat(
                            "MMM d, yyyy",
                            Locale.getDefault()
                        )

                        "Date: ${format.format(startDate)} - ${format.format(endDate)}"
                    }
                )
            },
            onClick = onDateClick
        )

        DropdownMenuItem(
            text = {
                Text(
                    if (guests == null) {
                        "Number of guests"
                    } else {
                        "Number of guests: $guests"
                    }
                )
            },
            onClick = onGuestsClick
        )
    }
}

/**
 * Allows the entrant to enter a location or guest count.
 * Leaving the field blank removes the selected filter.
 */
@Composable
private fun SearchFilterDialog(
    filter: SearchFilter,
    initialValue: String,
    onDismiss: () -> Unit,
    onApply: (String) -> Unit
) {
    var value by remember(filter, initialValue) {
        mutableStateOf(initialValue)
    }
    var error by remember { mutableStateOf("") }

    val isGuestFilter = filter == SearchFilter.GUESTS
    val label = if (isGuestFilter) {
        "Number of guests"
    } else {
        "Location"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(label) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PineconeTextField(
                    value = value,
                    onValueChange = {
                        value = it
                        error = ""
                    },
                    label = label,
                    keyboardOptions = if (isGuestFilter) {
                        KeyboardOptions(keyboardType = KeyboardType.Number)
                    } else {
                        KeyboardOptions.Default
                    }
                )

                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val input = value.trim()
                    val guestCount = input.toIntOrNull()

                    // A guest count must be a positive integer if provided
                    if (
                        isGuestFilter &&
                        input.isNotEmpty() &&
                        (guestCount == null || guestCount <= 0)
                    ) {
                        error = "Enter a valid number of guests."
                    } else {
                        onApply(input)
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { onApply("") }) {
                    Text("Clear")
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}

/**
 * Displays a calendar where entrants can select their preferred booking dates.
 * The selected range is used to filter the campsite search results.
 *
 * @param startDate The start date currently used as a search filter.
 * @param endDate The end date currently used as a search filter.
 * @param onDismiss Closes the calendar without changing the filter.
 * @param onDateSelected Applies or clears the selected date range.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AvailableDatesDialog(
    startDate: Date?,
    endDate: Date?,
    onDismiss: () -> Unit,
    onDateSelected: (Date?, Date?) -> Unit
) {
    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDate?.let {
            localDateToUtcMillis(it)
        },
        initialSelectedEndDateMillis = endDate?.let {
            localDateToUtcMillis(it)
        }
    )

    val selectedStart = pickerState.selectedStartDateMillis
    val selectedEnd = pickerState.selectedEndDateMillis

    DatePickerDialog(
        onDismissRequest = onDismiss,
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        confirmButton = {
            TextButton(
                onClick = {
                    if (selectedStart != null && selectedEnd != null) {
                        onDateSelected(
                            utcMillisToLocalDate(selectedStart),
                            utcMillisToLocalDate(selectedEnd)
                        )
                    }
                },
                enabled = selectedStart != null && selectedEnd != null
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { onDateSelected(null, null) }) {
                    Text("Clear")
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    ) {
        DateRangePicker(
            state = pickerState,
            title = {
                Text(
                    text = "Select booking dates",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(
                        start = 24.dp,
                        top = 16.dp
                    )
                )
            },
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

// Rebuild the selected day using the device's local timezone.
private fun utcMillisToLocalDate(utcMillis: Long): Date {
    val utc = Calendar.getInstance(
        TimeZone.getTimeZone("UTC")
    ).apply {
        timeInMillis = utcMillis
    }

    return Calendar.getInstance().apply {
        clear()
        set(
            utc.get(Calendar.YEAR),
            utc.get(Calendar.MONTH),
            utc.get(Calendar.DAY_OF_MONTH)
        )
    }.time
}

/**
 * One campsite in the search results: picture on top, then name,
 * occupancy and price, then address and rating. Tapping it opens the details.
 *
 * @param campsite Campsite shown on the card.
 * @param onClick Called when the card is tapped.
 */
@Composable
private fun SearchResultCard(
    campsite: Campsite,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {

            if (campsite.images.isEmpty()) {
                CampsiteImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            } else {
                AsyncImage(
                    model = campsite.images[0].url,
                    contentDescription = "Campsite thumbnail",
                    placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                    error = painterResource(android.R.drawable.ic_menu_report_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(160.dp)
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            // Name on the left, occupancy and price on the right
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = campsite.name.ifBlank { "Unnamed campsite" },
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.People,
                    contentDescription = "Maximum occupancy",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = campsite.maxOccupancy.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "\$${formatPrice(campsite.price)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Address on the left, rating on the right
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = campsite.location,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                StarRating(
                    rating = campsite.rating
                )
            }
        }
    }
}

/**
 * Formats prices without unnecessary decimal places (38.0 becomes "38").
 * Same rule as the entrant home and details screens, so prices look the same everywhere.
 */
private fun formatPrice(
    price: Double
): String {
    return if (price % 1.0 == 0.0) {
        price.toInt().toString()
    } else {
        "%.2f".format(price)
    }
}