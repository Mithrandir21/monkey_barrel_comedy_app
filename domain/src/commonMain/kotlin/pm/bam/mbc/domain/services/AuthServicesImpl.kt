package pm.bam.mbc.domain.services

import pm.bam.mbc.remote.services.RemoteAuth

internal class AuthServicesImpl(private val remoteAuth: RemoteAuth) : AuthServices {

    override suspend fun loggedIn(): Boolean = remoteAuth.loggedIn()

    override suspend fun signUp(email: String, password: String) = remoteAuth.signUp(email, password)

    override suspend fun login(email: String, password: String) = remoteAuth.login(email, password)

    override suspend fun logout() = remoteAuth.logout()

    override suspend fun resetPassword(email: String) = remoteAuth.resetPassword(email)

}