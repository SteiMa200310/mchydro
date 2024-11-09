using AecHandlerV1;
using Microsoft.VisualBasic.Devices;
using System;
using System.Diagnostics;
using System.Runtime.InteropServices;
using System.Windows.Forms;

#region Send Key / Open Process Testing
//Get And Open Process
//Process.GetProcessById(id)
//var processPath = "C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\Notepad++.lnk";
//Process? process = Process.Start(new ProcessStartInfo(processPath) { UseShellExecute = true });
//Console.WriteLine(process!.Id);

//Send Keys
//System.Windows.Forms.SendKeys.SendWait("Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg\n");
//System.Windows.Forms.SendKeys.SendWait("Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg\n");
//System.Windows.Forms.SendKeys.SendWait("Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg\n");
//System.Windows.Forms.SendKeys.SendWait("Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg\n");
//System.Windows.Forms.SendKeys.SendWait("Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg\n");
//System.Windows.Forms.SendKeys.SendWait("Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg\n");
//System.Windows.Forms.SendKeys.SendWait("Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg\n");
//System.Windows.Forms.SendKeys.SendWait("Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg Hallo du kleiner neg\n");
//SendKeys.SendWait("HalloHalloHalloHalloHalloHallo");
//SendKeys.SendWait("HalloHalloHalloHalloHalloHallo");
//SendKeys.SendWait("HalloHalloHalloHalloHalloHallo");
//SendKeys.SendWait("HalloHalloHalloHalloHalloHallo");
//SendKeys.SendWait("HalloHalloHalloHalloHalloHallo");

//more testing
//Thread.Sleep(5000);
//await Task.Delay(2000);

//WindowsHookRegistrations.BlockAll = true;
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//SendKeys.SendWait("Hallo du hundling");
//when sending a key but currenlty blocked then the send wait does not conntinug because well its waiting - but the block then does not fully work since
//well for some reason i then can move my mosue but laggy and ig that is some type of security but ... idk
//btw i need to send enough keys in order to trigger the hook again and again that allowes me to move my mouse a bit otherwise its completly frozen
#endregion

//also note that launch settings are beeing added automatically when debug configs are changed
//can also just add them manually and think its really cool that this is working in casual .net core consol applications as well and not just in asp.net core web ones

#pragma warning disable CS4014 //i dont await the tasks since i queue them just as work items

try
{
    Console.WriteLine("Starting ....");

    //CloseAfterMilliSeconds(60_000);

    var registeredIds = WindowsHookRegistrations.RegisterKeyboardAndMouse();

    var vlcProcessId = int.Parse(args[0]);
    var mcProcessId = int.Parse(args[1]);
    var inactiveTimeThreshHoldInMs = int.Parse(args[2]);

    Task.Run(async () =>
    {
        //0 MC | 1 VLC
        int currentProcess = 0;
        
        TimeSpan idleTime = TimeSpan.Zero;
        while (true)
        {
            if (WindowsHookRegistrations.TimeOfLastInteraction == null)
                continue;

            var previousIdleTime = idleTime;
            idleTime = DateTime.UtcNow - (DateTime)WindowsHookRegistrations.TimeOfLastInteraction;

            if ((int)idleTime.TotalMilliseconds > inactiveTimeThreshHoldInMs && currentProcess == 0)
            {
                //SendKeys.SendWait(); //Send to switch from mc to vlc

                //does block
                WindowsHookRegistrations.KeyAllowence = new SerialKeyAllowence([
                    Keys.Escape, 
                    Keys.LMenu, 
                    Keys.Tab, 
                    Keys.Enter, 
                    Keys.LControlKey, 
                    Keys.Left, 
                    Keys.LControlKey, 
                    Keys.Left, 
                    Keys.LControlKey, 
                    Keys.Left,
                    Keys.Space]);

                SendKeys.SendWait("{ESC}");
                SendKeys.SendWait("%{TAB}");
                SendKeys.SendWait("{ENTER}");

                SendKeys.SendWait("^{LEFT}");
                SendKeys.SendWait("^{LEFT}");
                SendKeys.SendWait("^{LEFT}");

                SendKeys.SendWait(" ");

                while(WindowsHookRegistrations.KeyAllowence != null) { }
                //WindowsHookRegistrations.KeyAllowence = null;

                WindowsHookRegistrations.BlockAll = true;
                currentProcess = 1;
                idleTime = TimeSpan.Zero;
                continue;
            }

            if (previousIdleTime > idleTime && currentProcess == 1)
            {
                //SendKeys.SendWait(); //Send to switch from vlc to mc
                //await Task.Delay(1000); //incase hes hitting the keyboard hard i wait a bit to midigate many inputs at least a bit
                //the first send chars are ignored anyways but then when my loop comes across again and the time diff is smaller than before i have to unlock the keyboard otherwise i cannot send the buttons
                //sadly i cannot send such commands to the vlc or mc process
                //what i could try is just disabeling the keyboard maybe ill do that -> since currently my hook fires no matter if system or actual keystroke has been used

                WindowsHookRegistrations.KeyAllowence = new SerialKeyAllowence([
                    Keys.Space, 
                    Keys.LMenu, 
                    Keys.Tab, 
                    Keys.Enter, 
                    Keys.Escape]);
                WindowsHookRegistrations.BlockAll = false;

                SendKeys.SendWait(" ");
                SendKeys.SendWait("%{TAB}");
                SendKeys.SendWait("{ENTER}");
                SendKeys.SendWait("{ESC}");

                while (WindowsHookRegistrations.KeyAllowence != null) { }
                //WindowsHookRegistrations.KeyAllowence = null;

                currentProcess = 0;
                continue;
            }
        }
    }); //custom check loop

    Application.Run();
    //WindowsHookRegistrations.UnRegister(registeredIds);
}
catch (Exception ex)
{
    Console.WriteLine(ex.ToString());
    Environment.Exit(0);
}

void CloseAfterMilliSeconds(int milliseconds)
{
    Task.Run(async () =>
    {
        await Task.Delay(milliseconds);
        Environment.Exit(0);
    });
}