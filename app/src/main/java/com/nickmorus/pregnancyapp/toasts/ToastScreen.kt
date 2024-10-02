package com.nickmorus.pregnancyapp.toasts

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable

@Composable
fun ToastNotInformation(context: Context, message:String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}
