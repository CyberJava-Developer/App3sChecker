package gsm.gsmnetindo.app_3s_checker.internal.process

import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.Kuesioner

class Likert(data: Kuesioner) {
    private var likertResult: LikertResult
    private var status: Int
    private var accuracy: Double
    init {
        val sa1 = when(data.answer1) {
            "iya" -> 25
            "tidak" -> 0
            else -> 0
        }
        val sa2 = when(data.answer2) {
            "iya" -> 50
            "tidak" -> 0
            else -> 0
        }
        val sa4 = when(data.answer4) {
            null -> 0
            "negatif" -> 0
            "positif" -> 100
            else -> 0
        }
        status = sa1 + sa2 + sa4
        val pa3 = when(data.answer3) {
            null -> 0
            "tidak" -> 10
            "ragu" -> 25
            "iya" -> 50
            else -> 0
        }
        val pa4 = when(data.answer4) {
            null -> 0 // tidak ada data rapid test
            else -> 75 // negatif atau positif
        }
        accuracy = ((pa3.toDouble() + pa4.toDouble()) / 125) * 100
        likertResult = LikertResult(status, accuracy)
    }
    fun get(): LikertResult = likertResult
}
data class LikertResult(
    val status: Int,
    val accuracy: Double
)