package pm.bam.mbc.remote.logic


enum class RemoteBuildType(val buildTypeName: String) {
    DEBUG("debug"),
    RELEASE("release")
}

interface RemoteBuildUtil {
    /** Get an enum representing the [RemoteBuildType] of this class. */
    fun buildType(): RemoteBuildType
}

expect fun getRemoteBuildUtil(): RemoteBuildUtil