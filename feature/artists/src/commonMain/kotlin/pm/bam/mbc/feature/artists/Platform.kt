package pm.bam.mbc.feature.artists

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform