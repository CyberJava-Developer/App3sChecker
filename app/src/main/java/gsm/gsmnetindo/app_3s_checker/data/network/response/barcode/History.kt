package gsm.gsmnetindo.app_3s_checker.data.network.response.barcode


import com.google.gson.annotations.SerializedName

data class History(
    val answer1: String,
    val answer2: String,
    val answer3: String?,
    val answer4: String?,
    val authority: Int?,
    @SerializedName("created_at")
    val createdAt: String,
    val id: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    val verified: Boolean
)