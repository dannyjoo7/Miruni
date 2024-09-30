package com.joo.miruni.presentation.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joo.miruni.theme.lightPallet
import com.ozcanalasalvar.datepicker.ui.theme.PickerTheme
import com.ozcanalasalvar.datepicker.ui.theme.colorLightPrimary
import com.ozcanalasalvar.datepicker.ui.theme.colorLightTextPrimary

@Composable
fun AlarmDisplayDatePicker(
    offset: Int = 4,
    selectedNumber: Int,
    selectedText: String,
    onDurationAmountChanged: (Int) -> Unit,
    onDurationUnitChanged: (String) -> Unit,
    darkModeEnabled: Boolean = true,
) {
    var amounts by remember {
        mutableStateOf(
            when (selectedText) {
                "주" -> (1..52).toList()
                "개월" -> (1..12).toList()
                "년" -> (1..10).toList()
                else -> (1..100).toList()
            }
        )
    }
    val durationUnit: List<String> = listOf("일", "주", "개월", "년")

    var selectedDurationAmounts by remember { mutableIntStateOf(selectedNumber) }
    var selectedDurationUnit by remember { mutableStateOf(selectedText) }

    val height = 30.dp

    LaunchedEffect(selectedDurationUnit) {
        amounts = when (selectedDurationUnit) {
            "주" -> (1..52).toList()
            "개월" -> (1..12).toList()
            "년" -> (1..10).toList()
            else -> (1..100).toList()
        }

        selectedDurationAmounts = amounts.first()
        onDurationAmountChanged(selectedDurationAmounts)
    }

    LaunchedEffect(selectedDurationAmounts) {
        onDurationAmountChanged(selectedDurationAmounts)
    }

    LaunchedEffect(selectedDurationUnit) {
        onDurationUnitChanged(selectedDurationUnit)
    }

    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 200.dp)
            .width(200.dp)
            .height(IntrinsicSize.Max)
            .background(if (darkModeEnabled) PickerTheme.colors.primary else colorLightPrimary),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 숫자 선택 WheelView
            WheelView(
                modifier = Modifier.weight(1f),
                itemSize = DpSize(150.dp, height),
                selection = amounts.indexOf(selectedDurationAmounts),
                itemCount = amounts.size,
                rowOffset = offset,
                isEndless = true,
                onFocusItem = {
                    selectedDurationAmounts = amounts[it]
                    onDurationAmountChanged(amounts[it])
                },
                content = {
                    Text(
                        text = amounts[it].toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(50.dp),
                        fontSize = 16.sp,
                        color = if (darkModeEnabled) PickerTheme.colors.textPrimary else colorLightTextPrimary
                    )
                }
            )

            // 한글 선택 WheelView
            WheelView(
                modifier = Modifier.weight(1f),
                itemSize = DpSize(150.dp, height),
                selection = durationUnit.indexOf(selectedDurationUnit),
                itemCount = durationUnit.size,
                onFocusItem = {
                    selectedDurationUnit = durationUnit[it]
                    onDurationUnitChanged(durationUnit[it])
                },
                isEndless = false,
                content = {
                    Text(
                        text = durationUnit[it],
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp),
                        fontSize = 16.sp,
                        color = if (darkModeEnabled) PickerTheme.colors.textPrimary else colorLightTextPrimary
                    )
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (darkModeEnabled) PickerTheme.pallets else lightPallet
                    )
                )
        ) {}

        SelectorView(darkModeEnabled = darkModeEnabled, offset = offset)
    }
}