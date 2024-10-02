package com.nickmorus.pregnancyapp.screens.draw

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat


fun shareAppPlayStoreUrl(context: Context){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, "Посмотри это прекрасное приложение")
            putExtra(Intent.EXTRA_TEXT, "Посмотри это удобное приложение\n "+"https://play.google.com/store/apps/details?id="+context.applicationInfo.packageName)
            type = "text/plain"

        }
        val shareIntent = Intent.createChooser(sendIntent, "Приложение для женщин")
        ContextCompat.startActivity(context,shareIntent,null)
    }
