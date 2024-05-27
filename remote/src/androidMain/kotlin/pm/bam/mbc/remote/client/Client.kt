package pm.bam.mbc.remote.client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import java.util.concurrent.TimeUnit

actual fun getHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    config(this)

    engine {
        config {
            connectTimeout(2, TimeUnit.SECONDS)
        }
    }
}