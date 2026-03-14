param(
    [string]$DriveLetter = "K"
)

$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent $PSScriptRoot
$shortRepoRoot = cmd /c "for %I in (""$repoRoot"") do @echo %~sI"
$shortRepoRoot = $shortRepoRoot.Trim()
if ([string]::IsNullOrWhiteSpace($shortRepoRoot)) {
    throw "Failed to resolve DOS short path for repo root: $repoRoot"
}

$gradleHome = "$DriveLetter`:\.gradle-home"
$gradleCommand = ".\gradlew.bat :app:assembleDebugNoMinify"

try {
    cmd /c "subst $DriveLetter`: /d" *> $null
    cmd /c "subst $DriveLetter`: $shortRepoRoot"
    Push-Location "$DriveLetter`:\"
    $env:GRADLE_USER_HOME = $gradleHome
    cmd /c $gradleCommand
} finally {
    Pop-Location -ErrorAction SilentlyContinue
    cmd /c "subst $DriveLetter`: /d" *> $null
}
