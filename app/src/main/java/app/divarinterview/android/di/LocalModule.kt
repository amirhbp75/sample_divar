package app.divarinterview.android.di

import android.content.Context
import androidx.room.Room
import app.divarinterview.android.common.container.PreferencesContainer
import app.divarinterview.android.data.database.DivarDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): PreferencesContainer {
        return PreferencesContainer(context)
    }

    @Provides
    @Singleton
    fun provideDivarDatabase(@ApplicationContext context: Context): DivarDatabase {
        return Room.databaseBuilder(context, DivarDatabase::class.java, "divar.db").build()
    }
}