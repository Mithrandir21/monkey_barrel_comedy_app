package pm.bam.mbc.remote.services

import pm.bam.mbc.remote.exceptions.RemoteAuthRestException
import pm.bam.mbc.remote.exceptions.RemoteHttpRequestException
import pm.bam.mbc.remote.exceptions.RemoteHttpRequestTimeoutException
import pm.bam.mbc.remote.exceptions.RemoteRestException
import pm.bam.mbc.remote.exceptions.RemoteWeakPasswordException
import kotlin.coroutines.cancellation.CancellationException

interface RemoteAuth {

    suspend fun loggedIn(): Boolean

    @Throws(
        RemoteRestException::class,
        RemoteAuthRestException::class,
        RemoteWeakPasswordException::class,
        RemoteHttpRequestTimeoutException::class,
        RemoteHttpRequestException::class,
        CancellationException::class
    )
    suspend fun signUp(email: String, password: String)

    @Throws(
        RemoteRestException::class,
        RemoteAuthRestException::class,
        RemoteHttpRequestTimeoutException::class,
        RemoteHttpRequestException::class,
        CancellationException::class
    )
    suspend fun login(email: String, password: String)

    @Throws(
        RemoteRestException::class,
        RemoteAuthRestException::class,
        RemoteHttpRequestTimeoutException::class,
        RemoteHttpRequestException::class,
        CancellationException::class
    )
    suspend fun logout()

    @Throws(
        RemoteRestException::class,
        RemoteAuthRestException::class,
        RemoteHttpRequestTimeoutException::class,
        RemoteHttpRequestException::class,
        CancellationException::class
    )
    suspend fun resetPassword(email: String)

}