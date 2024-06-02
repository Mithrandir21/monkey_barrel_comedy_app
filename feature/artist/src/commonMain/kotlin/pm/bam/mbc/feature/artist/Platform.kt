package pm.bam.mbc.feature.artist

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform