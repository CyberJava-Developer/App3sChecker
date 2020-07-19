package gsm.gsmnetindo.app_3s_checker.data.db.entity.detail


import com.google.gson.annotations.SerializedName

data class Subscription(
    val account: String,
    val admin: String,
    @SerializedName("created_at")
    val createdAt: String,
    val from: String,
    val plan: String,
    val to: String,
    @SerializedName("updated_at")
    val updatedAt: String
)