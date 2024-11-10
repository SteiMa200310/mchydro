#https://stackoverflow.com/questions/3369662/can-you-remove-an-add-ed-type-in-powershell-again
#assemblies like add-type cannot be unloaded - only appdomains can be unloaded which requiers a restart

Add-Type @"
using System;
using System.Runtime.InteropServices;
public class UserActivity
{
    [StructLayout(LayoutKind.Sequential)]
    public struct LASTINPUTINFO
    {
        public uint cbSize;
        public uint dwTime;
    }

    [DllImport("user32.dll")]
    public static extern bool GetLastInputInfo(ref LASTINPUTINFO plii);

    public static uint GetIdleTime()
    {
        LASTINPUTINFO lastIn = new LASTINPUTINFO();
        lastIn.cbSize = (uint)Marshal.SizeOf(lastIn);
        GetLastInputInfo(ref lastIn);
        return ((uint)Environment.TickCount - lastIn.dwTime); // return idle time in seconds
    }
}
"@ -PassThru #passthru allows multi usgae thru application domains

# Function to check idle time
function Get-IdleTime {
    [UserActivity]::GetIdleTime()
}

# Load Windows Forms to use SendKeys
Add-Type -AssemblyName System.Windows.Forms

# Function to send keys using SendKeys method
function Send-Key {
    param (
        [string]$key
    )
    [System.Windows.Forms.SendKeys]::SendWait($key)
}

#Send-Key "{ESC}"

#%{TAB} -> is alt tab
#Send-Key "%{TAB}"

#Start-Sleep -Milliseconds 500

#Send-Key "{ENTER}"

#0 MC || 1 VLC
$applid = 0
$wasIdleTimeAbove3000AfterSwitchToVLC = $false

while($true) {
    # Get idle time
    $idleTime = Get-IdleTime
    Write-Host "Idle Time: $idleTime seconds"

    if (($idleTime -gt 5000) -and ($applid -eq 0)) {
        Send-Key "{ESC}"

        Start-Sleep -Milliseconds 5

        Send-Key "%{TAB}"

        Start-Sleep -Milliseconds 5

        Send-Key "{ENTER}"

        Start-Sleep -Milliseconds 1000

        Send-Key " "

        $applid = 1
        continue
    }

    if (($applid -eq 1) -and ($idleTime -lt 1000) -and $wasIdleTimeAbove3000AfterSwitchToVLC) {
        Send-Key " "

        Start-Sleep -Milliseconds 5

        Send-Key "%{TAB}"

        Start-Sleep -Milliseconds 5

        Send-Key "{ENTER}"

        Start-Sleep -Milliseconds 5

        Send-Key "{ESC}"

        $applid = 0
        $wasIdleTimeAbove3000AfterSwitchToVLC = $false
        continue
    }

    if (($applid -eq 1) -and ($idleTime -gt 3000)) {
        $wasIdleTimeAbove3000AfterSwitchToVLC = $true
    }

    Start-Sleep -Milliseconds 200
}