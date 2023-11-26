package app.divarinterview.android.di

import android.content.Context
import app.divarinterview.android.common.container.PreferencesContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    private const val PREF_NAME = "MyPrefsFile"

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): PreferencesContainer {
        return PreferencesContainer(context)
    }
}