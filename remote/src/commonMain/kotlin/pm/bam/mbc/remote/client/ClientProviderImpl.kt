package pm.bam.mbc.remote.client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pm.bam.mbc.remote.logic.RemoteBuildType


class ClientProviderImpl(buildType: RemoteBuildType) : ClientProvider {

    private val client = getHttpClient {
        defaultRequest {
            when (buildType) {
                RemoteBuildType.DEBUG -> url("https://google.com/api/v2/")
                RemoteBuildType.RELEASE -> url("https://google.com/api/v2/")
            }
        }

        engine {
            pipelining = true
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                encodeDefaults = true // Makes sure default field values are encoded
                ignoreUnknownKeys = true
            })
        }
    }

    override fun client(): HttpClient = client
}