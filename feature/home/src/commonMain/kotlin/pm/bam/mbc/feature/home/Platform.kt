package pm.bam.mbc.feature.home

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform