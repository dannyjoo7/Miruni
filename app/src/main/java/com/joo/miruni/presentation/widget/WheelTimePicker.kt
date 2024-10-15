package com.joo.miruni.presentation.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.material.timepicker.TimeFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joo.miruni.theme.lightPallet
import com.ozcanalasalvar.datepicker.ui.theme.PickerTheme
import com.ozcanalasalvar.datepicker.ui.theme.colorLightOnBackground
import com.ozcanalasalvar.datepicker.ui.theme.colorLightPrimary
import com.ozcanalasalvar.datepicker.ui.theme.colorLightTextPrimary
import kotlinx.coroutines.launch

@Composable
fun WheelTimePicker(
    offset: Int = 4,
    selectorEffectEnabled: Boolean = true,
    timeFormat: Int = TimeFormat.CLOCK_24H,
    startTime: Time,
    textSize: Int = 16,
    onTimeChanged: (Int, Int, String?) -> Unit = { _, _, _ -> },
    darkModeEnabled: Boolean = true,
) {

    var selectedTime by remember { mutableStateOf(startTime) }

    val formats = listOf("오전", "오후", )

    val hours = mutableListOf<Int>().apply {
        for (hour in 0..if (timeFormat == TimeFormat.CLOCK_24H) 23 else 11) {
            add(hour)
        }
        if (timeFormat == TimeFormat.CLOCK_12H) {
            add(12)
        }
    }

    val minutes = mutableListOf<Int>().apply {
        for (minute in 0..59 step 5) {
            add(minute)
        }
    }
    val fontSize = maxOf(13, minOf(24, textSize))

    LaunchedEffect(selectedTime) {
        onTimeChanged(selectedTime.hour, selectedTime.minute, selectedTime.format)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(if (darkModeEnabled) PickerTheme.colors.primary else colorLightPrimary),
        contentAlignment = Alignment.Center
    ) {

        val height = (fontSize + 10).dp

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            WheelView(
                modifier = Modifier.weight(3f),
                itemSize = DpSize(150.dp, height),
                selection = hours.indexOf(selectedTime.hour),
                itemCount = hours.size,
                rowOffset = offset,
                selectorOption = SelectorOptions().copy(
                    selectEffectEnabled = selectorEffectEnabled,
                    enabled = false
                ),
                onFocusItem = {
                    selectedTime = selectedTime.copy(hour = hours[it])
                },
                content = {
                    val displayHour =
                        if (timeFormat == TimeFormat.CLOCK_12H && hours[it] == 0) 12 else hours[it]
                    Text(
                        text = if (displayHour < 10) "0$displayHour" else "$displayHour",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(50.dp),
                        fontSize = fontSize.sp,
                        color = if (darkModeEnabled) PickerTheme.colors.textPrimary else colorLightTextPrimary
                    )
                })

            WheelView(
                modifier = Modifier.weight(3f),
                itemSize = DpSize(150.dp, height),
                itemCount = minutes.size,
                selection = minutes.indexOf(selectedTime.minute),
                rowOffset = offset,
                selectorOption = SelectorOptions().copy(
                    selectEffectEnabled = selectorEffectEnabled,
                    enabled = false
                ),
                onFocusItem = {
                    selectedTime = selectedTime.copy(minute = minutes[it])
                },
                content = {
                    Text(
                        text = if (minutes[it] < 10) "0${minutes[it]}" else "${minutes[it]}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp),
                        fontSize = fontSize.sp,
                        color = if (darkModeEnabled) PickerTheme.colors.textPrimary else colorLightTextPrimary
                    )
                })

            // 오전, 오후 선택
            if (timeFormat == TimeFormat.CLOCK_12H) {
                WheelView(
                    modifier = Modifier.weight(3f),
                    itemSize = DpSize(150.dp, height),
                    selection = formats.indexOf(selectedTime.format),
                    itemCount = formats.size,
                    rowOffset = offset,
                    isEndless = false,
                    selectorOption = SelectorOptions().copy(
                        selectEffectEnabled = selectorEffectEnabled,
                        enabled = false
                    ),
                    onFocusItem = {
                        selectedTime = selectedTime.copy(format = formats[it])
                    },
                    content = {
                        Text(
                            text = formats[it],
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(100.dp)
                                .wrapContentWidth(),
                            fontSize = fontSize.sp,
                            color = if (darkModeEnabled) PickerTheme.colors.textPrimary else colorLightTextPrimary
                        )
                    })
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (darkModeEnabled) PickerTheme.pallets else lightPallet
                    )
                ),
        ) {}

        SelectorView(darkModeEnabled = darkModeEnabled, offset = offset)
    }
}

@Composable
fun WheelView(
    modifier: Modifier = Modifier,
    itemSize: DpSize = DpSize(256.dp, 256.dp),
    selection: Int = 0,
    itemCount: Int,
    rowOffset: Int = 3,
    isEndless: Boolean = true,
    onFocusItem: (Int) -> Unit,
    userScrollEnabled: Boolean = true,
    selectorOption: SelectorOptions = SelectorOptions(),
    lazyWheelState: LazyListState? = null,
    content: @Composable LazyItemScope.(index: Int) -> Unit,
) {

    InfiniteWheelViewImpl(
        modifier = modifier,
        itemSize = itemSize,
        selection = selection,
        itemCount = itemCount,
        rowOffset = rowOffset,
        isEndless = isEndless,
        onFocusItem = onFocusItem,
        userScrollEnabled = userScrollEnabled,
        selectorOption = selectorOption,
        lazyWheelState = lazyWheelState,
    ) {
        content(it)
    }

}

@Composable
fun InfiniteWheelViewImpl(
    modifier: Modifier,
    itemSize: DpSize,
    selection: Int,
    itemCount: Int,
    rowOffset: Int,
    isEndless: Boolean,
    onFocusItem: (Int) -> Unit,
    selectorOption: SelectorOptions,
    userScrollEnabled: Boolean = true,
    lazyWheelState: LazyListState? = null,
    content: @Composable LazyItemScope.(index: Int) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val count = if (isEndless) itemCount else itemCount + 2 * rowOffset
    val rowOffsetCount = maxOf(1, minOf(rowOffset, 4))
    val rowCount = ((rowOffsetCount * 2) + 1)
    val startIndex = if (isEndless) selection + (itemCount * 1000) - rowOffset else selection

    val state = lazyWheelState ?: rememberLazyListState(startIndex)

    val size = DpSize(itemSize.width, itemSize.height * rowCount)

    val isScrollInProgress = state.isScrollInProgress
    val focusedIndex = remember {
        derivedStateOf { state.firstVisibleItemIndex + rowOffsetCount }
    }

    LaunchedEffect(key1 = itemCount) {
        coroutineScope.launch {
            state.scrollToItem(startIndex)
        }
    }

    LaunchedEffect(key1 = isScrollInProgress) {
        if (!isScrollInProgress) {
            calculateIndexToFocus(state, size.height).let {
                val indexToFocus = if (isEndless) {
                    (it + rowOffsetCount) % itemCount
                } else {
                    ((it + rowOffsetCount) % count) - rowOffset
                }

                onFocusItem(indexToFocus)
                if (state.firstVisibleItemScrollOffset != 0) {
                    coroutineScope.launch {
                        state.animateScrollToItem(it, 0)
                    }
                }
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleItemIndex }.collect {
            if (selectorOption.selectEffectEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }


    Box(
        modifier = modifier
            .height(size.height)
            .fillMaxWidth(),
    ) {

        LazyColumn(
            modifier = Modifier
                .height(size.height)
                .fillMaxWidth(),
            state = state,
            userScrollEnabled = userScrollEnabled,
        ) {


            items(if (isEndless) Int.MAX_VALUE else count) {
                val rotateDegree = calculateIndexRotation(focusedIndex.value, it, rowOffset)
                Box(
                    modifier = Modifier
                        .height(size.height / rowCount)
                        .fillMaxWidth()
                        .graphicsLayer {
                            this.rotationX = rotateDegree
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    if (isEndless) {
                        content(it % itemCount)
                    } else if (it >= rowOffsetCount && it < itemCount + rowOffsetCount) {
                        content((it - rowOffsetCount) % itemCount)
                    }
                }

            }
        }


        if (selectorOption.enabled) {
            SelectionView(
                modifier
                    .height(size.height)
                    .fillMaxWidth(), selectorOptions = selectorOption, rowOffset
            )
        }

    }


}

@Composable
fun SelectorView(modifier: Modifier = Modifier, darkModeEnabled: Boolean, offset: Int) {
    Column(
        modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .weight(offset.toFloat())
                .fillMaxWidth()
                .background(if (darkModeEnabled) PickerTheme.colors.onbackground else colorLightOnBackground),
        )


        Column(
            modifier = Modifier
                .weight(1.13f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .height(0.5.dp)
                    .alpha(0.5f)
                    .background(if (darkModeEnabled) PickerTheme.colors.textPrimary else colorLightTextPrimary)
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .height(0.5.dp)
                    .alpha(0.5f)
                    .background(if (darkModeEnabled) PickerTheme.colors.textPrimary else colorLightTextPrimary)
                    .fillMaxWidth()
            )

        }



        Box(
            modifier = Modifier
                .weight(offset.toFloat())
                .fillMaxWidth()
                .background(if (darkModeEnabled) PickerTheme.colors.onbackground else colorLightOnBackground),
        )
    }
}

@Composable
fun SelectionView(
    modifier: Modifier = Modifier,
    selectorOptions: SelectorOptions,
    rowOffset: Int,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .weight(rowOffset.toFloat())
                .fillMaxWidth(),
        )


        Column(
            modifier = Modifier
                .weight(1.13f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .height(selectorOptions.width)
                    .alpha(selectorOptions.alpha)
                    .background(selectorOptions.color)
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .height(selectorOptions.width)
                    .alpha(selectorOptions.alpha)
                    .background(selectorOptions.color)
                    .fillMaxWidth()
            )

        }



        Box(
            modifier = Modifier
                .weight(rowOffset.toFloat())
                .fillMaxWidth(),
        )
    }
}

private fun calculateIndexToFocus(listState: LazyListState, height: Dp): Int {
    val currentItem = listState.layoutInfo.visibleItemsInfo.firstOrNull()
    var index = currentItem?.index ?: 0

    if (currentItem?.offset != 0) {
        if (currentItem != null && currentItem.offset <= -height.value * 3 / 10) {
            index++
        }
    }
    return index
}

@Composable
private fun calculateIndexRotation(focusedIndex: Int, index: Int, offset: Int): Float {
    return (6 * offset + 1).toFloat() * (focusedIndex - index)
}

data class Time(
    val hour: Int,
    val minute: Int,
    val format: String? = null,
)

data class SelectorOptions(
    val enabled: Boolean = true,
    val selectEffectEnabled: Boolean = true,
    val color: Color = Color.Black.copy(alpha = 0.7f),
    val width: Dp = 0.5.dp,
    val alpha: Float = 0.5f,
)