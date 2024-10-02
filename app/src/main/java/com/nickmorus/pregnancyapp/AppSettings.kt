package com.nickmorus.pregnancyapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pregnancy_plus_settings")

@Singleton
class AppSettings @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    init{
        CoroutineScope(Dispatchers.Default).launch {
            context.dataStore.data.collect{ preferences->
                setLastArticle(preferences[LAST_ARTICLE_ID]?: "")
                setAppStartedFirstTime(preferences[APP_STARTED_FIRST_TIME]?: true)


            }
        }
    }
    val isPregnant: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_PREGNANT_STATE] ?: false
        }

    val firstDay: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[FIRST_DAY_PREGNANT] ?: ""
        }


    suspend fun setPregnancy(isPregnant: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_PREGNANT_STATE] = isPregnant
        }
    }
    suspend fun setFirstDayPregnant(firstDay: String) {
        context.dataStore.edit { preferences ->
            preferences[FIRST_DAY_PREGNANT] = firstDay
        }
    }

    private var _lastArticleId = MutableStateFlow("")
    private fun setLastArticle(id: String){
        _lastArticleId.value = id
         CoroutineScope(Dispatchers.Default).launch {
             context.dataStore.edit {preferences->
                 preferences[LAST_ARTICLE_ID] = _lastArticleId.value
             }
         }
    }


    private var _appStartedFirstTime = true
    val appStartedFirstTime
        get() = _appStartedFirstTime
    fun setAppStartedFirstTime(value: Boolean){
        _appStartedFirstTime = value
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit {preferences->
                preferences[APP_STARTED_FIRST_TIME] = _appStartedFirstTime
            }
        }
    }








    companion object {
        val IS_PREGNANT_STATE = booleanPreferencesKey("is_pregnant")
        val LAST_ARTICLE_ID = stringPreferencesKey("last_article_id")
        val APP_STARTED_FIRST_TIME = booleanPreferencesKey("app_started_first_time")
        val FIRST_DAY_PREGNANT = stringPreferencesKey("first_day_pregnant")

    }
}
