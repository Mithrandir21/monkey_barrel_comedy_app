package pm.bam.mbc.domain

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform