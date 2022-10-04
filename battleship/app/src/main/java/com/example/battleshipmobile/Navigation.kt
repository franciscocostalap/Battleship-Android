package com.example.battleshipmobile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import java.net.URL
import kotlin.reflect.KClass

/**
 * Auxiliary function used to navigate between activities
 * @param other the [Activity] to navigate to
 */
fun <T : Activity> Activity.navigateTo(other: KClass<T>){
    val intent = Intent(this, other.java)
    startActivity(intent)
}


fun Activity.navigateTo(url: String){
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}