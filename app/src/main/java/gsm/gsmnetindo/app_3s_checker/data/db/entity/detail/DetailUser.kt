package gsm.gsmnetindo.app_3s_checker.data.db.entity.detail

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


const val USER_ID = 0
@Entity(tableName = "user_details")
data class DetailUser(
    @Embedded(prefix = "account_")
    val account: Account,
    @Embedded(prefix = "detail_")
    val detail: Detail,
    @Embedded(prefix = "home_")
    val home: Home,
    @Embedded(prefix = "location_")
    val location: Location,
    @Embedded
    val status: Status,
    val subscription: Subscription
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = USER_ID
}