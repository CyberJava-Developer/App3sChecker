package gsm.gsmnetindo.app_3s_checker.internal

import java.io.IOException

class NoConnectivityException: IOException()

class UnauthorizedException: IOException()

class NotVerifiedException: IOException()

class NotFoundException(override val message: String): Exception(message)

class RestApiException(override val message: String, code: Int) : Exception(message, (code to Throwable()) as Throwable)

class LocationPermissionException: Exception()
class LocationNotEnabledException: Exception()