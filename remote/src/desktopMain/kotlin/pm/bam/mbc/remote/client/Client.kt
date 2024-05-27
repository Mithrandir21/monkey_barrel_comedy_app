package pm.bam.mbc.remote.client

import io.ktor.client.*
import io.ktor.client.engine.apache5.Apache5

actual fun getHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Apache5) {
    config(this)
}