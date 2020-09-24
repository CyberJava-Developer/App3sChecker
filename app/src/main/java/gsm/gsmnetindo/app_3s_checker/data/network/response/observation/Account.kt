package gsm.gsmnetindo.app_3s_checker.data.network.response.observation


import com.google.gson.annotations.SerializedName

data class Account(
    val avatar: String,
    @SerializedName("created_at")
    val createdAt: String,
    val email: String,
    val id: Int,
    val phone: String,
    val role: Int,
    val status: String,
    val subscription: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    val user: Int,
    val whatsapp: String
)