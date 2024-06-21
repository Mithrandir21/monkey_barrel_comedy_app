package pm.bam.mbc.remote.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

internal expect fun getHttpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient