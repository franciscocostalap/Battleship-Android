import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

data class SocialInfo(val name: String, val nr: Int, val email: String)

@Composable
fun Credits(
    onSendEmailRequested: () -> Unit = { },
    socials: List<SocialInfo> = emptyList()
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            repeat(socials.size){
                val author = socials[it]
                Text(text = "${author.name} - ${author.nr}", style = MaterialTheme.typography.h5)
            }
            Box( modifier = Modifier.clickable { onSendEmailRequested()}) {
                Icon(imageVector = Icons.Default.Email, contentDescription = null)
            }

        }
    }
}

