package gsm.gsmnetindo.app_3s_checker.data.db.entity


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "days_status", indices = [Index(value = ["id"], unique = true)])
data class CountryStatusItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("Active")
    val active: Int,
    @SerializedName("City")
    val city: String,
    @SerializedName("CityCode")
    val cityCode: String,
    @SerializedName("Confirmed")
    val confirmed: Int,
    @SerializedName("Country")
    val country: String,
    @SerializedName("CountryCode")
    val countryCode: String,
    @SerializedName("Date")
    val date: String,
    @SerializedName("Deaths")
    val deaths: Int,
    @SerializedName("Lat")
    val lat: String,
    @SerializedName("Lon")
    val lon: String,
    @SerializedName("Province")
    val province: String,
    @SerializedName("Recovered")
    val recovered: Int
)