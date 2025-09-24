package com.datdang.snakeblock.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.datdang.snakeblock.data.db.game.GameDatabase
import com.datdang.snakeblock.data.util.DiskExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideGameDatabase(
        @ApplicationContext context: Context,
        diskExecutor: DiskExecutor
    ): GameDatabase {
        return Room
            .databaseBuilder(context, GameDatabase::class.java, "game.db")
            .setQueryExecutor(diskExecutor)
            .setTransactionExecutor(diskExecutor)
//            .addMigrations(MIGRATION_1_2)
            .build()
    }
}
//
//private val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(db: SupportSQLiteDatabase) {
//        db.execSQL("ALTER TABLE best_time ADD COLUMN score INTEGER")
//    }
//}
