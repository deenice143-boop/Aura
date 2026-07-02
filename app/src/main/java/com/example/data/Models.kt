package com.example.data

import androidx.annotation.DrawableRes

data class Stylist(
    val id: String,
    val name: String,
    val role: String,
    val rating: Double,
    val bio: String,
    val avatarInitials: String
)

data class Service(
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val durationMin: Int,
    val description: String
)

data class PortfolioItem(
    val id: String,
    @DrawableRes val imageRes: Int,
    val title: String,
    val stylistName: String,
    val initialLikes: Int,
    val isLiked: Boolean = false,
    val likesCount: Int = initialLikes,
    val category: String = "Modern Cut"
)

data class StyleDetail(
    val id: String,
    val name: String,
    val category: String, // "Modern Cut", "Classic Blowout", "Color & Balayage", "Updos"
    @DrawableRes val imageRes: Int,
    val description: String,
    val longDescription: String,
    val basePrice: Double,
    val durationMin: Int,
    val maintenanceLevel: String, // "Low", "Medium", "High"
    val hairTypeRecommended: String,
    val colorsAvailable: List<String> = emptyList(),
    val lengthOptions: List<String> = emptyList()
)

data class Booking(
    val id: String,
    val serviceName: String,
    val stylistName: String,
    val dateString: String,
    val timeSlot: String,
    val totalPrice: Double
)
