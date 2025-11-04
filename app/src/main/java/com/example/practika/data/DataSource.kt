package com.example.practika.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.practika.R
import java.util.Date

// --- DATA MODELS ---
data class Provider(val name: String, val logo: Any, val tollFreeNumber: String, val category: String)
data class Category(val name: String, val icon: ImageVector)
data class CallHistory(
    val id: String,
    val providerName: String,
    val category: String,
    val duration: String,
    val date: Date,
    val providerLogo: Any
)

// --- SAMPLE DATA ---

val allProviders = listOf(
    Provider(name = "Airtel", logo = R.drawable.airtel, tollFreeNumber = "121", category = "Mobile Networks"),
    Provider(name = "TNM", logo = R.drawable.tnm, tollFreeNumber = "105", category = "Mobile Networks"),
    Provider(name = "Police", logo = R.drawable.pollice, tollFreeNumber = "997", category = "Security"),
    Provider(name = "ESCOM", logo = R.drawable.escom, tollFreeNumber = "8000", category = "Utilities"),
    Provider(name = "Medical", logo = Icons.Default.LocalHospital, tollFreeNumber = "998", category = "Health")
)

val allCategories = listOf(
    Category(name = "Mobile Networks", icon = Icons.Default.Phone),
    Category(name = "Security", icon = Icons.Default.Security),
    Category(name = "Health", icon = Icons.Default.LocalHospital),
    Category(name = "Utilities", icon = Icons.Default.Business)
)

fun getProvidersForCategory(categoryName: String): List<Provider> {
    return allProviders.filter { it.category == categoryName }
}

object UserData {
    var phoneNumber: String? = null
}
