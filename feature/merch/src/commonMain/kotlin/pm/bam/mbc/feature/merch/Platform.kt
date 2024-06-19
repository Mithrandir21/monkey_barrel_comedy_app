package pm.bam.mbc.feature.merch

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform