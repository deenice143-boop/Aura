package com.example.ui.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isModuleLoading by viewModel.isModuleLoading.collectAsState()
    val bookingStatus by viewModel.bookingStatus.collectAsState()

    // Find if any tab module is currently in dynamic lazy loading (Suspense) state
    val activeLoadingTab = isModuleLoading.entries.find { it.value }?.key

    // Status Dialog
    bookingStatus?.let { status ->
        AlertDialog(
            onDismissRequest = { viewModel.clearBookingStatus() },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearBookingStatus() },
                    colors = ButtonDefaults.textButtonColors(contentColor = GoldPrimary)
                ) {
                    Text("Conclude", fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    text = "AURA Reservation",
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = status,
                    color = TextLight,
                    fontSize = 16.sp
                )
            },
            containerColor = CharcoalSurface,
            textContentColor = TextLight,
            tonalElevation = 8.dp,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(CharcoalBg),
        topBar = {
            LuxuryHeader()
        },
        bottomBar = {
            LuxuryBottomBar(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CharcoalBg)
        ) {
            // Background subtle gradient circles for high-end atmosphere
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(GoldPrimary.copy(alpha = 0.08f), Color.Transparent),
                                center = Offset(size.width * 0.8f, size.height * 0.2f),
                                radius = size.minDimension * 0.6f
                            )
                        )
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(GoldSecondary.copy(alpha = 0.05f), Color.Transparent),
                                center = Offset(size.width * 0.1f, size.height * 0.8f),
                                radius = size.minDimension * 0.6f
                            )
                        )
                    }
            )

            if (activeLoadingTab != null) {
                // Suspense Fallback Screen
                SuspenseFallback(tab = activeLoadingTab)
            } else {
                // Smooth content transitions based on tab selection
                Crossfade(
                    targetState = currentTab,
                    animationSpec = tween(durationMillis = 350),
                    label = "TabTransition"
                ) { tab ->
                    when (tab) {
                        DashboardTab.BOOKING -> BookingEngineModule(viewModel = viewModel)
                        DashboardTab.PORTFOLIO -> PortfolioGalleryModule(viewModel = viewModel)
                        DashboardTab.VISUALIZER -> StyleVisualizerModule(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun LuxuryHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = CharcoalBg,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "A U R A",
                fontSize = 28.sp,
                color = GoldPrimary,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                letterSpacing = 8.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "HAUTE COUTURE SALON & BOUTIQUE",
                fontSize = 10.sp,
                color = TextMuted,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                color = GoldPrimary.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.width(60.dp)
            )
        }
    }
}

@Composable
fun LuxuryBottomBar(
    currentTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit
) {
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = CharcoalSurface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentTab == DashboardTab.PORTFOLIO,
            onClick = { onTabSelected(DashboardTab.PORTFOLIO) },
            icon = {
                Icon(
                    imageVector = if (currentTab == DashboardTab.PORTFOLIO) Icons.Filled.PhotoLibrary else Icons.Outlined.PhotoLibrary,
                    contentDescription = "Portfolio"
                )
            },
            label = { Text("Portfolio", fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnGold,
                selectedTextColor = GoldPrimary,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("portfolio_tab")
        )

        NavigationBarItem(
            selected = currentTab == DashboardTab.VISUALIZER,
            onClick = { onTabSelected(DashboardTab.VISUALIZER) },
            icon = {
                Icon(
                    imageVector = if (currentTab == DashboardTab.VISUALIZER) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                    contentDescription = "Visualizer"
                )
            },
            label = { Text("Visualizer", fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnGold,
                selectedTextColor = GoldPrimary,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("visualizer_tab")
        )

        NavigationBarItem(
            selected = currentTab == DashboardTab.BOOKING,
            onClick = { onTabSelected(DashboardTab.BOOKING) },
            icon = {
                Icon(
                    imageVector = if (currentTab == DashboardTab.BOOKING) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                    contentDescription = "Book Appointment"
                )
            },
            label = { Text("Booking", fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnGold,
                selectedTextColor = GoldPrimary,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("booking_tab")
        )
    }
}

// ==========================================
// PORTFOLIO GALLERY MODULE
// ==========================================
@Composable
fun PortfolioGalleryModule(viewModel: DashboardViewModel) {
    val items by viewModel.portfolioItems.collectAsState()
    val categories = listOf("All", "Modern Cut", "Classic Blowout", "Updos")
    var selectedCategory by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Curated Stylist Portfolio",
            fontSize = 20.sp,
            color = TextLight,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        Text(
            text = "Witness the high-fashion work crafted by our master stylists. Tap the heart to express appreciation.",
            fontSize = 13.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Luxury Category Selection Pills Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) GoldPrimary else CharcoalSurface,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) GoldPrimary else CharcoalBorder,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) OnGold else TextLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Motion library AnimatedContent for seamless grid transitions
        AnimatedContent(
            targetState = selectedCategory,
            transitionSpec = {
                (fadeIn(animationSpec = tween(350, easing = EaseOutQuad)) + 
                 scaleIn(initialScale = 0.95f, animationSpec = tween(350, easing = EaseOutQuad)))
                    .togetherWith(fadeOut(animationSpec = tween(200, easing = EaseInQuad)))
            },
            label = "PortfolioCategoryTransition",
            modifier = Modifier.weight(1f)
        ) { targetCategory ->
            val filteredItems = if (targetCategory == "All") {
                items
            } else {
                items.filter { it.category == targetCategory }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    PortfolioItemCard(
                        item = item,
                        onLikeClicked = { viewModel.toggleLikePortfolioItem(item.id) },
                        onStyleInspectClicked = {
                            val matchedStyle = viewModel.styleDetails.find { style -> style.name == item.title }
                            if (matchedStyle != null) {
                                viewModel.selectVisualizerStyle(matchedStyle)
                                viewModel.selectTab(DashboardTab.VISUALIZER)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PortfolioItemCard(
    item: PortfolioItem,
    onLikeClicked: () -> Unit,
    onStyleInspectClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CharcoalBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onStyleInspectClicked),
        colors = CardDefaults.cardColors(containerColor = CharcoalSurface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Category Tag
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(CharcoalBg.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "MAÎTRE",
                        fontSize = 9.sp,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Like Floating Button
                IconButton(
                    onClick = onLikeClicked,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(CharcoalBg.copy(alpha = 0.7f), CircleShape)
                        .size(36.dp)
                        .testTag("like_button_${item.id}")
                ) {
                    Icon(
                        imageVector = if (item.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (item.isLiked) Color.Red else GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    color = TextLight,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "by ${item.stylistName}",
                    fontSize = 12.sp,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Likes",
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${item.likesCount} appreciations",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Inspect Style",
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// STYLE VISUALIZER MODULE (INTERACTIVE PREVIEW)
// ==========================================
@Composable
fun StyleVisualizerModule(viewModel: DashboardViewModel) {
    val selectedStyle by viewModel.selectedStyle.collectAsState()
    val selectedColor by viewModel.selectedStyleColor.collectAsState()
    val selectedLength by viewModel.selectedStyleLength.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Modern Cut", "Classic Blowout", "Updos")

    val filteredStyles = if (selectedCategory == "All") {
        viewModel.styleDetails
    } else {
        viewModel.styleDetails.filter { it.category == selectedCategory }
    }

    // Auto-select the first style of the new category to ensure real-time UI updates
    LaunchedEffect(selectedCategory) {
        if (selectedCategory != "All") {
            val stylesInCat = viewModel.styleDetails.filter { it.category == selectedCategory }
            if (stylesInCat.isNotEmpty() && !stylesInCat.any { it.id == selectedStyle.id }) {
                viewModel.selectVisualizerStyle(stylesInCat.first())
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Interactive Style Studio",
            fontSize = 20.sp,
            color = TextLight,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Preview elite hair profiles, customize colors & texturing options, and book your signature look.",
            fontSize = 13.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Horizontal selectors for Style Categories
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) GoldPrimary else CharcoalSurface,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) GoldPrimary else CharcoalBorder,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) OnGold else TextLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Horizontal selectors for individual Styles
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            items(filteredStyles) { style ->
                val isSelected = style.id == selectedStyle.id
                Surface(
                    onClick = { viewModel.selectVisualizerStyle(style) },
                    color = if (isSelected) GoldPrimary else CharcoalSurface,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (isSelected) GoldPrimary else CharcoalBorder),
                    modifier = Modifier
                        .height(44.dp)
                        .testTag("style_card_${style.id}")
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = style.name,
                            color = if (isSelected) OnGold else TextLight,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Feature Preview Card with overlapped text layout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = CharcoalSurface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    Image(
                        painter = painterResource(id = selectedStyle.imageRes),
                        contentDescription = selectedStyle.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Overlay showing category
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(CharcoalBg.copy(alpha = 0.85f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = selectedStyle.category.uppercase(),
                            fontSize = 10.sp,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }

                    // Price overlay
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(GoldPrimary, RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        Text(
                            text = "EST. $${selectedStyle.basePrice.toInt()}",
                            fontSize = 13.sp,
                            color = OnGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = selectedStyle.name,
                        fontSize = 22.sp,
                        color = TextLight,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedStyle.longDescription,
                        fontSize = 14.sp,
                        color = TextMuted,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = CharcoalBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick specs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("MAINTENANCE", fontSize = 10.sp, color = GoldSecondary, fontWeight = FontWeight.Bold)
                            Text(selectedStyle.maintenanceLevel, fontSize = 13.sp, color = TextLight)
                        }
                        Column {
                            Text("RECOMMENDED FOR", fontSize = 10.sp, color = GoldSecondary, fontWeight = FontWeight.Bold)
                            Text(selectedStyle.hairTypeRecommended, fontSize = 13.sp, color = TextLight)
                        }
                        Column {
                            Text("DURATION", fontSize = 10.sp, color = GoldSecondary, fontWeight = FontWeight.Bold)
                            Text("${selectedStyle.durationMin} mins", fontSize = 13.sp, color = TextLight)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // INTERACTIVE OPTIONS CONTROL PANEL (STATE MODIFIERS)
        Text(
            text = "Tailor Your Styling Settings",
            fontSize = 16.sp,
            color = GoldPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CharcoalBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = CharcoalSurface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Color modifiers
                Text(
                    text = "Dye Tint & Highlights",
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    selectedStyle.colorsAvailable.forEach { color ->
                        val isSelectedColor = color == selectedColor
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelectedColor) GoldPrimary.copy(alpha = 0.2f) else CharcoalBg)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelectedColor) GoldPrimary else CharcoalBorder,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable { viewModel.setVisualizerColor(color) }
                                .testTag("color_option_$color"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = color,
                                fontSize = 11.sp,
                                color = if (isSelectedColor) GoldPrimary else TextLight,
                                fontWeight = if (isSelectedColor) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Texturing modifiers
                Text(
                    text = "Texturing & Finish Profiles",
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    selectedStyle.lengthOptions.forEach { length ->
                        val isSelectedLength = length == selectedLength
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelectedLength) GoldPrimary.copy(alpha = 0.2f) else CharcoalBg)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelectedLength) GoldPrimary else CharcoalBorder,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable { viewModel.setVisualizerLength(length) }
                                .testTag("length_option_$length"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = length,
                                fontSize = 10.sp,
                                color = if (isSelectedLength) GoldPrimary else TextLight,
                                fontWeight = if (isSelectedLength) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dynamic live configuration summary
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CharcoalBg, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Summary",
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Configured: $selectedColor (${selectedLength})",
                            fontSize = 12.sp,
                            color = TextLight,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Request Look trigger (Direct Booking deep link!)
        Button(
            onClick = { viewModel.triggerBookingFromStyle(selectedStyle) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("request_look_button"),
            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = OnGold),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "RESERVE THIS CUSTOM LOOK",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to Booking"
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

// ==========================================
// BOOKING ENGINE MODULE
// ==========================================
@Composable
fun BookingEngineModule(viewModel: DashboardViewModel) {
    val selectedService by viewModel.selectedService.collectAsState()
    val selectedStylist by viewModel.selectedStylist.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTime by viewModel.selectedTimeSlot.collectAsState()
    val bookings by viewModel.bookings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "AURA Reservation Engine",
            fontSize = 20.sp,
            color = TextLight,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Select high-end services, choose your private stylist, and book your custom slot below.",
            fontSize = 13.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 1. Service Selection list
        Text(
            text = "1. SELECT COUTURE SERVICE",
            fontSize = 12.sp,
            color = GoldPrimary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        viewModel.services.forEach { srv ->
            val isSelected = selectedService?.id == srv.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) GoldPrimary else CharcoalBorder,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { viewModel.selectService(srv) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) GoldPrimary.copy(alpha = 0.08f) else CharcoalSurface
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { viewModel.selectService(srv) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = GoldPrimary,
                            unselectedColor = TextMuted
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(srv.name, fontWeight = FontWeight.Bold, color = TextLight, fontSize = 14.sp)
                        Text(srv.description, color = TextMuted, fontSize = 11.sp, lineHeight = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text("$${srv.price.toInt()}", fontWeight = FontWeight.Bold, color = GoldSecondary, fontSize = 14.sp)
                        Text("${srv.durationMin}m", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Stylist Selection list
        Text(
            text = "2. CHOOSE PRIVATE STYLIST",
            fontSize = 12.sp,
            color = GoldPrimary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(viewModel.stylists) { stylist ->
                val isSelected = selectedStylist?.id == stylist.id
                Card(
                    modifier = Modifier
                        .width(130.dp)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) GoldPrimary else CharcoalBorder,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.selectStylist(stylist) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) GoldPrimary.copy(alpha = 0.08f) else CharcoalSurface
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Styled Initials Avatar instead of blank image
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(if (isSelected) GoldPrimary else CharcoalBg, CircleShape)
                                .border(1.dp, GoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stylist.avatarInitials,
                                color = if (isSelected) OnGold else GoldPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stylist.name,
                            fontWeight = FontWeight.Bold,
                            color = TextLight,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            stylist.role,
                            color = TextMuted,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, contentDescription = "Rating", tint = GoldPrimary, modifier = Modifier.size(11.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(stylist.rating.toString(), color = TextLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Minimal Calendar & Time Picker
        Text(
            text = "3. RESERVATION DATE & TIME",
            fontSize = 12.sp,
            color = GoldPrimary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Minimal calendar builder (generates next 10 days horizontally!)
        val datesList = remember {
            val sdf = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
            val list = mutableListOf<String>()
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 1) // Start from tomorrow
            for (i in 0..9) {
                list.add(sdf.format(calendar.time))
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            list
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(datesList) { dateStr ->
                val isSelected = selectedDate == dateStr
                val dateParts = dateStr.split(", ")
                val weekday = dateParts[0]
                val dayMonth = dateParts[1]

                Card(
                    modifier = Modifier
                        .width(72.dp)
                        .height(64.dp)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) GoldPrimary else CharcoalBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { viewModel.selectDate(dateStr) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) GoldPrimary else CharcoalSurface
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = weekday.uppercase(),
                            fontSize = 10.sp,
                            color = if (isSelected) OnGold else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dayMonth,
                            fontSize = 12.sp,
                            color = if (isSelected) OnGold else TextLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Slots Scroll
        val timeSlots = listOf("09:00 AM", "10:15 AM", "11:30 AM", "01:00 PM", "02:15 PM", "03:30 PM", "04:45 PM", "06:00 PM")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(timeSlots) { slot ->
                val isSelected = selectedTime == slot
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) GoldPrimary.copy(alpha = 0.2f) else CharcoalSurface)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) GoldPrimary else CharcoalBorder,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { viewModel.selectTimeSlot(slot) }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = slot,
                        fontSize = 12.sp,
                        color = if (isSelected) GoldPrimary else TextLight,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Confirm Reservation Action Button
        Button(
            onClick = { viewModel.bookAppointment() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("book_button"),
            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = OnGold),
            shape = RoundedCornerShape(12.dp),
            enabled = true
        ) {
            Text(
                text = "CONFIRM HAUTE RESERVATION",
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
        }

        // 4. Current Bookings Summary section
        if (bookings.isNotEmpty()) {
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "YOUR ACTIVE APPOINTMENTS",
                fontSize = 12.sp,
                color = GoldPrimary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            bookings.forEach { booking ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = CharcoalSurface),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(GoldPrimary.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.VerifiedUser,
                                contentDescription = "Active",
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = booking.serviceName,
                                fontWeight = FontWeight.Bold,
                                color = TextLight,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "with ${booking.stylistName}",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${booking.dateString} at ${booking.timeSlot}",
                                color = GoldSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "$${booking.totalPrice.toInt()}",
                                fontWeight = FontWeight.Bold,
                                color = TextLight,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            IconButton(
                                onClick = { viewModel.cancelBooking(booking.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = "Cancel",
                                    tint = Color.Red.copy(alpha = 0.7f),
                                    modifier = Modifier.size(18.dp)
                               )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

// ==========================================
// LUXURY SUSPENSE & LAZY SKELETON LOADERS
// ==========================================
@Composable
fun SuspenseFallback(tab: DashboardTab) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // Luxury glowing circular loader
        CircularProgressIndicator(
            color = GoldPrimary,
            strokeWidth = 2.dp,
            modifier = Modifier.size(44.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "S U S P E N S E",
            fontSize = 11.sp,
            color = GoldPrimary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        
        val loadingMsg = when (tab) {
            DashboardTab.BOOKING -> "Asynchronously downloading reservation engine..."
            DashboardTab.PORTFOLIO -> "Fetching high-resolution style catalog pieces..."
            DashboardTab.VISUALIZER -> "Compiling styling studio variables & layouts..."
        }
        
        Text(
            text = loadingMsg,
            fontSize = 12.sp,
            color = TextMuted,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Render corresponding skeleton UI matching the module layout perfectly
        when (tab) {
            DashboardTab.BOOKING -> BookingSkeleton()
            DashboardTab.PORTFOLIO -> PortfolioSkeleton()
            DashboardTab.VISUALIZER -> VisualizerSkeleton()
        }
    }
}

@Composable
fun ShimmerCard(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "ShimmerTransition")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AlphaAnimation"
    )
    
    Box(
        modifier = modifier
            .background(CharcoalCard.copy(alpha = alpha), RoundedCornerShape(12.dp))
            .border(1.dp, CharcoalBorder.copy(alpha = alpha), RoundedCornerShape(12.dp))
    )
}

@Composable
fun BookingSkeleton() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section header skeleton
        ShimmerCard(modifier = Modifier.width(180.dp).height(16.dp))
        
        // List items
        repeat(3) {
            ShimmerCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
            )
        }
    }
}

@Composable
fun PortfolioSkeleton() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ShimmerCard(modifier = Modifier.width(150.dp).height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ShimmerCard(modifier = Modifier.fillMaxWidth().height(160.dp))
                ShimmerCard(modifier = Modifier.fillMaxWidth().height(200.dp))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ShimmerCard(modifier = Modifier.fillMaxWidth().height(200.dp))
                ShimmerCard(modifier = Modifier.fillMaxWidth().height(160.dp))
            }
        }
    }
}

@Composable
fun VisualizerSkeleton() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ShimmerCard(modifier = Modifier.width(160.dp).height(16.dp))
        
        // Category selectors skeleton
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            repeat(3) {
                ShimmerCard(modifier = Modifier.weight(1f).height(40.dp))
            }
        }
        
        // Large hero card
        ShimmerCard(modifier = Modifier.fillMaxWidth().height(240.dp))
        
        // Details row
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            repeat(3) {
                ShimmerCard(modifier = Modifier.weight(1f).height(34.dp))
            }
        }
    }
}
