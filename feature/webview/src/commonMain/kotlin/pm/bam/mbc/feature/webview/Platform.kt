package pm.bam.mbc.feature.webview

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform