import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.studhub.app.domain.model.Listing
import com.studhub.app.presentation.listing.browse.components.ListingCard

@Composable
fun BrowseContent(listings: List<Listing>, navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        ) {
            items(listings) { listing ->
                ListingCard(
                    listing = listing,
                    onClick = {
                        navController.navigate("Listing/${listing.id}")
                    })
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}
