package pm.bam.mbc.remote.api

import de.jensklingenberg.ktorfit.http.GET

interface ExampleAPI {

    @GET("people/1/")
    suspend fun getPerson(): String

}