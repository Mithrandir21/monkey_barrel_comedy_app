package pm.bam.mbc.remote.exceptions

import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.exception.AuthRestException
import io.github.jan.supabase.gotrue.exception.AuthWeakPasswordException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.utils.io.errors.IOException

class RemoteRestException(error: String, description: String?, message: String) : RestException(error, description, message)

class RemoteAuthRestException(errorCode: String, message: String) : AuthRestException(errorCode, message)

class RemoteWeakPasswordException(errorCode: String, description: String, val reasons: List<String>) : AuthRestException(errorCode, description)

class RemoteHttpRequestTimeoutException(message: String) : IOException(message)

class RemoteHttpRequestException(message: String) : Exception(message)

internal fun Exception.toRemoteRestException(): Exception = when (this) {
    is RestException -> RemoteRestException(error, description, message ?: "")
    is AuthRestException -> RemoteAuthRestException(errorCode?.value ?: error, message ?: "")
    is AuthWeakPasswordException -> RemoteWeakPasswordException(errorCode?.value ?: error, description ?: "", reasons)
    is HttpRequestTimeoutException -> RemoteHttpRequestTimeoutException(message ?: "")
    is HttpRequestException -> RemoteHttpRequestException(message ?: "")
    else -> this
}