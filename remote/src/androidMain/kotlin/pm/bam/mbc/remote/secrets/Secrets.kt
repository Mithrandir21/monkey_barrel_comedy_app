package pm.bam.mbc.remote.secrets

import pm.bam.mbc.remote.BuildConfig

actual fun getSecrets(): Secrets = object: Secrets {
    override val supaBaseUrl: String = BuildConfig.SUPABASE_URL
    override val supaBaseKey: String = BuildConfig.SUPABASE_KEY
}