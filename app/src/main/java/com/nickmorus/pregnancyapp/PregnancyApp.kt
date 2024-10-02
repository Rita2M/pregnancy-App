package com.nickmorus.pregnancyapp

import android.app.Application
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class PregnancyApp @Inject constructor()
    : Application() {
}
