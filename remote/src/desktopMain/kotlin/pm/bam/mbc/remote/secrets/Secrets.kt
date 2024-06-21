package pm.bam.mbc.remote.secrets

actual fun getSecrets(): Secrets = object : Secrets {
    override val supaBaseUrl: String = System.getenv(Secrets.SUPABASE_URL).takeIf { it.isNotEmpty() }
        ?: "https://nrsstqiixzgrsohtahyw.supabase.co"

    override val supaBaseKey: String = System.getenv(Secrets.SUPABASE_KEY).takeIf { it.isNotEmpty() }
        // This a safe public key, don't worry
        ?: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5yc3N0cWlpeHpncnNvaHRhaHl3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTgwOTgxNjgsImV4cCI6MjAzMzY3NDE2OH0.GuxRK6DidTqjM3TuWgLHvbD5mg_XmxASUjdm2YwIHxg"
}