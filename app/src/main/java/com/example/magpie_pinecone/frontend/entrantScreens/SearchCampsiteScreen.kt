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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.magpie_pinecone.ui.components.CampsiteImage
import com.example.magpie_pinecone.ui.components.PillButton
import com.example.magpie_pinecone.ui.components.PineconeTextField
import com.example.magpie_pinecone.ui.components.StarRating
import java.text.SimpleDateFormat
import java.util.Locale

/*
 * SearchCampsiteScreen.kt contains all relevant functions for displaying the campsite search screen.
 *
 * Composable Functions:
 * SearchCampsiteScreen displays the campsite search screen, with options to search by campsite name, location, date, and guest number.
 * CampsiteSearchBar displays the pill-shaped search bar.
 * SearchFiltersPanel displays the location, date and guest filters with an Apply button.
 * SearchResultCard displays one campsite in the results, with its picture, price, occupancy and rating.
 */

/**
 * Displays a list of campsites that entrants can search through.
 *
 * All campsites are shown when the screen opens. Entrants can search by
 * name, or open Filters to narrow the results by location, date and
 * number of guests.
 *
 * @param navController Controls navigation between entrant screens.
 * @param onCampsiteClick Opens the details of the campsite that was tapped.
 */
@Composable
fun SearchCampsiteScreen(
    navController: NavController,
    onCampsiteClick: (Campsite) -> Unit
) {
    val campsiteDB = remember { CampsiteDB() }   // Used to search the campsites in the database
    val focusManager = LocalFocusManager.current // Used to close the keyboard after searching

    var campsites by remember { mutableStateOf<List<Campsite>>(emptyList()) }
    var isLoaded by remember { mutableStateOf(false) }    // Whether the first search has finished

    var searchText by remember { mutableStateOf("") }     // Text typed in the search bar
    var locationText by remember { mutableStateOf("") }   // Location filter
    var dateText by remember { mutableStateOf("") }       // Date filter (YYYY-MM-DD)
    var guestText by remember { mutableStateOf("") }      // Number of guests filter
    var showFilters by remember { mutableStateOf(false) } // Whether the filters panel is open

    /**
     * Searches the database using the search bar text and all the filters.
     * Blank filters are ignored, so an empty search shows every campsite.
     */
    fun runSearch() {
        val guests = guestText.toIntOrNull()

        // A date that is blank or typed wrong is simply not used as a filter
        val date = if (dateText.isBlank()) {
            null
        } else {
            try {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateText)
            } catch (exception: Exception) {
                null
            }
        }

        campsiteDB.searchCampsites(
            name = searchText,
            location = locationText,
            date = date,
            guests = guests
        ) { results ->
            campsites = results
            isLoaded = true
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
        CampsiteSearchBar(
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            onSearch = { runSearch() }
        )

        // "Filters" button on the right, like Figma
        TextButton(
            onClick = { showFilters = !showFilters },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Filters")
        }

        if (showFilters) {
            SearchFiltersPanel(
                locationText = locationText,
                onLocationChange = { locationText = it },
                dateText = dateText,
                onDateChange = { dateText = it },
                guestText = guestText,
                onGuestChange = { guestText = it },
                onApply = {
                    runSearch()
                    showFilters = false
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
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
}

/**
 * Pill-shaped search bar. Pressing the search icon or the keyboard's
 * search key runs the search.
 *
 * @param searchText Text currently in the search bar.
 * @param onSearchTextChange Called when the text changes.
 * @param onSearch Called when the entrant asks to search.
 */
@Composable
private fun CampsiteSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = { Text("Search campsites") },
        singleLine = true,
        shape = RoundedCornerShape(50),   // Fully rounded ends
        leadingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        trailingIcon = {
            // Only show the clear button when there is text to clear
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { onSearchTextChange("") }) {
                    Icon(Icons.Outlined.Cancel, contentDescription = "Clear")
                }
            }
        },
        // Show a "search" key on the keyboard and run the search when it is pressed
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Lavender panel holding the location, date and guest filters.
 *
 * @param locationText Current location filter.
 * @param onLocationChange Called when the location filter changes.
 * @param dateText Current date filter (YYYY-MM-DD).
 * @param onDateChange Called when the date filter changes.
 * @param guestText Current number of guests filter.
 * @param onGuestChange Called when the number of guests filter changes.
 * @param onApply Called when the entrant applies the filters.
 */
@Composable
private fun SearchFiltersPanel(
    locationText: String,
    onLocationChange: (String) -> Unit,
    dateText: String,
    onDateChange: (String) -> Unit,
    guestText: String,
    onGuestChange: (String) -> Unit,
    onApply: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PineconeTextField(
                value = locationText,
                onValueChange = onLocationChange,
                label = "Location"
            )
            PineconeTextField(
                value = dateText,
                onValueChange = onDateChange,
                label = "Date (YYYY-MM-DD)"
            )
            PineconeTextField(
                value = guestText,
                onValueChange = onGuestChange,
                label = "Number of guests",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            PillButton(
                text = "Apply filters",
                onClick = onApply,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            )
        }
    }
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