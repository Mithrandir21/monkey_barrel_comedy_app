package pm.bam.mbc.feature.merch

class DesktopPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = DesktopPlatform()