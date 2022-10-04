package com.example.battleshipmobile.views.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.model.Ship
import com.example.battleshipmobile.viewModels.ConfigViewModel
import com.example.battleshipmobile.viewModels.PlayScreenViewModel
import com.example.battleshipmobile.views.CenterAligned
import com.example.battleshipmobile.views.Board
import com.example.battleshipmobile.views.GRID_SIZE


class PlayScreenActivity : ComponentActivity() {

    private val vm by viewModels<PlayScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.updateShips(
            listOf(
                Ship(2, "Destroyer"),
                Ship(3, "Submarine"),
                Ship(3, "Cruiser"),
                Ship(4, "Battleship"),
                Ship(5, "Carrier")
            )
        )
        setContent {
           Column {
               Row {
                   Text(text ="Player 1")
                   //vs
                   Text(text ="Player 2", textAlign = TextAlign.End)
               }


               //display whose turn it is and nshots missing

               Board(10, GRID_SIZE)

               CenterAligned{
                   Button(
                       onClick = { /*TODO*/ },
                       modifier = Modifier.padding(16.dp)
                   ) {
                       Text(text = "CONFIRM PLACEMENT")
                   }
               }

               Spacer(modifier = Modifier.height(10.dp))

               ShipList(vm.ships, GRID_SIZE)



           }


        }
    }
}

@Composable
fun ShipList(shipList : List<Ship>, shipSize : Dp){
    Column {
        shipList.forEach { ship ->
            Row {
                Spacer(modifier =Modifier.size(4.dp))
                ship.Draw(shipSize)

                Spacer(modifier =Modifier.size(4.dp))
            }
        }
    }



}










class ConfigScreenActivity : ComponentActivity() {

    private val vm by viewModels<ConfigViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


                Column {
                    NumberConfig(
                        value = vm.nShots,
                        configName = "Number of Shots",
                        onValueChange = { vm.updateShotNumber(it) }
                    )

                }





            //regraS?



            //botao com 'play no fim da pag

        }
    }

}

@Composable
fun NumberConfig (value : Int, configName : String, onValueChange: (Int) -> Unit){

    Row(modifier = Modifier.border(1.dp, Color.DarkGray)){
        Button(onClick = { onValueChange(value -1) }) {
            Icon(Icons.Rounded.KeyboardArrowLeft, contentDescription = "Less")
        }
        Text(text = value.toString())

        Button(onClick = { onValueChange(value +1) }) {
            Icon(Icons.Rounded.KeyboardArrowRight, contentDescription = "More")
        }
        Text(text = configName)
        //regras
    }

}
