package pm.bam.mbc.remote.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

actual fun getHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Darwin) {
    config(this)

    engine {
        configureRequest {
            //setAllowsCellularAccess(true)
        }
    }
}