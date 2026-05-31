package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.TaskEntity
import com.example.ui.TaskViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

// Custom Glassmorphism Border Modifier
fun Modifier.glassmorphismBorder(
    shape: CornerBasedShape = RoundedCornerShape(16.dp),
    borderWidth: Dp = 1.dp
) = this.border(
    width = borderWidth,
    brush = Brush.verticalGradient(
        colors = listOf(
            Color(0x3BFFFFFF), // Crisp light glass highlight on top
            Color(0x0FA9C7D8)  // Ambient Moonstone tint at the bottom
        )
    ),
    shape = shape
)

// Glassmorphism Card Modifier representing premium Moonstone Glass card backing
fun Modifier.glassmorphismCard(
    shape: CornerBasedShape = RoundedCornerShape(16.dp)
) = this
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0x1F111821), // Graphite translucent backing
                Color(0x0F050607)  // Obsidian backing
            )
        ),
        shape = shape
    )
    .glassmorphismBorder(shape)

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val viewModel: TaskViewModel = viewModel()
    val scope = rememberCoroutineScope()

    // Splash screen bootup state for outstanding launcher branding experience
    var showSplash by remember { mutableStateOf(true) }
    var splashAlpha by remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) {
        delay(1800)
        // Smoothly fade out build console
        animate(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        ) { value, _ ->
            splashAlpha = value
        }
        showSplash = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // App Layout Frame
        AppLayout(viewModel = viewModel)

        // Boot-Up Splash HUD Overlay
        if (showSplash) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(splashAlpha)
                    .background(ObsidianBlack),
                contentAlignment = Alignment.Center
            ) {
                // Background mesh grid on booting
                GridSpaceBackground(modifier = Modifier.fillMaxSize())

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Custom Canvas reticle logo rotating slowly
                    val infiniteTransition = rememberInfiniteTransition(label = "spin")
                    val rotation by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(8000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "rotation"
                    )

                    HudReticle(
                        modifier = Modifier
                            .size(140.dp)
                            .rotate(rotation),
                        color = MoonstoneBlue
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "PROOFBOARD OS",
                        color = MoonstoneIce,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "MISSION CONTROL // VERSION 2.0",
                        color = MoonstoneBlue.copy(alpha = 0.65f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(34.dp))

                    // Progress Loader status indic
                    CircularProgressIndicator(
                        color = MoonstoneBlue,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GridSpaceBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val gridSize = 56.dp.toPx()
        val gridColor = MoonstoneBlue.copy(alpha = 0.04f)

        // Vertical lines
        var x = 0f
        while (x < w) {
            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(x, 0f),
                end = androidx.compose.ui.geometry.Offset(x, h),
                strokeWidth = 0.8.dp.toPx()
            )
            x += gridSize
        }

        // Horizontal lines
        var y = 0f
        while (y < h) {
            drawLine(
                color = gridColor,
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(w, y),
                strokeWidth = 0.8.dp.toPx()
            )
            y += gridSize
        }
    }
}

@Composable
fun HudReticle(modifier: Modifier = Modifier, color: Color = Color(0xFFA9C7D8)) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2
        val cy = h / 2

        // Orbit circles representing camera focus scopes
        drawCircle(
            color = color.copy(alpha = 0.12f),
            radius = w * 0.45f,
            style = Stroke(width = 0.8.dp.toPx())
        )
        drawCircle(
            color = color.copy(alpha = 0.08f),
            radius = w * 0.3f,
            style = Stroke(width = 0.8.dp.toPx())
        )

        // Crosshairs axes
        drawLine(
            color = color.copy(alpha = 0.25f),
            start = androidx.compose.ui.geometry.Offset(cx, cy - h * 0.45f),
            end = androidx.compose.ui.geometry.Offset(cx, cy + h * 0.45f),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = color.copy(alpha = 0.25f),
            start = androidx.compose.ui.geometry.Offset(cx - w * 0.45f, cy),
            end = androidx.compose.ui.geometry.Offset(cx + w * 0.45f, cy),
            strokeWidth = 1.dp.toPx()
        )

        // Central Magnifying Glass Lens
        drawCircle(
            color = color,
            radius = w * 0.2f,
            style = Stroke(width = 2.5.dp.toPx())
        )

        // Diamond Center Star
        val path = Path().apply {
            moveTo(cx, cy - 8.dp.toPx())
            lineTo(cx + 8.dp.toPx(), cy)
            lineTo(cx, cy + 8.dp.toPx())
            lineTo(cx - 8.dp.toPx(), cy)
            close()
        }
        drawPath(path = path, color = Color.White)
        drawPath(path = path, color = color, style = Stroke(width = 1.5.dp.toPx()))

        // Boundary tick brackets
        val pad = w * 0.14f
        val tick = 8.dp.toPx()

        // Top-Left
        drawLine(color.copy(alpha = 0.5f), androidx.compose.ui.geometry.Offset(pad, pad), androidx.compose.ui.geometry.Offset(pad + tick, pad), strokeWidth = 1.2.dp.toPx())
        drawLine(color.copy(alpha = 0.5f), androidx.compose.ui.geometry.Offset(pad, pad), androidx.compose.ui.geometry.Offset(pad, pad + tick), strokeWidth = 1.2.dp.toPx())

        // Top-Right
        drawLine(color.copy(alpha = 0.5f), androidx.compose.ui.geometry.Offset(w - pad, pad), androidx.compose.ui.geometry.Offset(w - pad - tick, pad), strokeWidth = 1.2.dp.toPx())
        drawLine(color.copy(alpha = 0.5f), androidx.compose.ui.geometry.Offset(w - pad, pad), androidx.compose.ui.geometry.Offset(w - pad, pad + tick), strokeWidth = 1.2.dp.toPx())

        // Bottom-Left
        drawLine(color.copy(alpha = 0.5f), androidx.compose.ui.geometry.Offset(pad, h - pad), androidx.compose.ui.geometry.Offset(pad + tick, h - pad), strokeWidth = 1.2.dp.toPx())
        drawLine(color.copy(alpha = 0.5f), androidx.compose.ui.geometry.Offset(pad, h - pad), androidx.compose.ui.geometry.Offset(pad, h - pad - tick), strokeWidth = 1.2.dp.toPx())

        // Bottom-Right
        drawLine(color.copy(alpha = 0.5f), androidx.compose.ui.geometry.Offset(w - pad, h - pad), androidx.compose.ui.geometry.Offset(w - pad - tick, h - pad), strokeWidth = 1.2.dp.toPx())
        drawLine(color.copy(alpha = 0.5f), androidx.compose.ui.geometry.Offset(w - pad, h - pad), androidx.compose.ui.geometry.Offset(w - pad, h - pad - tick), strokeWidth = 1.2.dp.toPx())
    }
}

@Composable
fun AppLayout(viewModel: TaskViewModel) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0 = Dashboard, 1 = Checklists, 2 = Blueprint specs

    val toastMsg by viewModel.toastMessage.collectAsStateWithLifecycle()

    // Auto-dismissing Toast notification
    LaunchedEffect(toastMsg) {
        toastMsg?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.dismissToast()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        containerColor = Color.Transparent,
        bottomBar = {
            // High-fidelity Floating Glass Bottom Navigation
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphismCard(RoundedCornerShape(30.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TabItem(
                            selected = selectedTab == 0,
                            icon = Icons.Default.Home,
                            label = "Dashboard",
                            onClick = { selectedTab = 0 },
                            testTag = "nav_dashboard"
                        )
                        TabItem(
                            selected = selectedTab == 1,
                            icon = Icons.Default.List,
                            label = "Console",
                            onClick = { selectedTab = 1 },
                            testTag = "nav_checklists"
                        )
                        TabItem(
                            selected = selectedTab == 2,
                            icon = Icons.Default.Info,
                            label = "Blueprints",
                            onClick = { selectedTab = 2 },
                            testTag = "nav_blueprints"
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Base static blueprint grid background
            GridSpaceBackground(modifier = Modifier.fillMaxSize())

            // Safe drawing inset for edge-to-edge camera notches
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                Crossfade(targetState = selectedTab, label = "tab_fade") { tab ->
                    when (tab) {
                        0 -> DashboardTab(viewModel = viewModel)
                        1 -> ControlRoomTab(viewModel = viewModel)
                        2 -> TechSpecsTab()
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TabItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    val alpha by animateFloatAsState(if (selected) 1f else 0.45f, label = "alpha")
    val scale by animateFloatAsState(if (selected) 1.05f else 0.95f, label = "scale")

    Box(
        modifier = Modifier
            .weight(1f)
            .scale(scale)
            .testTag(testTag)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MoonstoneIce else MoonstoneBlue,
                modifier = Modifier
                    .size(24.dp)
                    .alpha(alpha)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = if (selected) MoonstoneIce else MoonstoneBlue,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Black else FontWeight.Bold,
                letterSpacing = 0.5.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.alpha(alpha)
            )
        }
    }
}


// Tab 1: DASHBOARD
@Composable
fun DashboardTab(viewModel: TaskViewModel) {
    val totalTasks by viewModel.totalCount.collectAsStateWithLifecycle()
    val completedTasks by viewModel.completedCount.collectAsStateWithLifecycle()
    val progressPct by viewModel.progressPercent.collectAsStateWithLifecycle()
    val remainingTasks = totalTasks - completedTasks

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Floating Branded Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Pulse brand dot
                val pulseAnimation = rememberInfiniteTransition(label = "pulse")
                val alphaDot by pulseAnimation.animateFloat(
                    initialValue = 0.25f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "alpha"
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White.copy(alpha = alphaDot))
                )
                Text(
                    text = "PROOFBOARD OS",
                    color = MoonstoneSoft,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.5.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Box(
                modifier = Modifier
                    .background(FaintAccent, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "ACTIVE SESSION",
                    color = MoonstoneIce,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Panel Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .glassmorphismCard(RoundedCornerShape(26.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "PORTFOLIO V2",
                    color = MoonstoneBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Mission Control.",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1).sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Your daily execution engine. Keep track of what you turn into proof—one section, one milestone, one completed task at a time.",
                    color = MutedText,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Quick chip row
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuoteChip(text = "Build proof daily.")
                    QuoteChip(text = "Track honestly.")
                    QuoteChip(text = "Progress compounds.")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Operating Rules Floating Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x3B6E8798), RoundedCornerShape(16.dp))
                .border(1.dp, Color(0x30A9C7D8), RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Rule",
                tint = MoonstoneBlue,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = "OPERATING RULE:",
                    color = MoonstoneSoft,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "One finished task moves the system.",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Stats Matrix Grid
        Text(
            text = "SYSTEM METRICS",
            color = MoonstoneBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(modifier = Modifier.weight(1f), label = "Completed", value = "$completedTasks")
            StatCard(modifier = Modifier.weight(1f), label = "Remaining", value = "$remainingTasks")
            StatCard(modifier = Modifier.weight(1.1f), label = "Progress", value = "$progressPct%", hasGradient = true)
            StatCard(modifier = Modifier.weight(1f), label = "Total Tasks", value = "$totalTasks")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Large Progress bar with metallic ambient gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .glassmorphismCard(RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "REQUIREMENTS READINESS",
                        color = MoonstoneSoft,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "$progressPct% COMPLETE",
                        color = MoonstoneIce,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                // Beautiful Custom Segmented / Ambient glow progress
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x1F222B35))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(if (progressPct == 0) 0.01f else progressPct / 100f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF6E8798), Color(0xFFD7E2EA), Color(0xFFFFFFFF))
                                )
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Build Path Flow Rows
        Text(
            text = "PRACTICAL WORKFLOW",
            color = MoonstoneBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BuildNode(number = "01", title = "Blueprint")
            BuildNode(number = "02", title = "Static Layout")
            BuildNode(number = "03", title = "Assets")
            BuildNode(number = "04", title = "Motion")
            BuildNode(number = "05", title = "Integrate")
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Current Sprint Checklist highlight
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .glassmorphismCard(RoundedCornerShape(22.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CURRENT SPRINT",
                        color = MoonstoneBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "THIS WEEK",
                            color = ObsidianBlack,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                SprintItem(text = "Finalize master templates for all five sections.")
                SprintItem(text = "Create one clean V2 blueprint document before coding.")
                SprintItem(text = "Sketch initial, middle, and final state for each interaction.")
                SprintItem(text = "Collect required assets and mark missing assets honestly.")
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Five Focus Screens info map
        Text(
            text = "FIVE FOCUS SECTIONS",
            color = MoonstoneBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FocusMapList()

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun QuoteChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0x12FFFFFF), RoundedCornerShape(99.dp))
            .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(99.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            color = MoonstoneSoft,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    hasGradient: Boolean = false
) {
    Box(
        modifier = modifier
            .glassmorphismCard(RoundedCornerShape(18.dp))
            .padding(12.dp)
            .height(72.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                color = if (hasGradient) MoonstoneIce else Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label.uppercase(),
                color = MoonstoneSoft.copy(alpha = 0.7f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun BuildNode(number: String, title: String) {
    Box(
        modifier = Modifier
            .width(110.dp)
            .height(64.dp)
            .glassmorphismCard(RoundedCornerShape(14.dp))
            .padding(10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = number,
                color = MoonstoneBlue.copy(alpha = 0.45f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title.uppercase(),
                color = MoonstoneIce,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1
            )
        }
    }
}

@Composable
fun SprintItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(6.dp)
                .background(MoonstoneBlue, RoundedCornerShape(3.dp))
        )
        Text(
            text = text,
            color = MutedText,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun FocusMapList() {
    val sections = listOf(
        Triple("01 / Arrival", "Magnifying Lens", "Who am I right now? Glasses become a metaphor for learning to see clearly."),
        Triple("02 / Formation", "CD Timeline + Persona", "How was I shaped? Time, meaning, and identity move together."),
        Triple("03 / Direction", "3D Split scroll", "What am I pursuing? One current self splits into three active missions."),
        Triple("04 / Learning", "Learning Loop", "How do I grow? Project -> Problem -> Skill -> Artifact -> Feedback."),
        Triple("05 / Shape", "eBay Grid Assembly", "What am I turning into proof? Fragments become a structured board.")
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        sections.forEach { (index, title, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphismCard(RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = index,
                            color = MoonstoneBlue,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(FaintAccent, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "METAPHOR",
                                color = MoonstoneIce,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = desc,
                        color = MutedText,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}


// Tab 2: CONTROL ROOM (Task checklists checklists saved directly to local database Room)
@Composable
fun ControlRoomTab(viewModel: TaskViewModel) {
    val context = LocalContext.current
    val tasks by viewModel.filteredTasks.collectAsStateWithLifecycle()
    val expandedPhases by viewModel.expandedPhases.collectAsStateWithLifecycle()
    val currentFilter by viewModel.filter.collectAsStateWithLifecycle()

    val totalCount by viewModel.totalCount.collectAsStateWithLifecycle()
    val completedCount by viewModel.completedCount.collectAsStateWithLifecycle()
    val progressPct by viewModel.progressPercent.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    // Base native picker for importing .json checklist sheets
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { stream ->
                    val jsonString = stream.bufferedReader().readText()
                    viewModel.importFromJsonString(jsonString, onError = {
                        Toast.makeText(context, "Invalid backup configuration.", Toast.LENGTH_SHORT).show()
                    })
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load progress.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(text = "Reset All Progress?") },
            text = { Text(text = "Are you absolutely sure you want to reset all checklist records back to to-do? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetProgress()
                        showResetDialog = false
                    }
                ) {
                    Text(text = "RESET", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(text = "CANCEL")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Filter Controls Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterButton(text = "All", count = totalCount, active = currentFilter == "all", modifier = Modifier.weight(1f)) {
                viewModel.setFilter("all")
            }
            FilterButton(text = "To Do", count = totalCount - completedCount, active = currentFilter == "todo", modifier = Modifier.weight(1.1f)) {
                viewModel.setFilter("todo")
            }
            FilterButton(text = "Done", count = completedCount, active = currentFilter == "done", modifier = Modifier.weight(1f)) {
                viewModel.setFilter("done")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Utilities Toolbar Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            UtilityButton(
                icon = Icons.Default.KeyboardArrowDown,
                label = if (expandedPhases.size >= 7) "Collapse" else "Expand",
                modifier = Modifier.weight(1f)
            ) {
                viewModel.setAllExpanded(expandedPhases.size < 7)
            }
            UtilityButton(
                icon = Icons.Default.Share,
                label = "Export",
                modifier = Modifier.weight(1f)
            ) {
                // Export using system native share sheet sheet
                viewModel.getExportJsonString(object : TaskViewModel.ExportCallback {
                    override fun onExportReady(jsonString: String) {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, jsonString)
                            type = "application/json"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Export Checklist Progress")
                        context.startActivity(shareIntent)
                    }
                })
            }
            UtilityButton(
                icon = Icons.Default.ArrowBack,
                label = "Import",
                modifier = Modifier.weight(1f)
            ) {
                filePickerLauncher.launch("application/json")
            }
            UtilityButton(
                icon = Icons.Default.Refresh,
                label = "Reset",
                modifier = Modifier.weight(0.9f)
            ) {
                showResetDialog = true
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Dynamic State overview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .glassmorphismCard(RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = "CHECKLIST PROGRESS",
                    color = MoonstoneBlue,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$completedCount/$totalCount tasks complete · $progressPct%",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Big checklist container
        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tasks match current filtering criteria.",
                    color = MutedText.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Group by phase
            val groupedByPhase = remember(tasks) {
                tasks.groupBy { it.phaseIndex }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                groupedByPhase.forEach { (phaseIdx, phaseTasks) ->
                    val phaseName = phaseTasks.firstOrNull()?.phaseName ?: "Phase $phaseIdx"
                    val phaseNote = phaseTasks.firstOrNull()?.phaseNote ?: ""
                    val isExpanded = expandedPhases.contains(phaseIdx)

                    val doneCount = phaseTasks.count { it.isCompleted }
                    val totalPhaseTasks = phaseTasks.size

                    item(key = "phase_$phaseIdx") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .glassmorphismCard(RoundedCornerShape(22.dp))
                        ) {
                            Column {
                                // Accordion Header Row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.togglePhaseExpanded(phaseIdx) }
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color.White, RoundedCornerShape(20.dp))
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "PHASE $phaseIdx",
                                                color = ObsidianBlack,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                letterSpacing = 1.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = phaseName.substringAfter(": "),
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = phaseNote,
                                            color = MutedText.copy(alpha = 0.65f),
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "$doneCount/$totalPhaseTasks",
                                            color = MoonstoneIce,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Icon(
                                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Expand phase drawer",
                                            tint = MoonstoneBlue,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                // Tasks Body List
                                AnimatedVisibility(
                                    visible = isExpanded,
                                    enter = expandVertically() + fadeIn(),
                                    exit = shrinkVertically() + fadeOut()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        // Group tasks inside phase by section labels
                                        val sectionGroups = phaseTasks.groupBy { it.sectionLabel }
                                        sectionGroups.forEach { (sectionLabel, sectTasks) ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color(0x1F000000), RoundedCornerShape(14.dp))
                                                    .padding(8.dp)
                                            ) {
                                                Column {
                                                    // Section sub-indicator
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(bottom = 6.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = sectionLabel.uppercase(),
                                                            color = MoonstoneSoft,
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            letterSpacing = 1.2.sp
                                                        )
                                                        Text(
                                                            text = "${sectTasks.count { it.isCompleted }}/${sectTasks.size}",
                                                            color = MoonstoneBlue.copy(alpha = 0.7f),
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            fontFamily = FontFamily.Monospace
                                                        )
                                                    }

                                                    sectTasks.forEach { task ->
                                                        TaskCheckboxRow(
                                                            task = task,
                                                            onToggle = { completed ->
                                                                viewModel.toggleTask(task.id, completed, task.taskText)
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterButton(
    text: String,
    count: Int,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(99.dp))
            .background(if (active) Color.White else Color(0x18FFFFFF))
            .border(
                1.dp,
                if (active) Color.White else Color(0x26A9C7D8),
                RoundedCornerShape(99.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = text.uppercase(),
                color = if (active) ObsidianBlack else MoonstoneSoft,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
            Box(
                modifier = Modifier
                    .background(
                        if (active) Color(0xFF111821) else Color(0x3B6E8798),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$count",
                    color = if (active) Color.White else MoonstoneIce,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun UtilityButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(99.dp))
            .background(Color(0x0EFFFFFF))
            .border(1.dp, Color(0x14A9C7D8), RoundedCornerShape(99.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MoonstoneSoft,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = label.uppercase(),
                color = MoonstoneSoft,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun TaskCheckboxRow(
    task: TaskEntity,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!task.isCompleted) }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // High fidelity Custom Minimal checkbox to avoid generic Android gray box
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(if (task.isCompleted) Color.White else Color(0x0DFFFFFF))
                .border(
                    width = 1.2.dp,
                    color = if (task.isCompleted) Color.White else Color(0x40A9C7D8),
                    shape = RoundedCornerShape(7.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (task.isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed checkmark",
                    tint = ObsidianBlack,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = task.taskText,
            color = if (task.isCompleted) MutedText.copy(alpha = 0.45f) else Color.White,
            fontSize = 13.5.sp,
            lineHeight = 18.sp,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Colored priority tag indicator
        val tagBg = when (task.priority) {
            "critical" -> Color.White
            "important" -> Color(0xFFD7D7D1)
            else -> Color(0xFF6E8798).copy(alpha = 0.45f)
        }
        val tagText = when (task.priority) {
            "critical" -> ObsidianBlack
            "important" -> ObsidianBlack
            else -> MoonstoneIce
        }

        Box(
            modifier = Modifier
                .background(tagBg, RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = task.priority.uppercase(),
                color = tagText,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}


// Tab 3: BLUEPRINTS SPECS
@Composable
fun TechSpecsTab() {
    val blueprints = listOf(
        BlueprintSpecItem(
            name = "Hero / Arrival",
            q = "Who is Hemanth right now?",
            role = "Introduces current Hemanth before Still Becoming reveals what shaped him.",
            ref = "Magnifying Hero Text Animation in Framer.",
            metaphor = "Learning to see clearly through focus, observation, curiosity, and perspective.",
            content = "Micro-label, headline, subheadline, live badge, CTAs, moving words: AI, Software, Design, Writing, Discipline, Proof.",
            assets = "Current portrait, glasses PNG/WebP, lens mask, moving text layer, dark background.",
            anim = "At first: moving words behind glasses. Scroll: glasses become focus and move toward portrait. Final: current Hemanth is revealed.",
            success = "Visitor feels Hemanth is early but intentional, learning publicly."
        ),
        BlueprintSpecItem(
            name = "Still Becoming",
            q = "How was Hemanth shaped?",
            role = "Replaces generic identity section by showing formation through lived phases.",
            ref = "Circular CD Selection + Crazy Hover Mask Reveal.",
            metaphor = "Identity archaeology: current self contains older layers.",
            content = "Karate, Sainik School, NCC, Poetry, Oratory, Entrepreneurship, Building in Public.",
            assets = "CD template, current portrait, persona portraits, cadet visual, poetry visual, builder visual.",
            anim = "At first: title and CDs. Scroll: active CD changes, left meaning updates, right persona reveals.",
            success = "Visitor understands interests are not random; each phase left something."
        ),
        BlueprintSpecItem(
            name = "Current Mission",
            q = "What is Hemanth pursuing right now?",
            role = "Turns past formation into present direction.",
            ref = "3D Image Split Scroll Animation from Redo Media.",
            metaphor = "One current self splits into three active missions.",
            content = "Proof Hub, AI Workflow Lab, UX Field Notes.",
            assets = "Unified intro frame, three mission cards, AI visual, UX notes visual.",
            anim = "At first: one unified frame. Scroll: splits into three panels. Final: cards flip to reveal meaning.",
            success = "Visitor stating the three current directions clearly."
        ),
        BlueprintSpecItem(
            name = "How I Learn",
            q = "How does Hemanth turn curiosity into capability?",
            role = "Explains the process behind pursuing the missions.",
            ref = "Minimal scroll-revealed process diagram.",
            metaphor = "Curiosity becomes capability through repeatable loops.",
            content = "Project -> Problem -> Skill -> Artifact -> Feedback -> Iteration.",
            assets = "Step cards, template icons, connector SVG links.",
            anim = "Scroll: steps activate one by one and connector lines draw to make loop.",
            success = "Visitor understands Hemanth learns on physical loops & problem searches."
        ),
        BlueprintSpecItem(
            name = "What’s Taking Shape",
            q = "What am I actively turning into proof?",
            role = "Shows what learning process produces without pre-mature portfolio walls.",
            ref = "eBay Playbook sticky image grid scroll.",
            metaphor = "Fragments becoming structure.",
            content = "Proof Hub, AI Workflow Lab, UX Field Notes, First Real Product.",
            assets = "Proof cards, note fragments, screenshot logs, custom status labels.",
            anim = "Scroll: scattered cards move inward, scale normalizes, opacity increases into a clean board.",
            success = "Visitor thinks: he is early, honest, and organizing ideas into proof."
        )
    )

    var expandedBlueprintIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            text = "BLUEPRINT ARCHITECTURE",
            color = MoonstoneBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.5.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = " Cinematic Section specs for development",
            color = MutedText.copy(alpha = 0.65f),
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Expandable List List
        blueprints.forEachIndexed { idx, bp ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .glassmorphismCard(RoundedCornerShape(18.dp))
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedBlueprintIndex = if (expandedBlueprintIndex == idx) -1 else idx
                            }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "0${idx + 1} / ${bp.name.uppercase()}",
                                color = MoonstoneBlue,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = bp.q,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Icon(
                            imageVector = if (expandedBlueprintIndex == idx) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand specs drawer",
                            tint = MoonstoneBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = expandedBlueprintIndex == idx,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp)
                                .padding(bottom = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            HorizontalDivider(color = Color(0x1FFFFFFF))

                            SpecLine(label = "Story Role", value = bp.role)
                            SpecLine(label = "Reference Component", value = bp.ref)
                            SpecLine(label = "Visual Metaphor", value = bp.metaphor)
                            SpecLine(label = "Core Content", value = bp.content)
                            SpecLine(label = "Assets Required", value = bp.assets)
                            SpecLine(label = "Scroll Animation Flow", value = bp.anim)
                            SpecLine(label = "Mission Success Criteria", value = bp.success)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Animation matrix table summary
        Text(
            text = "ANIMATION LEVEL MATRIX",
            color = MoonstoneBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .glassmorphismCard(RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MatrixRow(sec = "Hero", anim = "Magnifying Lens Reveal", diff = "Hard")
                MatrixRow(sec = "Still Becoming", anim = "CD Timeline selection", diff = "Very Hard")
                MatrixRow(sec = "Current Mission", anim = "3D Image Split Flip", diff = "Medium-Hard")
                MatrixRow(sec = "How I Learn", anim = "Process Loop drawing", diff = "Medium")
                MatrixRow(sec = "What's Taking Shape", anim = "Grid Scattered Assemble", diff = "Medium-Hard")
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun SpecLine(label: String, value: String) {
    Column {
        Text(
            text = label.uppercase(),
            color = MoonstoneBlue.copy(alpha = 0.8f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = MutedText,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun MatrixRow(sec: String, anim: String, diff: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = sec,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = anim,
                color = MutedText.copy(alpha = 0.65f),
                fontSize = 11.sp
            )
        }
        val pillBg = when (diff) {
            "Very Hard" -> Color.White
            "Hard" -> Color(0xFFD7D7D1)
            else -> Color(0xFF6E8798).copy(alpha = 0.45f)
        }
        val pillText = when (diff) {
            "Very Hard" -> ObsidianBlack
            "Hard" -> ObsidianBlack
            else -> MoonstoneIce
        }
        Box(
            modifier = Modifier
                .background(pillBg, RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = diff.uppercase(),
                color = pillText,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

// Data class mapping specs
data class BlueprintSpecItem(
    val name: String,
    val q: String,
    val role: String,
    val ref: String,
    val metaphor: String,
    val content: String,
    val assets: String,
    val anim: String,
    val success: String
)
