package pm.bam.mbc.remote

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform