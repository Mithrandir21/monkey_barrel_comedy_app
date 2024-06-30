package pm.bam.mbc.domain.services

import kotlin.coroutines.cancellation.CancellationException

interface AuthServices {

    suspend fun loggedIn(): Boolean

    @Throws(Exception::class, CancellationException::class)
    suspend fun signUp(email: String, password: String)

    @Throws(Exception::class, CancellationException::class)
    suspend fun login(email: String, password: String)

    @Throws(Exception::class, CancellationException::class)
    suspend fun logout()

    @Throws(Exception::class, CancellationException::class)
    suspend fun resetPassword(email: String)

}