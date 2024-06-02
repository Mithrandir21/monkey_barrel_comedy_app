package pm.bam.mbc.feature.podcasts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform