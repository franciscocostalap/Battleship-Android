package com.example.battleshipmobile.ui.views.game

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Rotate90DegreesCcw
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.battleship.service.model.Ship
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.views.general.OrientedLayout

data class ShipData(
    val id: String,
    val ship: Ship
)

val INDIGO_7 = Color( 66, 99, 235)
val GRAY_8 = Color( 52, 58, 64)
private val CONTROL_BUTTON_SPACER_DP = 2.dp
private val ICON_BUTTON_PADDING = 16.dp
private val ICON_BUTTON_SIZE = 42.dp
private const val NUM_OF_BUTTONS = 3


@Composable
fun FleetCompositionView(
    availableShips: List<ShipData>,
    selectedShip: ShipData?,
    onShipSelected: (ShipData) -> Unit = {}
) {
    val availableShipsBySize = availableShips.groupBy { it.ship.size }
    val height = ICON_BUTTON_SIZE.times(NUM_OF_BUTTONS) + CONTROL_BUTTON_SPACER_DP.times(NUM_OF_BUTTONS-1) + ICON_BUTTON_PADDING.times(2).times(
        NUM_OF_BUTTONS)
    val shipSquareSize = (SQUARE_BASE_SIDE / (SQUARE_SHRINK_FACTOR * 10)).dp
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(16.dp).height(height).width(shipSquareSize.times(5))
    ) {

        items(availableShipsBySize.toList()) { (_, shipsPerSize) ->
            LazyRow {
                items(shipsPerSize) { shipData ->
                    val isSelected = selectedShip?.id == shipData.id
                    val actualShip = if (isSelected) selectedShip else shipData
                    checkNotNull(actualShip){"Unexpected null shipData."}
                    ShipView(
                        shipData = actualShip,
                        squareSize =  shipSquareSize,
                        onClick = { onShipSelected(actualShip) },
                        isSelected = selectedShip?.id == shipData.id
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun FleetCompositionControls(
    onRotateRequested: () -> Unit = {},
    onResetRequested: () -> Unit = {},
    onSubmitRequested: () -> Unit = {},
    isSubmitEnabled: Boolean = true,
    isResetEnabled: Boolean = true,
    isRotateEnabled: Boolean = true
){
    Column{
        FleetCompositionControlButton(
            onClick = onRotateRequested,
            testTag = TestTags.LayoutDefinition.RotateButton,
            description = "Rotate Selected Ship",
            imageVector = Icons.Default.Rotate90DegreesCcw,
            isEnabled = isRotateEnabled
        )
        Spacer(modifier = Modifier.height(CONTROL_BUTTON_SPACER_DP))
        FleetCompositionControlButton(
            onClick = onResetRequested,
            testTag = TestTags.LayoutDefinition.ResetFleetButton,
            description = "Reset Fleet",
            imageVector = Icons.Default.RestartAlt,
            isEnabled = isResetEnabled
        )
        Spacer(modifier = Modifier.height(CONTROL_BUTTON_SPACER_DP))
        FleetCompositionControlButton(
            onClick = onSubmitRequested,
            testTag = "", //TODO()
            description = "Submit layout",
            imageVector = Icons.Default.Done,
            isEnabled = isSubmitEnabled
        )
    }
}

@Composable
fun FleetCompositionControlButton(
    onClick: () -> Unit,
    testTag: String,
    description: String,
    imageVector: ImageVector,
    isEnabled: Boolean = true
) {
    val iconBackgroundColor = if (isEnabled) MaterialTheme.colors.primary else MaterialTheme.colors.onSecondary
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(ICON_BUTTON_PADDING)
            .background(color = iconBackgroundColor)
            .testTag(testTag),
        enabled = isEnabled
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = description,
            tint = Color.White
        )
    }
}

@Composable
fun ShipView(shipData: ShipData, squareSize: Dp, onClick: () -> Unit, isSelected: Boolean = false) {

    val selectedModifier = if (isSelected) {
        Modifier.border(5.dp, Color.Black)
    } else {
        Modifier
    }
    OrientedLayout(
        orientation = shipData.ship.orientation,
        modifier = selectedModifier.clickable { onClick() }
    ) {
        repeat(shipData.ship.size) {
            Box(
                modifier = Modifier
                    .size(squareSize)
                    .border(2.dp, GRAY_8)
                    .background(color = INDIGO_7)
            )
        }
    }
}