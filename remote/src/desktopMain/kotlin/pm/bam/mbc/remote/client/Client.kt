package pm.bam.mbc.remote.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.apache5.Apache5

actual fun getHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Apache5) {
    config(this)
}