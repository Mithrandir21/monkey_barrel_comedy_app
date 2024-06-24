package pm.bam.mbc.compose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform