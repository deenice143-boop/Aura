package com.example.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class DashboardTab {
    BOOKING,
    PORTFOLIO,
    VISUALIZER
}

class DashboardViewModel : ViewModel() {

    // Tab State
    private val _currentTab = MutableStateFlow(DashboardTab.PORTFOLIO) // Default to Portfolio to showcase beautiful visual assets first!
    val currentTab: StateFlow<DashboardTab> = _currentTab.asStateFlow()

    // Simulated React.lazy & Suspense chunk loading states for each tab
    private val _isModuleLoading = MutableStateFlow<Map<DashboardTab, Boolean>>(
        mapOf(
            DashboardTab.PORTFOLIO to false,
            DashboardTab.VISUALIZER to false,
            DashboardTab.BOOKING to false
        )
    )
    val isModuleLoading: StateFlow<Map<DashboardTab, Boolean>> = _isModuleLoading.asStateFlow()

    private val loadedModules = mutableSetOf<DashboardTab>()

    // Base Master Data
    val stylists = listOf(
        Stylist("st_1", "Elena Rostova", "Master Stylist", 4.9, "Elena has 12 years of experience specializing in custom couture cuts and French coloring techniques.", "ER"),
        Stylist("st_2", "Alexander Mercer", "Artistic Director", 5.0, "Alexander is known for high-fashion runway blowouts and sophisticated editorial hairstyling.", "AM"),
        Stylist("st_3", "Marcus Vance", "Senior Creative Artist", 4.8, "Marcus is a master of precision geometrical haircuts, sharp bobs, and modern asymmetrical styles.", "MV"),
        Stylist("st_4", "Sophia Loren", "Special Event Stylist", 4.9, "Sophia excels in romantic bridal updos, intricate braids, and luxury red-carpet hair arrangements.", "SL")
    )

    val services = listOf(
        Service("srv_1", "Signature Couture Cut", "Cuts", 150.0, 60, "Tailored scissor or razor haircut, luxury scalp massage, and bespoke signature blowout finish."),
        Service("srv_2", "Hollywood Glam Blowout", "Blowouts", 95.0, 45, "Luxurious high-volume shampoo, custom protein hair masking, and glamour wave round-brush style."),
        Service("srv_3", "Golden Balayage & Melt", "Color", 320.0, 150, "Bespoke hand-painted luxury golden color highlights finished with a custom toning glaze."),
        Service("srv_4", "Restorative Caviar Treatment", "Treatment", 180.0, 75, "Intense cellular restoration therapy infused with luxury caviar lipids and advanced steam hydration.")
    )

    private val _portfolioItems = MutableStateFlow<List<PortfolioItem>>(emptyList())
    val portfolioItems: StateFlow<List<PortfolioItem>> = _portfolioItems.asStateFlow()

    val styleDetails = listOf(
        StyleDetail(
            id = "style_1",
            name = "Signature French Bob",
            category = "Modern Cut",
            imageRes = R.drawable.img_portfolio_1_1782419113311,
            description = "A sophisticated chin-grazing French bob characterized by texturized ends and effortless movement.",
            longDescription = "Our premier modern cut. The French Bob is customized to frame your cheekbones perfectly. Includes our signature texturizing technique to ensure low daily maintenance while retaining an incredibly high-fashion look.",
            basePrice = 150.0,
            durationMin = 60,
            maintenanceLevel = "Low to Medium",
            hairTypeRecommended = "Straight, Wavy, or Fine Hair",
            colorsAvailable = listOf("Chocolat Noir", "Honey Blonde", "Muted Rose Gold", "Natural Onyx"),
            lengthOptions = listOf("Classic Jawline", "Modern Cheekbone", "Textured Lob")
        ),
        StyleDetail(
            id = "style_2",
            name = "Hollywood Glam Blowout",
            category = "Classic Blowout",
            imageRes = R.drawable.img_portfolio_2_1782419126099,
            description = "High-volume, cascading glamour waves with exceptional natural shine and professional hold.",
            longDescription = "The ultimate red-carpet blowout. Designed to maximize your hair's natural density, incorporating a custom silk-protein treatment before styling with iconic ceramic round brushes for premium bounce and shine.",
            basePrice = 95.0,
            durationMin = 45,
            maintenanceLevel = "Temporary (Event-ready)",
            hairTypeRecommended = "All Hair Types & Lengths",
            colorsAvailable = listOf("Golden Highlights", "Satin Pearl Gloss", "Warm Amber", "No Tint"),
            lengthOptions = listOf("Max Volume Bounce", "Sleek Cascading Waves", "Structured Curls")
        ),
        StyleDetail(
            id = "style_3",
            name = "Asymmetrical Chic Chop",
            category = "Modern Cut",
            imageRes = R.drawable.img_portfolio_3_1782419135795,
            description = "A bold, avant-garde geometrical haircut featuring asymmetrical lines and razor-sharp texturizing.",
            longDescription = "A striking statement hairstyle crafted for trendsetters. Marcus Vance performs precision measurements to shape a sharp, custom-angled side silhouette that highlights your facial structure beautifully.",
            basePrice = 165.0,
            durationMin = 75,
            maintenanceLevel = "Medium (requires trim every 6 weeks)",
            hairTypeRecommended = "Thick, Medium, or Straight Hair",
            colorsAvailable = listOf("Platinum Frost", "Midnight Obsidian", "Copper Ember", "Amethyst Haze"),
            lengthOptions = listOf("Dramatic Angle", "Subtle Sleek Gradient", "Bold Razor Edged")
        ),
        StyleDetail(
            id = "style_4",
            name = "Gilded Braided Crown",
            category = "Updos",
            imageRes = R.drawable.img_portfolio_4_1782419147166,
            description = "A majestic romantic updo featuring loose bohemian braiding decorated with delicate floral or gold pin accents.",
            longDescription = "Perfect for weddings, exclusive events, or black-tie gala nights. Sophia custom weaves a gorgeous crown braid tailored to your hair's volume, incorporating luxury subtle floral or golden details.",
            basePrice = 195.0,
            durationMin = 90,
            maintenanceLevel = "Single Occasion",
            hairTypeRecommended = "Medium to Long Hair",
            colorsAvailable = listOf("Antique Brass Pins", "Gilded Gold Leaves", "Fresh Pearl Accents", "Classic Minimalist"),
            lengthOptions = listOf("Bohemian Fishtail Crown", "Double Dutch Braided Updo", "Classic Roman Halo")
        )
    )

    // Style Visualizer State
    private val _selectedStyle = MutableStateFlow(styleDetails[0])
    val selectedStyle: StateFlow<StyleDetail> = _selectedStyle.asStateFlow()

    private val _selectedStyleColor = MutableStateFlow(styleDetails[0].colorsAvailable.first())
    val selectedStyleColor: StateFlow<String> = _selectedStyleColor.asStateFlow()

    private val _selectedStyleLength = MutableStateFlow(styleDetails[0].lengthOptions.first())
    val selectedStyleLength: StateFlow<String> = _selectedStyleLength.asStateFlow()

    // Booking State
    private val _selectedService = MutableStateFlow<Service?>(null)
    val selectedService: StateFlow<Service?> = _selectedService.asStateFlow()

    private val _selectedStylist = MutableStateFlow<Stylist?>(null)
    val selectedStylist: StateFlow<Stylist?> = _selectedStylist.asStateFlow()

    private val _selectedDate = MutableStateFlow("")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedTimeSlot = MutableStateFlow("")
    val selectedTimeSlot: StateFlow<String> = _selectedTimeSlot.asStateFlow()

    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    private val _bookingStatus = MutableStateFlow<String?>(null)
    val bookingStatus: StateFlow<String?> = _bookingStatus.asStateFlow()

    init {
        // Initialize Portfolio Items with mock data and specific categories
        _portfolioItems.value = listOf(
            PortfolioItem("p_1", R.drawable.img_portfolio_1_1782419113311, "Signature French Bob", "Elena Rostova", 142, category = "Modern Cut"),
            PortfolioItem("p_2", R.drawable.img_portfolio_2_1782419126099, "Hollywood Glam Blowout", "Alexander Mercer", 328, category = "Classic Blowout"),
            PortfolioItem("p_3", R.drawable.img_portfolio_3_1782419135795, "Asymmetrical Chic Chop", "Marcus Vance", 94, category = "Modern Cut"),
            PortfolioItem("p_4", R.drawable.img_portfolio_4_1782419147166, "Gilded Braided Crown", "Sophia Loren", 215, category = "Updos")
        )

        // Leave initial selections null to allow validation checks on booking submission
        _selectedService.value = null
        _selectedStylist.value = null

        // Initialize with tomorrow's date
        val sdf = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        _selectedDate.value = sdf.format(calendar.time)
        _selectedTimeSlot.value = "11:30 AM"

        // Portfolio tab starts pre-loaded so initial launch is immediate
        loadedModules.add(DashboardTab.PORTFOLIO)
    }

    // Tab Action with simulated React.lazy dynamic loading (Suspense fallback)
    fun selectTab(tab: DashboardTab) {
        if (!loadedModules.contains(tab)) {
            viewModelScope.launch {
                _isModuleLoading.value = _isModuleLoading.value.toMutableMap().apply { put(tab, true) }
                // Simulate asynchronous bundle chunk download (e.g. 750ms)
                delay(750)
                loadedModules.add(tab)
                _isModuleLoading.value = _isModuleLoading.value.toMutableMap().apply { put(tab, false) }
                _currentTab.value = tab
            }
        } else {
            _currentTab.value = tab
        }
    }

    // Portfolio Action
    fun toggleLikePortfolioItem(itemId: String) {
        _portfolioItems.value = _portfolioItems.value.map { item ->
            if (item.id == itemId) {
                val isNowLiked = !item.isLiked
                item.copy(
                    isLiked = isNowLiked,
                    likesCount = if (isNowLiked) item.likesCount + 1 else item.likesCount - 1
                )
            } else {
                item
            }
        }
    }

    // Style Visualizer Actions
    fun selectVisualizerStyle(style: StyleDetail) {
        _selectedStyle.value = style
        _selectedStyleColor.value = style.colorsAvailable.firstOrNull() ?: ""
        _selectedStyleLength.value = style.lengthOptions.firstOrNull() ?: ""
    }

    fun setVisualizerColor(color: String) {
        _selectedStyleColor.value = color
    }

    fun setVisualizerLength(length: String) {
        _selectedStyleLength.value = length
    }

    // Booking Actions
    fun selectService(service: Service) {
        _selectedService.value = service
    }

    fun selectStylist(stylist: Stylist) {
        _selectedStylist.value = stylist
    }

    fun selectDate(dateStr: String) {
        _selectedDate.value = dateStr
    }

    fun selectTimeSlot(slot: String) {
        _selectedTimeSlot.value = slot
    }

    fun triggerBookingFromStyle(style: StyleDetail) {
        // Map visualizer style to active service if possible
        val matchedService = services.find { it.name.contains(style.name) || style.name.contains(it.name) }
            ?: services.first()
        _selectedService.value = matchedService

        // Select stylist if matched
        val matchedStylist = stylists.find { it.name == style.name } ?: stylists.first()
        _selectedStylist.value = matchedStylist

        // Navigate to booking tab via selectTab to ensure simulated Suspense is triggered
        selectTab(DashboardTab.BOOKING)
    }

    fun bookAppointment() {
        val service = _selectedService.value
        val stylist = _selectedStylist.value
        val date = _selectedDate.value
        val time = _selectedTimeSlot.value

        if (service == null) {
            _bookingStatus.value = "Please select a couture service to continue."
            return
        }
        if (stylist == null) {
            _bookingStatus.value = "Please choose a private artisan stylist."
            return
        }
        if (date.isEmpty() || time.isEmpty()) {
            _bookingStatus.value = "Please complete your slot date and time selection."
            return
        }

        val newBooking = Booking(
            id = UUID.randomUUID().toString(),
            serviceName = service.name,
            stylistName = stylist.name,
            dateString = date,
            timeSlot = time,
            totalPrice = service.price
        )

        _bookings.value = _bookings.value + newBooking
        _bookingStatus.value = "SUCCESS: Your luxury styling appointment with ${stylist.name} for ${service.name} has been secured!"
    }

    fun clearBookingStatus() {
        _bookingStatus.value = null
    }

    fun cancelBooking(bookingId: String) {
        _bookings.value = _bookings.value.filter { it.id != bookingId }
    }
}
