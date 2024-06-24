package pm.bam.mbc.feature.news

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform