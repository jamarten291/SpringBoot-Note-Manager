# Cargar variables desde .env
if (Test-Path ".env") {
    Get-Content ".env" | ForEach-Object {
        if ($_ -match "^\s*#") { return } # Ignorar comentarios
        $parts = $_ -split "="
        if ($parts.Length -eq 2) {
            [System.Environment]::SetEnvironmentVariable($parts[0], $parts[1])
        }
    }
    Write-Host "✅ Variables de entorno cargadas desde .env"
} else {
    Write-Host "⚠️ Archivo .env no encontrado. Usando variables del sistema."
}

# Ejecutar el proyecto con Gradle Wrapper
if (Test-Path ".\gradlew") {
    .\gradlew bootRun
} else {
    Write-Host "❌ No se encontró gradlew. ¿Estás seguro de que este es un proyecto Gradle?"
}