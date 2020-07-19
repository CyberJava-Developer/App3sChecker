package gsm.gsmnetindo.app_3s_checker.data.db.entity.detail


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("created_at")
    val createdAt: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("updated_at")
    val updatedAt: String
)