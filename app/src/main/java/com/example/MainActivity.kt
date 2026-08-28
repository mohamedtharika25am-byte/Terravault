package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.data.repository.TerravaultRepository
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TerravaultApp()
            }
        }
    }
}

enum class ScreenState {
    LANDING,
    MAIN_HUB,
    PARCEL_DETAIL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerravaultApp() {
    val userProfile by TerravaultRepository.userProfile.collectAsState()
    val auditLogs by TerravaultRepository.auditLogs.collectAsState()
    val issues by TerravaultRepository.issues.collectAsState()

    val criticalIssuesCount = remember(issues) {
        issues.count { it.status == com.example.data.model.IssueStatus.OPEN && it.severity == com.example.data.model.IssueSeverity.CRITICAL }
    }

    var currentScreenState by remember { mutableStateOf(ScreenState.LANDING) }
    var selectedNavItem by remember { mutableStateOf(NavItem.DASHBOARD) }
    var activeDetailParcelId by remember { mutableStateOf<String?>("TN-COI-00123-0456") }
    var mapInitialParcelId by remember { mutableStateOf<String?>(null) }
    var reportsInitialParcelId by remember { mutableStateOf<String?>("TN-COI-00123-0456") }

    var showSearchDialog by remember { mutableStateOf(false) }
    var showNotificationDrawer by remember { mutableStateOf(false) }
    var showRoleSelectorModal by remember { mutableStateOf(false) }

    // Guided Presentation Demo Step Handler
    fun handleDemoStep(stepIndex: Int) {
        when (stepIndex) {
            0 -> {
                currentScreenState = ScreenState.MAIN_HUB
                selectedNavItem = NavItem.DASHBOARD
            }
            1 -> {
                currentScreenState = ScreenState.MAIN_HUB
                selectedNavItem = NavItem.GIS_MAP
                mapInitialParcelId = "TN-COI-00123-0456"
            }
            2 -> {
                currentScreenState = ScreenState.MAIN_HUB
                selectedNavItem = NavItem.GIS_MAP
                mapInitialParcelId = "TN-COI-00892-1102"
            }
            3 -> {
                activeDetailParcelId = "TN-COI-00123-0456"
                currentScreenState = ScreenState.PARCEL_DETAIL
            }
            4 -> {
                currentScreenState = ScreenState.MAIN_HUB
                selectedNavItem = NavItem.ISSUES
            }
            5 -> {
                reportsInitialParcelId = "TN-COI-00123-0456"
                currentScreenState = ScreenState.MAIN_HUB
                selectedNavItem = NavItem.REPORTS
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = IvoryBackground,
        topBar = {
            if (currentScreenState == ScreenState.MAIN_HUB) {
                TerravaultTopBar(
                    userProfile = userProfile,
                    onOpenSearch = { showSearchDialog = true },
                    onOpenNotifications = { showNotificationDrawer = true },
                    onOpenRoleSelector = { showRoleSelectorModal = true },
                    onStartDemoTour = { handleDemoStep(0) },
                    unreadNotificationCount = auditLogs.size.coerceAtMost(4)
                )
            }
        },
        bottomBar = {
            if (currentScreenState == ScreenState.MAIN_HUB) {
                TerravaultBottomNavigation(
                    selectedItem = selectedNavItem,
                    onItemSelected = { selectedNavItem = it },
                    criticalIssuesCount = criticalIssuesCount
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreenState) {
                ScreenState.LANDING -> {
                    LandingScreen(
                        onEnterApp = { role ->
                            currentScreenState = ScreenState.MAIN_HUB
                            selectedNavItem = NavItem.DASHBOARD
                        }
                    )
                }

                ScreenState.PARCEL_DETAIL -> {
                    ParcelDetailScreen(
                        parcelId = activeDetailParcelId ?: "TN-COI-00123-0456",
                        onBack = { currentScreenState = ScreenState.MAIN_HUB },
                        onNavigateToMap = { pId ->
                            mapInitialParcelId = pId
                            selectedNavItem = NavItem.GIS_MAP
                            currentScreenState = ScreenState.MAIN_HUB
                        },
                        onNavigateToReport = { pId ->
                            reportsInitialParcelId = pId
                            selectedNavItem = NavItem.REPORTS
                            currentScreenState = ScreenState.MAIN_HUB
                        }
                    )
                }

                ScreenState.MAIN_HUB -> {
                    when (selectedNavItem) {
                        NavItem.DASHBOARD -> {
                            DashboardScreen(
                                onNavigateToMap = { selectedNavItem = NavItem.GIS_MAP },
                                onNavigateToParcels = { selectedNavItem = NavItem.PARCELS },
                                onNavigateToIssues = { selectedNavItem = NavItem.ISSUES },
                                onNavigateToSources = { selectedNavItem = NavItem.DATA_SOURCES },
                                onNavigateToReports = { selectedNavItem = NavItem.REPORTS },
                                onNavigateToParcelDetail = { pId ->
                                    activeDetailParcelId = pId
                                    currentScreenState = ScreenState.PARCEL_DETAIL
                                },
                                onStepClicked = { handleDemoStep(it) }
                            )
                        }

                        NavItem.GIS_MAP -> {
                            GisMapScreen(
                                initialSelectedParcelId = mapInitialParcelId,
                                onNavigateToParcelDetail = { pId ->
                                    activeDetailParcelId = pId
                                    currentScreenState = ScreenState.PARCEL_DETAIL
                                },
                                onNavigateToReport = { pId ->
                                    reportsInitialParcelId = pId
                                    selectedNavItem = NavItem.REPORTS
                                }
                            )
                        }

                        NavItem.PARCELS -> {
                            ParcelsListScreen(
                                onNavigateToParcelDetail = { pId ->
                                    activeDetailParcelId = pId
                                    currentScreenState = ScreenState.PARCEL_DETAIL
                                },
                                onNavigateToMapWithParcel = { pId ->
                                    mapInitialParcelId = pId
                                    selectedNavItem = NavItem.GIS_MAP
                                }
                            )
                        }

                        NavItem.ISSUES -> {
                            IssuesScreen(
                                onNavigateToParcel = { pId ->
                                    activeDetailParcelId = pId
                                    currentScreenState = ScreenState.PARCEL_DETAIL
                                }
                            )
                        }

                        NavItem.ANALYTICS -> {
                            AnalyticsScreen()
                        }

                        NavItem.DATA_SOURCES -> {
                            DataSourcesScreen()
                        }

                        NavItem.REPORTS -> {
                            ReportsScreen(
                                initialParcelId = reportsInitialParcelId,
                                onNavigateToParcel = { pId ->
                                    activeDetailParcelId = pId
                                    currentScreenState = ScreenState.PARCEL_DETAIL
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Global Search Modal Dialog
    if (showSearchDialog) {
        SearchDialog(
            onDismiss = { showSearchDialog = false },
            onSelectParcel = { pId ->
                activeDetailParcelId = pId
                currentScreenState = ScreenState.PARCEL_DETAIL
            }
        )
    }

    // Real-Time Notification & Audit Drawer
    if (showNotificationDrawer) {
        NotificationDrawer(
            auditLogs = auditLogs,
            onDismiss = { showNotificationDrawer = false },
            onNavigateToParcel = { pId ->
                activeDetailParcelId = pId
                currentScreenState = ScreenState.PARCEL_DETAIL
            }
        )
    }

    // Role Switcher Modal Bottom Sheet
    if (showRoleSelectorModal) {
        ModalBottomSheet(
            onDismissRequest = { showRoleSelectorModal = false },
            containerColor = PureWhiteSurface,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Switch User Persona Role",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalTextPrimary
                    )
                    IconButton(onClick = { showRoleSelectorModal = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "Demonstrate TERRAVAULT from different stakeholder vantage points for the SIH 2026 jury.",
                    fontSize = 12.sp,
                    color = MutedSlate,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                UserRole.entries.forEach { role ->
                    val isSelected = userProfile.role == role
                    Surface(
                        onClick = {
                            TerravaultRepository.updateUserRole(role)
                            showRoleSelectorModal = false
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) ForestGreenLight else SurfaceVariant,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, ForestGreenPrimary) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    TerravaultRepository.updateUserRole(role)
                                    showRoleSelectorModal = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = ForestGreenPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = role.badge,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CharcoalTextPrimary
                                )
                                Text(
                                    text = when (role) {
                                        UserRole.ADMIN -> "District Collector / Special Officer (Land Policy & Governance)"
                                        UserRole.GOVERNMENT_OFFICER -> "Tahsildar / Zonal Revenue Officer (Dispute Resolution)"
                                        UserRole.REVIEWER -> "Senior Cadastral Survey Inspector (DGPS Field Resurveys)"
                                        UserRole.VIEWER -> "Citizen / Public Land Records Portal (Encumbrance & Title)"
                                    },
                                    fontSize = 11.sp,
                                    color = CharcoalTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
