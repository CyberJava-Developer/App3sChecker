package gsm.gsmnetindo.app_3s_checker.data.network.response

data class UserLoginResponse(
    val phone: String,
    val registered: Boolean,
    val role: Int,
    val jwt: String,
    val code: String
)