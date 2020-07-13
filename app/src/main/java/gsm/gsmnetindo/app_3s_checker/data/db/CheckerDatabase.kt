package gsm.gsmnetindo.app_3s_checker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gsm.gsmnetindo.app_3s_checker.data.db.entity.CountryStatusItem

@Database(
    entities = [
    CountryStatusItem::class
    ],
    version = 1
)
abstract class CheckerDatabase: RoomDatabase() {
    abstract fun covidDao(): CovidDao

    companion object{
        @Volatile
        private var instance: CheckerDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CheckerDatabase::class.java, "3s-checker.db"
            )
                .build()
    }
}