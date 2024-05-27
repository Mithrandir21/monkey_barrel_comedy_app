package pm.bam.mbc.remote.client

import io.ktor.client.*

interface ClientProvider {

    fun client(): HttpClient

}