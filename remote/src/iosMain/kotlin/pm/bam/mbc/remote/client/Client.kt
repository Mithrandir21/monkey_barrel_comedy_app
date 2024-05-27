package pm.bam.mbc.remote.client

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

actual fun getHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Darwin) {
    config(this)

    engine {
        configureRequest {
            //setAllowsCellularAccess(true)
        }
    }
}