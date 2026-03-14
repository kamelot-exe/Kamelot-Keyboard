param(
    [string]$ApkPath = (Join-Path $PSScriptRoot "..\app\build\outputs\apk\debugNoMinify\KamelotKeyboard_3.8-debugNoMinify.apk")
)

$ErrorActionPreference = "Stop"

function Get-AdbPath {
    $localProperties = Join-Path (Split-Path -Parent $PSScriptRoot) "local.properties"
    if (Test-Path $localProperties) {
        $sdkDirLine = Get-Content $localProperties | Where-Object { $_ -like "sdk.dir=*" } | Select-Object -First 1
        if ($sdkDirLine) {
            $sdkDir = $sdkDirLine.Substring("sdk.dir=".Length) -replace "\\:", ":" -replace "\\\\", "\"
            $adb = Join-Path $sdkDir "platform-tools\adb.exe"
            if (Test-Path $adb) {
                return $adb
            }
        }
    }
    if ($env:ANDROID_HOME) {
        $adb = Join-Path $env:ANDROID_HOME "platform-tools\adb.exe"
        if (Test-Path $adb) {
            return $adb
        }
    }
    throw "adb.exe not found. Configure local.properties or ANDROID_HOME first."
}

$resolvedApkPath = (Resolve-Path $ApkPath).Path
$adb = Get-AdbPath

& $adb start-server | Out-Host
$devices = & $adb devices
$deviceLines = $devices | Select-Object -Skip 1 | Where-Object { $_ -match "\S+\s+device$" }
if (-not $deviceLines) {
    throw "No authorized Android device detected. Enable USB debugging, connect the phone, and accept the RSA prompt."
}

& $adb install -r $resolvedApkPath
