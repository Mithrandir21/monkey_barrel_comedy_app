package pm.bam.mbc.remote.secrets

interface Secrets {
    val supaBaseUrl: String
    val supaBaseKey: String

    companion object {
        const val SUPABASE_URL = "SUPABASE_URL"
        const val SUPABASE_KEY = "SUPABASE_KEY"
    }
}

expect fun getSecrets(): Secrets