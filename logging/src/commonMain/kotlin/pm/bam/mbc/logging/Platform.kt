package pm.bam.mbc.logging

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform