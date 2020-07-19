package gsm.gsmnetindo.app_3s_checker.data.db.entity.detail


import com.google.gson.annotations.SerializedName

data class Status(
    val authority: String,
    @SerializedName("created_at")
    val createdAt: String,
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val verified: Boolean
)