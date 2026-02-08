package com.convertly.app.ui.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.convertly.app.BuildConfig
import com.convertly.app.billing.BillingManager
import com.convertly.app.ui.theme.accentColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    billingManager: BillingManager,
    onBack: () -> Unit
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val accentColor by viewModel.accentColor.collectAsState()
    val decimalPlaces by viewModel.decimalPlaces.collectAsState()
    val hapticFeedback by viewModel.hapticFeedback.collectAsState()
    val soundEffects by viewModel.soundEffects.collectAsState()
    val thousandSeparator by viewModel.thousandSeparator.collectAsState()
    val adsRemoved by viewModel.adsRemoved.collectAsState()
    val purchaseState by billingManager.purchaseState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ===== APPEARANCE =====
            SettingsSectionHeader("Appearance")

            // Theme
            SettingsItem(
                icon = Icons.Default.DarkMode,
                title = "Theme",
                subtitle = when (themeMode) {
                    "light" -> "Light"
                    "dark" -> "Dark"
                    else -> "System Default"
                }
            ) {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(
                            when (themeMode) {
                                "light" -> "Light"
                                "dark" -> "Dark"
                                else -> "System"
                            }
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("System Default") }, onClick = {
                            viewModel.setThemeMode("system"); expanded = false
                        })
                        DropdownMenuItem(text = { Text("Light") }, onClick = {
                            viewModel.setThemeMode("light"); expanded = false
                        })
                        DropdownMenuItem(text = { Text("Dark") }, onClick = {
                            viewModel.setThemeMode("dark"); expanded = false
                        })
                    }
                }
            }

            // Accent color
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Palette,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Accent Color", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(start = 40.dp)
                ) {
                    items(accentColors) { option ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(option.light)
                                .then(
                                    if (accentColor == option.name)
                                        Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                    else Modifier
                                )
                                .clickable { viewModel.setAccentColor(option.name) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (accentColor == option.name) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            @Suppress("DEPRECATION")
            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // ===== CONVERSION =====
            SettingsSectionHeader("Conversion")

            // Decimal places
            SettingsItem(
                icon = Icons.Default.Pin,
                title = "Decimal Places",
                subtitle = "$decimalPlaces"
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (decimalPlaces > 0) viewModel.setDecimalPlaces(decimalPlaces - 1) },
                        enabled = decimalPlaces > 0
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    Text(
                        "$decimalPlaces",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { if (decimalPlaces < 10) viewModel.setDecimalPlaces(decimalPlaces + 1) },
                        enabled = decimalPlaces < 10
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }

            // Thousand separator
            SettingsToggle(
                icon = Icons.Default.FormatListNumbered,
                title = "Thousand Separator",
                subtitle = "Show commas in large numbers",
                checked = thousandSeparator,
                onCheckedChange = { viewModel.setThousandSeparator(it) }
            )

            @Suppress("DEPRECATION")
            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // ===== FEEDBACK =====
            SettingsSectionHeader("Feedback")

            SettingsToggle(
                icon = Icons.Default.Vibration,
                title = "Haptic Feedback",
                subtitle = "Vibrate on button presses",
                checked = hapticFeedback,
                onCheckedChange = { viewModel.setHapticFeedback(it) }
            )

            SettingsToggle(
                icon = Icons.Default.VolumeUp,
                title = "Sound Effects",
                subtitle = "Play sounds on actions",
                checked = soundEffects,
                onCheckedChange = { viewModel.setSoundEffects(it) }
            )

            @Suppress("DEPRECATION")
            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // ===== PREMIUM =====
            SettingsSectionHeader("Premium")

            if (!adsRemoved) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            billingManager.launchPurchaseFlow(context as Activity) { success ->
                                if (success) {
                                    viewModel.setAdsRemoved(true)
                                    Toast
                                        .makeText(context, "Ads removed! Thank you!", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Remove Ads",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "One-time purchase for lifetime ad-free experience",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        val price = when (val state = purchaseState) {
                            is BillingManager.PurchaseState.Available -> state.price
                            else -> "$0.99"
                        }
                        FilledTonalButton(
                            onClick = {
                                billingManager.launchPurchaseFlow(context as Activity) { success ->
                                    if (success) {
                                        viewModel.setAdsRemoved(true)
                                    }
                                }
                            }
                        ) {
                            Text(price)
                        }
                    }
                }
            } else {
                SettingsItem(
                    icon = Icons.Default.CheckCircle,
                    title = "Ads Removed",
                    subtitle = "You're enjoying an ad-free experience"
                ) {}
            }

            // Restore purchases
            SettingsClickableItem(
                icon = Icons.Default.Restore,
                title = "Restore Purchases",
                subtitle = "Restore previous ad removal purchase",
                onClick = {
                    billingManager.restorePurchases { success ->
                        if (success) {
                            viewModel.setAdsRemoved(true)
                            Toast.makeText(context, "Purchase restored!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "No previous purchases found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )

            @Suppress("DEPRECATION")
            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // ===== ABOUT =====
            SettingsSectionHeader("About")

            SettingsClickableItem(
                icon = Icons.Default.Star,
                title = "Rate Convertly",
                subtitle = "Leave a review on Google Play",
                onClick = {
                    try {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))
                        )
                    } catch (_: Exception) {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}"))
                        )
                    }
                }
            )

            SettingsClickableItem(
                icon = Icons.Default.Share,
                title = "Share Convertly",
                subtitle = "Tell your friends about Convertly",
                onClick = {
                    val shareIntent = Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out Convertly - a simple unit converter app! https://play.google.com/store/apps/details?id=${context.packageName}")
                    }, "Share via")
                    context.startActivity(shareIntent)
                }
            )

            SettingsItem(
                icon = Icons.Default.Info,
                title = "Version",
                subtitle = BuildConfig.VERSION_NAME
            ) {}

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        trailing()
    }
}

@Composable
fun SettingsToggle(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
