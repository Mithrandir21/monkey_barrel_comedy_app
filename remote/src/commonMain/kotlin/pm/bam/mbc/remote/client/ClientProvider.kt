package pm.bam.mbc.remote.client

import io.ktor.client.HttpClient

interface ClientProvider {

    fun client(): HttpClient

}