package pm.bam.mbc.remote.secrets

// TODO - replace with actual secrets for iOS
actual fun getSecrets(): Secrets = object: Secrets {
    override val supaBaseUrl: String = "https://nrsstqiixzgrsohtahyw.supabase.co"

    // This a safe public key, don't worry
    override val supaBaseKey: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5yc3N0cWlpeHpncnNvaHRhaHl3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTgwOTgxNjgsImV4cCI6MjAzMzY3NDE2OH0.GuxRK6DidTqjM3TuWgLHvbD5mg_XmxASUjdm2YwIHxg"
}