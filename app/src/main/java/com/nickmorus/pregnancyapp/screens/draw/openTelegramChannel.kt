package com.nickmorus.pregnancyapp.screens.draw

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast


fun Context.openTelegramChannel(channelUrl: String) {

    try {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channelUrl))
        startActivity(intent)

    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "Telegram не установлен", Toast.LENGTH_SHORT).show()
    } catch (t: Throwable) {
        Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show()
    }
}
