package com.centr.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.centr.R
import com.centr.data.GameTheme
import com.centr.ui.screens.formatCoins
import com.centr.ui.theme.GoldYellow
import com.centr.ui.theme.WinHighlight
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.math.sin

@Composable
private fun SlotImage(resId: Int) {
    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxWidth(0.9f)
    )
}

@Composable
fun AztecInfoColumn(
    modifier: Modifier = Modifier,
    theme: GameTheme,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SlotImage(theme.slotLogo)

        SlotImage(R.drawable.aztec_royal_stub)
        SlotImage(R.drawable.aztec_grand_stub)
        SlotImage(R.drawable.aztec_major_stub)
        SlotImage(R.drawable.aztec_minor_stub)
        SlotImage(R.drawable.aztec_mini_stub)
    }
}

@Composable
fun LionTopRow(
    modifier: Modifier = Modifier,
    theme: GameTheme
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        SlotTopImage(R.drawable.lion_grand_stub, 0.175f)
        SlotTopImage(R.drawable.lion_major_stub, 0.175f)
        SlotTopImage(theme.slotLogo, 0.3f)
        SlotTopImage(R.drawable.lion_minor_stub, 0.175f)
        SlotTopImage(R.drawable.lion_mini_stub, 0.175f)
    }
}

@Composable
private fun RowScope.SlotTopImage(
    resId: Int,
    weight: Float
) {
    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier.weight(weight)
    )
}

@Composable
fun RoyalLeftColumn(
    modifier: Modifier = Modifier,
    coins: Long,
    level: Int,
    levelProgress: Float,
    onCoinsPositioned: (Float, Float) -> Unit = { _, _ -> },
    onStarPositioned: (Float, Float) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.85f))
                .border(1.dp, GoldYellow.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .onGloballyPositioned {
                    val pos = it.localToRoot(Offset.Zero)
                    onCoinsPositioned(pos.x + it.size.width / 2f, pos.y + it.size.height / 2f)
                }
        ) {
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painter = painterResource(id = R.drawable.lion_element_8),
                contentDescription = null, modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = formatCoins(coins), color = GoldYellow,
                fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = CasinoFont
            )
            Spacer(modifier = Modifier.width(10.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .onGloballyPositioned {
                            val pos = it.localToRoot(Offset.Zero)
                            onStarPositioned(
                                pos.x + it.size.width / 2f,
                                pos.y + it.size.height / 2f
                            )
                        }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Lv.$level",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = CasinoFont
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                LinearProgressIndicator(
                    progress = { (levelProgress / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxSize(),
                    color = GoldYellow, trackColor = Color.Transparent
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${levelProgress.toInt()}%",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CasinoFont
            )
        }
    }
}

@Composable
fun RoyalTopBanner(
    modifier: Modifier = Modifier,
    theme: GameTheme
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = (-80).dp),
        verticalAlignment = Alignment.Bottom
    ) {

        // Левая колонка
        SlotSideColumn(
            topRes = R.drawable.royal_grand_stub,
            bottomRes = R.drawable.royal_minor_stub,
            modifier = Modifier.weight(0.3f)
        )

        // Центр (логотип)
        Image(
            painter = painterResource(id = theme.slotLogo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(0.4f)
                .scale(1.15f)
        )

        // Правая колонка
        SlotSideColumn(
            topRes = R.drawable.royal_major_stub,
            bottomRes = R.drawable.royal_mini_stub,
            modifier = Modifier.weight(0.3f)
        )
    }
}

@Composable
private fun SlotSideColumn(
    topRes: Int,
    bottomRes: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = topRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth()
        )
        Image(
            painter = painterResource(id = bottomRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SlotTopBannerPirate(
    modifier: Modifier = Modifier,
    theme: GameTheme
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = (-84).dp),
        verticalAlignment = Alignment.Bottom
    ) {

        // Левая колонка
        TreasureSideColumn(
            topRes = R.drawable.pirote_major_stub,
            bottomRes = R.drawable.pirote_mini_stub,
            modifier = Modifier.weight(0.3f)
        )

        // Центр (логотип с увеличением)
        Image(
            painter = painterResource(id = theme.slotLogo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(0.4f)
                .scale(1.15f)
        )

        // Правая колонка
        SlotSideColumn(
            topRes = R.drawable.pirote_grand_stub,
            bottomRes = R.drawable.pirote_minor_stub,
            modifier = Modifier.weight(0.3f)
        )
    }
}

@Composable
private fun TreasureSideColumn(
    topRes: Int,
    bottomRes: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = topRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth()
        )
        Image(
            painter = painterResource(id = bottomRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TreasureTopRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .offset(y = (-30).dp),
        verticalAlignment = Alignment.Bottom
    ) {
        TreasureItem(R.drawable.treasure_grand_stub)
        TreasureItem(R.drawable.treasure_major_stub)
        TreasureItem(R.drawable.treasure_minor_stub)
        TreasureItem(R.drawable.treasure_mini_stub)
    }
}

@Composable
private fun RowScope.TreasureItem(resId: Int) {
    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun SevenTopRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .offset(y = (-30).dp),
        verticalAlignment = Alignment.Bottom
    ) {
        SevenItem(R.drawable.seven_grand_stub)
        SevenItem(R.drawable.seven_major_stub)
        SevenItem(R.drawable.seven_minor_stub)
        SevenItem(R.drawable.seven_mini_stub)
    }
}

@Composable
private fun RowScope.SevenItem(resId: Int) {
    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun ReelColumn(
    col: Int,
    theme: GameTheme,
    reelStrip: List<Int>,
    displaySymbols: List<Int>,
    spinTrigger: Int,
    spinning: Boolean,
    paused: Boolean,
    speedLevel: Int,
    winPositions: Set<Pair<Int, Int>>,
    glowAlpha: Float,
    cellPositions: MutableMap<Pair<Int, Int>, Pair<Float, Float>>,
    cellSizes: MutableMap<Pair<Int, Int>, IntSize>,
    modifier: Modifier = Modifier
) {
    val rows = theme.rows
    val scrollAnim = remember { Animatable(0f) }
    val currentPaused by rememberUpdatedState(paused)

    LaunchedEffect(spinTrigger) {
        if (spinTrigger == 0 || reelStrip.isEmpty()) return@LaunchedEffect

        scrollAnim.snapTo(1f)

        val baseDuration = when (speedLevel) {
            1 -> 2000
            2 -> 1200
            3 -> 700
            else -> 2000
        }
        val totalDuration = baseDuration + col * 200

        // Stagger delay — suspend while paused
        var staggerLeft = col * 300L
        while (staggerLeft > 0) {
            if (currentPaused) {
                snapshotFlow { currentPaused }.first { !it }
                continue
            }
            val step = staggerLeft.coerceAtMost(50L)
            delay(step)
            staggerLeft -= step
        }

        // Main scroll — frame-based, suspend while paused
        var elapsed = 0L
        var lastFrame = withFrameMillis { it }
        while (elapsed < totalDuration) {
            if (currentPaused) {
                snapshotFlow { currentPaused }.first { !it }
                lastFrame = withFrameMillis { it }
                continue
            }
            val frameTime = withFrameMillis { it }
            val dt = frameTime - lastFrame
            elapsed += dt
            val progress = 1f - (elapsed.toFloat() / totalDuration).coerceAtMost(1f)
            scrollAnim.snapTo(progress)
            lastFrame = frameTime
        }
        scrollAnim.snapTo(0f)

        // Bounce at landing
        scrollAnim.animateTo(
            targetValue = 0f,
            animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f)
        )
    }

    val isAnimating = spinTrigger > 0 && reelStrip.size > rows
    val stripSize = if (isAnimating) reelStrip.size else rows
    val scrollableCount = (stripSize - rows).coerceAtLeast(0)

    // Continuous float position in the strip
    val floatPos = scrollAnim.value * scrollableCount
    val startIndex = floatPos.toInt().coerceIn(0, scrollableCount)
    val fraction = floatPos - startIndex // 0..1 sub-cell fractional offset

    val isStopped = !spinning && scrollAnim.value <= 0.01f

    // Outer box clips to exactly the visible area — only `rows` cells visible
    var columnHeightPx by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .clipToBounds()
            .onGloballyPositioned { columnHeightPx = it.size.height.toFloat() }
    ) {
        if (columnHeightPx <= 0f) return@Box
        val cellHeightPx = columnHeightPx / rows

        // Inner column: rows+1 items, shifted up by fraction * cellHeight
        // This gives pixel-smooth scrolling; the extra item scrolls in from bottom
        val offsetPx = (fraction * cellHeightPx).toInt()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(androidx.compose.ui.platform.LocalDensity.current) {
                    (cellHeightPx * (rows + 1)).toInt().toDp()
                })
                .offset { IntOffset(0, -offsetPx) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val itemCount = (rows + 1).coerceAtMost(stripSize - startIndex)
            for (i in 0 until itemCount) {
                val stripIndex = startIndex + i
                val symbolIndex = if (isAnimating && stripIndex < reelStrip.size) {
                    reelStrip[stripIndex]
                } else {
                    displaySymbols.getOrElse(i) { 0 }
                }

                // Map back to actual row for win detection (only valid when stopped)
                val row = i
                val isWinCell = isStopped && row < rows && Pair(col, row) in winPositions

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(with(androidx.compose.ui.platform.LocalDensity.current) {
                            cellHeightPx.toInt().toDp()
                        })
                        .padding(2.dp)
                        .then(
                            if (isWinCell) Modifier.border(
                                2.dp, WinHighlight.copy(alpha = glowAlpha), RoundedCornerShape(4.dp)
                            ) else Modifier
                        )
                        .onGloballyPositioned { coordinates ->
                            if (isStopped && i < rows) {
                                val pos = coordinates.positionInParent()
                                cellPositions[Pair(col, i)] = Pair(pos.x, pos.y)
                                cellSizes[Pair(col, i)] = coordinates.size
                            }
                        }
                ) {
                    val resId =
                        theme.elementResIds.getOrElse(symbolIndex) { theme.elementResIds[0] }
                    val isCurrentlySpinning = spinning && scrollAnim.value > 0.05f

                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize(0.85f)
                            .clip(RoundedCornerShape(4.dp))
                            .graphicsLayer {
                                if (isCurrentlySpinning) {
                                    scaleY = 1.1f
                                    alpha = 0.7f
                                }
                            }
                    )
                }
            }
        }
    }
}

// ─── Top Bar ────────────────────────────────────────────────────────────────

@Composable
fun GameTopBar(
    coins: Long,
    level: Int,
    theme: GameTheme,
    levelProgress: Float,
    musicEnabled: Boolean = true,
    onBack: () -> Unit,
    onMusic: () -> Unit,
    onInfo: () -> Unit,
    onPause: () -> Unit,
    onCoinsPositioned: (Float, Float) -> Unit = { _, _ -> },
    onStarPositioned: (Float, Float) -> Unit = { _, _ -> }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SquareButton(
            btnRes = R.drawable.home_button,
            btnMaxWidth = 0.06f, btnClickable = onBack
        )

        if (theme.displayName == "Royal" || theme.displayName == "Pirate") {
            Spacer(modifier = Modifier.weight(1f))
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.85f))
                    .border(1.dp, GoldYellow.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .onGloballyPositioned {
                        val pos = it.localToRoot(Offset.Zero)
                        onCoinsPositioned(pos.x + it.size.width / 2f, pos.y + it.size.height / 2f)
                    }
            ) {
                Spacer(modifier = Modifier.width(6.dp))
                Image(
                    painter = painterResource(id = R.drawable.lion_element_8),
                    contentDescription = null, modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatCoins(coins), color = GoldYellow,
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = CasinoFont
                )
                Spacer(modifier = Modifier.width(10.dp))
            }

            Spacer(modifier = Modifier.width(60.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .onGloballyPositioned {
                            val pos = it.localToRoot(Offset.Zero)
                            onStarPositioned(
                                pos.x + it.size.width / 2f,
                                pos.y + it.size.height / 2f
                            )
                        }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Lv.$level",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = CasinoFont
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                ) {
                    LinearProgressIndicator(
                        progress = { (levelProgress / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxSize(),
                        color = GoldYellow, trackColor = Color.Transparent
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${levelProgress.toInt()}%",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CasinoFont
                )
            }
        }

        Row {
            Box(modifier = Modifier.graphicsLayer { alpha = if (musicEnabled) 1f else 0.4f }) {
                SquareButton(
                    btnRes = R.drawable.sound_button,
                    btnMaxWidth = if (theme.displayName == "Royal" || theme.displayName == "Pirate") 0.048f else 0.09f,
                    cooldownMillis = 0L,
                    btnClickable = onMusic
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            SquareButton(
                btnRes = R.drawable.info_button,
                btnMaxWidth = if (theme.displayName == "Royal" || theme.displayName == "Pirate") 0.048f else 0.09f,
                btnClickable = onInfo
            )
            Spacer(modifier = Modifier.width(8.dp))
            SquareButton(
                btnRes = R.drawable.pause_button,
                btnMaxWidth = if (theme.displayName == "Royal" || theme.displayName == "Pirate") 0.054f else 0.11f,
                btnClickable = onPause
            )
        }
    }
}

// ─── Bottom Bar ─────────────────────────────────────────────────────────────

@Composable
fun GameBottomBar(
    bet: Long, maxBet: Long, spinning: Boolean, autoSpin: Boolean, lastWin: Long,
    theme: GameTheme,
    onIncreaseBet: () -> Unit, onDecreaseBet: () -> Unit,
    onBetPositioned: (Float, Float) -> Unit = { _, _ -> }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 44.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(0.1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "LAST WIN",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp, fontFamily = CasinoFont
            )
            Text(
                text = if (lastWin > 0) formatCoins(lastWin) else "—",
                color = if (lastWin > 0) GoldYellow else Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CasinoFont
            )
        }

        Box(
            modifier = Modifier.weight(0.6f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = theme.winText),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(0.98f)
            )
        }

        Row(
            modifier = Modifier.weight(0.3f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MenuButton(
                text = "-", fontSize = 24.sp,
                buttonRes = theme.buttonRes,
                cooldown = 0L,
                enabled = !spinning && !autoSpin && bet > 1_000L,
                modifier = Modifier
                    .width(50.dp)
                    .height(40.dp),
                onClick = onDecreaseBet
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.onGloballyPositioned {
                    val pos = it.localToRoot(Offset.Zero)
                    onBetPositioned(pos.x + it.size.width / 2f, pos.y + it.size.height / 2f)
                }
            ) {
                Text(
                    text = "PLAY",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontFamily = CasinoFont,
                    fontWeight = FontWeight.Bold,
                )
                AnimatedBetText(bet = bet)
            }
            Spacer(modifier = Modifier.width(8.dp))
            MenuButton(
                text = "+", fontSize = 24.sp,
                buttonRes = theme.buttonRes,
                cooldown = 0L,
                enabled = !spinning && !autoSpin && bet < maxBet,
                modifier = Modifier
                    .width(50.dp)
                    .height(40.dp),
                onClick = onIncreaseBet
            )
        }
    }
}

@Composable
fun GameRightBar(
    spinning: Boolean,
    autoSpin: Boolean,
    speedLevel: Int, theme: GameTheme,
    onSpin: () -> Unit,
    onAutoSpin: () -> Unit,
    onSpeed: () -> Unit,
    onSpinPositioned: (Float, Float) -> Unit = { _, _ -> }
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SquareButton(
                btnRes = R.drawable.auto_win_button,
                btnMaxWidth = 0.3f, cooldownMillis = 0L,
                btnEnabled = true, btnClickable = onAutoSpin
            )

            Text(
                text = if (autoSpin) "AUTO ON" else "AUTO OFF",
                color = if (autoSpin) GoldYellow else Color.White,
                fontSize = 11.sp,
                fontFamily = CasinoFont,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier.onGloballyPositioned {
                val pos = it.localToRoot(Offset.Zero)
                onSpinPositioned(pos.x + it.size.width / 2f, pos.y + it.size.height / 2f)
            }
        ) {
            SquareButton(
                btnRes = theme.spinButtonRes,
                btnMaxWidth = 0.6f, cooldownMillis = 0L,
                btnEnabled = !spinning && !autoSpin, btnClickable = onSpin
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SquareButton(
                btnRes = R.drawable.speed_button,
                btnMaxWidth = 0.3f, cooldownMillis = 300L, btnClickable = onSpeed
            )
            Text(
                text = "${speedLevel}x",
                color = Color.White,
                fontSize = 11.sp,
                fontFamily = CasinoFont,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ─── Animated Bet Counter ────────────────────────────────────────────────────

@Composable
fun AnimatedBetText(bet: Long) {
    var displayBet by remember { mutableStateOf(bet) }

    LaunchedEffect(bet) {
        val start = displayBet
        val diff = bet - start
        if (diff == 0L) return@LaunchedEffect
        val steps = 8
        for (i in 1..steps) {
            displayBet = start + diff * i / steps
            delay(25L)
        }
        displayBet = bet
    }

    Text(
        text = "%,d".format(displayBet),
        color = GoldYellow,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = CasinoFont
    )
}

// ─── Flying Particle Animation ──────────────────────────────────────────────

@Composable
fun SlotParticleFly(
    drawableRes: Int,
    startXPx: Float,
    startYPx: Float,
    targetXPx: Float,
    targetYPx: Float,
    delayMs: Int = 0,
    density: androidx.compose.ui.unit.Density,
    onFinished: () -> Unit
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        if (delayMs > 0) delay(delayMs.toLong())
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
        )
        onFinished()
    }

    val t = progress.value
    if (t <= 0f) return

    val currentX = startXPx + (targetXPx - startXPx) * t
    val currentY = startYPx + (targetYPx - startYPx) * t - 80f * sin(t * Math.PI.toFloat())

    val offsetX = with(density) { currentX.toInt().toDp() }
    val offsetY = with(density) { currentY.toInt().toDp() }

    Image(
        painter = painterResource(id = drawableRes),
        contentDescription = null,
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(18.dp)
            .graphicsLayer {
                alpha = 1f - t * 0.3f
                scaleX = 1f - t * 0.4f
                scaleY = 1f - t * 0.4f
            }
    )
}