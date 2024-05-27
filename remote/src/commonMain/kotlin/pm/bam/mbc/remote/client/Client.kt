package pm.bam.mbc.remote.client

import io.ktor.client.*

internal expect fun getHttpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient