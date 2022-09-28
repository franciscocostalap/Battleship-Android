package com.example.battleshipmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue

import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BattleshipMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {


                }
            }
        }
    }
}






@Composable
fun InputTextField(value: TextFieldValue,onValueChange: (TextFieldValue) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange
    )
}


@Composable
fun HomeScreen(){
    Column(){

        //imagem
        //botao de play?

        //por um scaffold com menubar em baixo
    }
}


@Composable
fun GameSetupScreen(onClick : () -> Unit ){
    //info de quem esta a jogar
    //grid
    //botao de rotate
    Button(
        onClick = onClick,
        modifier = Modifier.size(10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.bs_icon),
            modifier = Modifier
                .size(10.dp)
                ,
            contentDescription = null
        )

    }
    //ships

    //botao de confirmar
}


