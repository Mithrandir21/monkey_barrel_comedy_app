package pm.bam.mbc.remote.services

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.debug
import pm.bam.mbc.remote.exceptions.toRemoteRestException

internal class RemoteAuthImpl(
    private val logger: Logger,
    private val supabaseClient: SupabaseClient
) : RemoteAuth {

    override suspend fun loggedIn(): Boolean = supabaseClient.auth.currentUserOrNull() != null

    override suspend fun signUp(email: String, password: String) {
        debug(logger) { "Signing up with email: $email" }

        try {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        } catch (e: Exception) {
            debug(logger, e) { "Sign up failed" }
            throw e.toRemoteRestException()
        }

        debug(logger) { "Sign up successful" }
    }

    override suspend fun login(email: String, password: String) {
        debug(logger) { "Logging in with email: $email" }

        try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        } catch (e: Exception) {
            debug(logger, e) { "Login failed" }
            throw e.toRemoteRestException()
        }

        debug(logger) { "Login successful" }
    }


    override suspend fun logout() {
        debug(logger) { "Logging out" }

        try {
            supabaseClient.auth.signOut()
        } catch (e: Exception) {
            debug(logger, e) { "Logout failed" }
            throw e.toRemoteRestException()
        }

        debug(logger) { "Logout successful" }
    }

    override suspend fun resetPassword(email: String) {
        debug(logger) { "Sending email for password reset: $email" }

        try {
            supabaseClient.auth.resetPasswordForEmail(email)
        } catch (e: Exception) {
            debug(logger, e) { "Failed to send email for password reset" }
            throw e.toRemoteRestException()
        }

        debug(logger) { "Sent email for password successfully" }
    }
}