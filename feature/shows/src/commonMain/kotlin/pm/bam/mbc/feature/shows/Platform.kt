package pm.bam.mbc.feature.shows

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform