package com.example.battleshipmobile.ui.views.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Rotate90DegreesCcw
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun FleetCompositionView(
    availableShips: List<ShipData>,
    selectedShip: ShipData?,
    onShipSelected: (ShipData) -> Unit = {}
) {
    val availableShipsBySize = availableShips.groupBy { it.ship.size }
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(16.dp)
    ) {
        items(availableShipsBySize.toList()) { (_, shipsPerSize) ->
            LazyRow {
                items(shipsPerSize) { shipData ->
                    val isSelected = selectedShip?.id == shipData.id
                    val actualShip = if (isSelected) selectedShip else shipData
                    checkNotNull(actualShip){"Unexpected null shipData."}
                    ShipView(
                        shipData = actualShip,
                        squareSize =  (SQUARE_BASE_SIDE / (SQUARE_SHRINK_FACTOR * 10)).dp,
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
    isShipSelected: Boolean
){
    Column{
        IconButton(
            onClick = onRotateRequested,
            modifier = Modifier
                .padding(16.dp)
                .testTag(TestTags.LayoutDefinition.RotateButton)
        ) {
            Icon(
                imageVector = Icons.Default.Rotate90DegreesCcw,
                contentDescription = "Rotate Selected Ship",
                tint = MaterialTheme.colors.primary
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        IconButton(
            onClick = onResetRequested,
            modifier = Modifier
                .padding(16.dp)
                .testTag(TestTags.LayoutDefinition.ResetFleetButton)
        ) {
            Icon(
                imageVector = Icons.Default.RestartAlt,
                contentDescription = "Reset Fleet",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun ShipView(shipData: ShipData, squareSize: Dp, onClick: () -> Unit, isSelected: Boolean = false) {

    val selectedModifier = if (isSelected) {
        Modifier.border(2.dp, Color.Red)
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
                    .border(2.dp, Color.Black)
                    .background(color = MaterialTheme.colors.secondary)
            )
        }
    }
}