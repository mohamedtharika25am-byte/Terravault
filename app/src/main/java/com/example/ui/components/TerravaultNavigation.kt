package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int = 0
) {
    DASHBOARD("Overview", Icons.Default.Dashboard, Icons.Outlined.Dashboard),
    GIS_MAP("GIS Map", Icons.Default.Map, Icons.Outlined.Map),
    PARCELS("Parcels", Icons.Default.CropSquare, Icons.Outlined.CropSquare),
    ISSUES("Issues", Icons.Default.Warning, Icons.Outlined.WarningAmber),
    ANALYTICS("Analytics", Icons.Default.BarChart, Icons.Outlined.BarChart),
    DATA_SOURCES("Sources", Icons.Default.Hub, Icons.Outlined.Hub),
    REPORTS("Reports", Icons.Default.Description, Icons.Outlined.Description)
}

@Composable
fun TerravaultBottomNavigation(
    selectedItem: NavItem,
    onItemSelected: (NavItem) -> Unit,
    criticalIssuesCount: Int = 0,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = PureWhiteSurface,
        tonalElevation = 6.dp
    ) {
        NavItem.entries.forEach { item ->
            val isSelected = selectedItem == item
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item == NavItem.ISSUES && criticalIssuesCount > 0) {
                                Badge(
                                    containerColor = DangerRed,
                                    contentColor = Color.White
                                ) {
                                    Text("$criticalIssuesCount", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            tint = if (isSelected) ForestGreenPrimary else MutedSlate
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) ForestGreenPrimary else MutedSlate
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ForestGreenPrimary,
                    selectedTextColor = ForestGreenPrimary,
                    indicatorColor = ForestGreenLight,
                    unselectedIconColor = MutedSlate,
                    unselectedTextColor = MutedSlate
                )
            )
        }
    }
}
