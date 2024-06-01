package pm.bam.mbc.feature.podcast

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform