package gsm.gsmnetindo.app_3s_checker.data.network.body

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class DataPostRegistration (
    @Field("email")
    @SerializedName("email")
    var email: String,

    @Field("name")
    @SerializedName("name")
    var name: String,

    @Field("whatsapp")
    @SerializedName("whatsapp")
    var whatsapp: String,

    @Field("born_place")
    @SerializedName("born_place")
    @Expose(deserialize = true)
    var bornPlace: String,

    @Field("born_date")
    @Expose(deserialize = true)
    @SerializedName("born_date")
    var bornDate: String,

    @Field("gender")
    @SerializedName("gender")
    var gender: String
)