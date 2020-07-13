package gsm.gsmnetindo.app_3s_checker.data.db.entity


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "feeds", indices = [Index(value = ["id"], unique = true)])
data class FeedItem(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val image: String,
    val link: String,
    val title: String
)