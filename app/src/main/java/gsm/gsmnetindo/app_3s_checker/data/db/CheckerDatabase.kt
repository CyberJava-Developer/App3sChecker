package gsm.gsmnetindo.app_3s_checker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import gsm.gsmnetindo.app_3s_checker.data.db.entity.CovidDataItem
import gsm.gsmnetindo.app_3s_checker.data.db.entity.FeedItem

@Database(
    entities = [
        CovidDataItem::class,
        FeedItem::class
    ],
    version = 1
)
abstract class CheckerDatabase : RoomDatabase() {
    abstract fun covidDao(): CovidDao
    abstract fun feedsDao(): FeedsDao

    // migration


    companion object {
        @Volatile
        private var instance: CheckerDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE IF NOT EXISTS feeds (id INTEGER UNIQUE PRIMARY KEY NOT NULL, link TEXT NOT NULL, createdAt TEXT NOT NULL, image TEXT NOT NULL, title TEXT NOT NULL)")
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CheckerDatabase::class.java, "3s-checker.db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
    }
}