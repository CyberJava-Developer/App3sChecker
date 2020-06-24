package gsm.gsmnetindo.app_3s_checker.internal

object Secret {

    init {
        System.loadLibrary("native-lib")
    }

    external fun apiKey(): String

    external fun apiVersion(): String

    external fun baseApi(): String

}