package pm.bam.mbc.feature.blogs

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform