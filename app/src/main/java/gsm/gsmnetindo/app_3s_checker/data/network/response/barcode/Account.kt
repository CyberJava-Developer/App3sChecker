package gsm.gsmnetindo.app_3s_checker.data.network.response.barcode


import com.google.gson.annotations.SerializedName

data class Account(
    val avatar: String,
    @SerializedName("created_at")
    val createdAt: String,
    val email: String,
    val phone: String,
    val role: Int,
    val status: String,
    val subscription: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    val whatsapp: String
)