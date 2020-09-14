package gsm.gsmnetindo.app_3s_checker.data.network.response.detail


import com.google.gson.annotations.SerializedName

data class Subscription(
    val account: Int,
    val admin: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val from: String,
    val id: Int,
    val plan: Int,
    @SerializedName("price_per_month")
    val pricePerMonth: Int,
    @SerializedName("quota_account")
    val quotaAccount: Int,
    val salesman: Int,
    val status: String,
    val to: String,
    @SerializedName("updated_at")
    val updatedAt: String
)