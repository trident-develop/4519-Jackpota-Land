package com.centr.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.centr.R
import com.centr.data.GameTheme
import com.centr.data.WinLine
import com.centr.sound.SoundManager
import com.centr.ui.components.AztecInfoColumn
import com.centr.ui.components.CasinoFont
import com.centr.ui.components.GameBottomBar
import com.centr.ui.components.GameRightBar
import com.centr.ui.components.GameTopBar
import com.centr.ui.components.LionTopRow
import com.centr.ui.components.MenuButton
import com.centr.ui.components.ReelColumn
import com.centr.ui.components.RoyalLeftColumn
import com.centr.ui.components.RoyalTopBanner
import com.centr.ui.components.SevenTopRow
import com.centr.ui.components.SlotParticleFly
import com.centr.ui.components.SlotTopBannerPirate
import com.centr.ui.components.TreasureTopRow
import com.centr.ui.theme.GoldYellow
import com.centr.ui.theme.WinHighlight
import com.centr.viewmodel.SlotViewModel
import com.centr.viewmodel.model.SlotUiState

private data class SlotFlyingCoin(
    val id: Int,
    val startX: Float,
    val startY: Float,
    val delayMs: Int = 0
)

@Composable
fun SlotGameScreen(
    theme: GameTheme,
    soundManager: SoundManager,
    onBack: () -> Unit,
    slotViewModel: SlotViewModel = viewModel()
) {
    val state by slotViewModel.uiState.collectAsState()
    var showInfo by remember { mutableStateOf(false) }
    var musicEnabled by remember { mutableStateOf(soundManager.isMusicPlaying) }
    var isExitingScreen by remember { mutableStateOf(false) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_PAUSE && !isExitingScreen && !state.showLevelUp && !state.showWinScreen) {
                slotViewModel.pause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    BackHandler(enabled = true) {
        isExitingScreen = true
        onBack()
    }

    LaunchedEffect(theme) {
        slotViewModel.initialize(theme)
    }

    LaunchedEffect(state.spinTrigger) {
        if (state.spinTrigger > 0) {
            soundManager.playSpinSound()
        }
    }

    LaunchedEffect(state.totalWin) {
        if (state.totalWin > 0) {
            soundManager.playWinSound()
        }
    }

    LaunchedEffect(state.showLevelUp) {
        if (state.showLevelUp) {
            soundManager.playLevelUpSound()
        }
    }

    SlotGameScreenContent(
        theme = theme,
        state = state,
        showInfo = showInfo,
        musicEnabled = musicEnabled,
        onBack = {
            isExitingScreen = true
            onBack()
        },
        onMusic = {
            soundManager.toggleMusic()
            musicEnabled = soundManager.isMusicPlaying
        },
        onInfo = {
            slotViewModel.pause()
            showInfo = true
        },
        onDismissInfo = {
            showInfo = false
            slotViewModel.resume()
        },
        onSpin = { slotViewModel.spin() },
        onIncreaseBet = { slotViewModel.increaseBet() },
        onDecreaseBet = { slotViewModel.decreaseBet() },
        onAutoSpin = { slotViewModel.toggleAutoSpin() },
        onSpeed = { slotViewModel.cycleSpeed() },
        onPause = { slotViewModel.pause() },
        onResume = { slotViewModel.resume() },
        onDismissWin = { slotViewModel.dismissWinScreen() },
        onDismissLevelUp = { slotViewModel.dismissLevelUp() }
    )
}

@Composable
fun SlotGameScreenContent(
    theme: GameTheme,
    state: SlotUiState,
    showInfo: Boolean,
    musicEnabled: Boolean = true,
    onBack: () -> Unit,
    onMusic: () -> Unit,
    onInfo: () -> Unit,
    onDismissInfo: () -> Unit,
    onSpin: () -> Unit,
    onIncreaseBet: () -> Unit,
    onDecreaseBet: () -> Unit,
    onAutoSpin: () -> Unit,
    onSpeed: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onDismissWin: () -> Unit,
    onDismissLevelUp: () -> Unit
) {
    var flyingCoins by remember { mutableStateOf(listOf<SlotFlyingCoin>()) }
    var coinIdCounter by remember { mutableStateOf(0) }
    var betAreaX by remember { mutableStateOf(0f) }
    var betAreaY by remember { mutableStateOf(0f) }
    var coinsTargetX by remember { mutableStateOf(0f) }
    var coinsTargetY by remember { mutableStateOf(0f) }

    var flyingStars by remember { mutableStateOf(listOf<SlotFlyingCoin>()) }
    var starIdCounter by remember { mutableStateOf(0) }
    var spinBtnX by remember { mutableStateOf(0f) }
    var spinBtnY by remember { mutableStateOf(0f) }
    var starTargetX by remember { mutableStateOf(0f) }
    var starTargetY by remember { mutableStateOf(0f) }

    fun spawnWinCoins() {
        if (betAreaX <= 0f) return
        val count = 15
        val newCoins = (0 until count).map { i ->
            SlotFlyingCoin(
                id = coinIdCounter + i,
                startX = betAreaX + (-40..40).random(),
                startY = betAreaY + (-20..20).random(),
                delayMs = i * 40
            )
        }
        coinIdCounter += count
        flyingCoins = flyingCoins + newCoins
    }

    fun spawnSpinStars() {
        if (spinBtnX <= 0f) return
        val count = 10
        val newStars = (0 until count).map { i ->
            SlotFlyingCoin(
                id = starIdCounter + i,
                startX = spinBtnX + (-25..25).random(),
                startY = spinBtnY + (-15..15).random(),
                delayMs = i * 30
            )
        }
        starIdCounter += count
        flyingStars = flyingStars + newStars
    }

    LaunchedEffect(state.spinTrigger) {
        if (state.spinTrigger > 0) {
            spawnSpinStars()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = theme.bgRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f), // верх (тёмный)
                            Color.Transparent,             // середина
                            Color.Transparent,             // середина
                            Color.Black.copy(alpha = 0.6f) // низ (тёмный)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            GameTopBar(
                coins = state.coins,
                level = state.level,
                levelProgress = state.levelProgress,
                musicEnabled = musicEnabled,
                onBack = onBack,
                theme = theme,
                onMusic = onMusic,
                onInfo = onInfo,
                onPause = onPause,
                onCoinsPositioned = { x, y ->
                    coinsTargetX = x
                    coinsTargetY = y
                },
                onStarPositioned = { x, y ->
                    starTargetX = x
                    starTargetY = y
                }
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(
                            when (theme.displayName) {
                                "Aztec" -> 0.2f
                                "Royal" -> 0.25f
                                "Pirate" -> 0.25f
                                "Seven" -> 0.18f
                                else -> 0.1f
                            }
                        )
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (theme.displayName == "Aztec") {
                        AztecInfoColumn(
                            modifier = Modifier.padding(start = 30.dp),
                            theme = theme
                        )
                    }

                    if (theme.displayName == "Royal" || theme.displayName == "Pirate") {
                        RoyalLeftColumn(
                            modifier = Modifier.padding(start = 20.dp),
                            coins = state.coins,
                            level = state.level,
                            levelProgress = state.levelProgress,
                            onCoinsPositioned = { x, y ->
                                coinsTargetX = x
                                coinsTargetY = y
                            },
                            onStarPositioned = { x, y ->
                                starTargetX = x
                                starTargetY = y
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(if (theme.displayName == "Aztec") 0.6f else 0.8f)
                        .fillMaxWidth()
                        .padding(
                            top = when (theme.displayName) {
                                "Lion" -> 16.dp
                                "Royal" -> 50.dp
                                "Pirate" -> 50.dp
                                "Treasure" -> 40.dp
                                "Seven" -> 10.dp
                                else -> 0.dp
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    SlotMachine(
                        theme = theme,
                        reelStrips = state.reelStrips,
                        displayGrid = state.displayGrid,
                        spinTrigger = state.spinTrigger,
                        spinning = state.spinning,
                        paused = state.paused,
                        speedLevel = state.speedLevel,
                        winLines = state.winLines
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    GameRightBar(
                        spinning = state.spinning,
                        autoSpin = state.autoSpin,
                        speedLevel = state.speedLevel,
                        theme = theme,
                        onSpin = onSpin,
                        onAutoSpin = onAutoSpin,
                        onSpeed = onSpeed,
                        onSpinPositioned = { x, y ->
                            spinBtnX = x
                            spinBtnY = y
                        }
                    )
                }
            }

            GameBottomBar(
                bet = state.bet,
                maxBet = state.maxBet,
                spinning = state.spinning,
                autoSpin = state.autoSpin,
                lastWin = state.lastWin,
                theme = theme,
                onIncreaseBet = onIncreaseBet,
                onDecreaseBet = onDecreaseBet,
                onBetPositioned = { x, y ->
                    betAreaX = x
                    betAreaY = y
                }
            )
        }

        val density = LocalDensity.current

        for (coin in flyingCoins) {
            key(coin.id) {
                SlotParticleFly(
                    drawableRes = R.drawable.coin,
                    startXPx = coin.startX,
                    startYPx = coin.startY,
                    targetXPx = coinsTargetX,
                    targetYPx = coinsTargetY,
                    delayMs = coin.delayMs,
                    density = density,
                    onFinished = {
                        flyingCoins = flyingCoins.filter { it.id != coin.id }
                    }
                )
            }
        }

        for (star in flyingStars) {
            key(star.id) {
                SlotParticleFly(
                    drawableRes = R.drawable.star,
                    startXPx = star.startX,
                    startYPx = star.startY,
                    targetXPx = starTargetX,
                    targetYPx = starTargetY,
                    delayMs = star.delayMs,
                    density = density,
                    onFinished = {
                        flyingStars = flyingStars.filter { it.id != star.id }
                    }
                )
            }
        }
    }

    if (state.showWinScreen && state.totalWin > 0 && !state.spinning) {
        WinDialog(
            theme = theme,
            totalWin = state.totalWin,
            bet = state.bet,
            onDismiss = {
                spawnWinCoins()
                onDismissWin()
            }
        )
    }

    if (state.showLevelUp) {
        LevelUpDialog(
            newLevel = state.level,
            totalCoins = state.coins,
            onDismiss = onDismissLevelUp
        )
    }

    if (showInfo) {
        InfoDialog(
            theme = theme,
            onDismiss = onDismissInfo
        )
    }

    if (state.paused && !showInfo) {
        PauseOverlay(theme = theme, onResume = onResume)
    }
}

@Composable
private fun PauseOverlay(theme: GameTheme, onResume: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .pointerInput(Unit) { detectTapGestures { /* consume */ } },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "PAUSED",
                color = GoldYellow,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CasinoFont
            )
            Spacer(modifier = Modifier.height(24.dp))
            MenuButton(
                text = "CONTINUE",
                fontSize = 18.sp,
                buttonRes = theme.buttonRes,
                cooldown = 0L,
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp),
                onClick = onResume
            )
        }
    }
}

// ─── Slot Machine with real vertical reel scrolling ─────────────────────────

@Composable
private fun SlotMachine(
    theme: GameTheme,
    reelStrips: List<List<Int>>,
    displayGrid: List<List<Int>>,
    spinTrigger: Int,
    spinning: Boolean,
    paused: Boolean,
    speedLevel: Int,
    winLines: List<WinLine>
) {
    val winPositions = remember(winLines) {
        winLines.flatMap { it.positions }.toSet()
    }
    val cellPositions = remember { mutableMapOf<Pair<Int, Int>, Pair<Float, Float>>() }
    val cellSizes = remember { mutableMapOf<Pair<Int, Int>, IntSize>() }

    val infiniteTransition = rememberInfiniteTransition(label = "winGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (theme.displayName == "Treasure") {
            Image(
                painter = painterResource(id = theme.slotLogo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize(0.5f)
                    .offset(y = (-80).dp)
                    .scale(1f)
            )
        }

        Image(
            painter = painterResource(id = theme.slotRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .scale(1f)
        )

        if (theme.displayName == "Treasure") {
            TreasureTopRow(modifier = Modifier.align(Alignment.TopCenter))
        }

        if (theme.displayName == "Pirate") {
            SlotTopBannerPirate(
                modifier = Modifier.align(Alignment.TopCenter),
                theme = theme
            )
        }

        if (theme.displayName == "Lion") {
            LionTopRow(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-20).dp),
                theme = theme
            )
        }

        if (theme.displayName == "Royal") {
            RoyalTopBanner(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                theme = theme
            )
        }

        if (theme.displayName == "Seven") {
            SevenTopRow(modifier = Modifier.align(Alignment.TopCenter))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = when (theme.displayName) {
                        "Lion" -> 24.dp
                        "Royal" -> 18.dp
                        "Pirate" -> 30.dp
                        "Treasure" -> 10.dp
                        else -> 24.dp
                    }
                )
                .padding(
                    bottom = 16.dp, top = when (theme.displayName) {
                        "Pirate" -> 40.dp
                        "Royal" -> 40.dp
                        "Seven" -> 30.dp
                        else -> 24.dp
                    }
                )
                .drawBehind {
                    if (winLines.isNotEmpty() && !spinning) {
                        for (winLine in winLines) {
                            val positions = winLine.positions
                            for (i in 0 until positions.size - 1) {
                                val start = cellPositions[positions[i]]
                                val end = cellPositions[positions[i + 1]]
                                val startSize = cellSizes[positions[i]]
                                val endSize = cellSizes[positions[i + 1]]
                                if (start != null && end != null && startSize != null && endSize != null) {
                                    drawLine(
                                        color = WinHighlight.copy(alpha = glowAlpha),
                                        start = Offset(
                                            start.first + startSize.width / 2f,
                                            start.second + startSize.height / 2f
                                        ),
                                        end = Offset(
                                            end.first + endSize.width / 2f,
                                            end.second + endSize.height / 2f
                                        ),
                                        strokeWidth = 6f,
                                        cap = StrokeCap.Round
                                    )
                                }
                            }
                        }
                    }
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (col in 0 until theme.columns) {
                    ReelColumn(
                        col = col,
                        theme = theme,
                        reelStrip = reelStrips.getOrNull(col) ?: emptyList(),
                        displaySymbols = displayGrid.getOrNull(col) ?: emptyList(),
                        spinTrigger = spinTrigger,
                        spinning = spinning,
                        paused = paused,
                        speedLevel = speedLevel,
                        winPositions = winPositions,
                        glowAlpha = glowAlpha,
                        cellPositions = cellPositions,
                        cellSizes = cellSizes,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}