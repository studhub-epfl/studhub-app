import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.presentation.ui.listing.FakeListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailedListingViewModel(private val listingId: String) : ViewModel() {
    private val listingRepository = FakeListingRepository()

    private val _detailedListingState = MutableStateFlow<Listing?>(null)
    val detailedListingState: StateFlow<Listing?> = _detailedListingState.asStateFlow()

    fun fetchListing(id: String) {
        viewModelScope.launch {
            listingRepository.getListing(id)
                .catch { throwable ->
                    // Handle error
                }
                .collect { result ->
                    when (result) {
                        is ApiResponse.Success -> {
                            _detailedListingState.value = result.data
                        }
                        is ApiResponse.Failure -> {
                            // Handle error
                        }
                        is ApiResponse.Loading -> {
                            // Handle loading
                        }
                    }
                }
        }
    }

}
