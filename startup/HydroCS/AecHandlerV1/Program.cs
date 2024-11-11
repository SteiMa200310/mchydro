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

//SendKeys.SendWait(); //Send to switch from vlc to mc
//await Task.Delay(1000); //incase hes hitting the keyboard hard i wait a bit to midigate many inputs at least a bit
//the first send chars are ignored anyways but then when my loop comes across again and the time diff is smaller than before i have to unlock the keyboard otherwise i cannot send the buttons
//sadly i cannot send such commands to the vlc or mc process
//what i could try is just disabeling the keyboard maybe ill do that -> since currently my hook fires no matter if system or actual keystroke has been used
#endregion

//also note that launch settings are beeing added automatically when debug configs are changed
//can also just add them manually and think its really cool that this is working in casual .net core consol applications as well and not just in asp.net core web ones

#pragma warning disable CS4014 //i dont await the tasks since i queue them just as work items
try
{
    Console.WriteLine("Starting ...");

    //CloseAfterMilliSeconds(20_000);

    var registeredIds = WindowsHookRegistrations.RegisterKeyboardAndMouse();

    var mcProcessId = int.Parse(args[0]);
    var vlcProcessId = int.Parse(args[1]);
    var inactiveTimeThreshHoldInMs = int.Parse(args[2]);

    Task.Run(async () =>
    {
        CurrentProcess currentProcess = CurrentProcess.Minecraft;
        TimeSpan idleTime = TimeSpan.Zero;

        while (true)
        {
            if (WindowsHookRegistrations.TimeOfLastInteraction == null)
                continue;

            var previousIdleTime = idleTime;
            idleTime = DateTime.UtcNow - (DateTime)WindowsHookRegistrations.TimeOfLastInteraction;

            if ((int)idleTime.TotalMilliseconds > inactiveTimeThreshHoldInMs && currentProcess == CurrentProcess.Minecraft)
            {
                await HandleMcToVlc(mcProcessId, vlcProcessId);
                currentProcess = CurrentProcess.Vlc;
                idleTime = TimeSpan.Zero; //indirectly reset previous to zero
                continue;
            }

            if (previousIdleTime > idleTime && currentProcess == CurrentProcess.Vlc)
            {
                await HandleVlcToMc(mcProcessId, vlcProcessId);
                currentProcess = CurrentProcess.Minecraft;
                continue;
            }
        }
    }); //custom check loop

    Application.Run();
    WindowsHookRegistrations.UnRegister(registeredIds);
}
catch (Exception ex)
{
    Console.WriteLine(ex.ToString());
    Environment.Exit(0);
}

async Task HandleMcToVlc(int mcProcessId, int vlcProcessId)
{
    WindowManager.ActivateWindow(mcProcessId);
    await Task.Delay(50);

    WindowsHookRegistrations.RequiredKeySequence = new RequiredKeySequence([Keys.Escape]);
    SendKeys.SendWait("{ESC}");
    await Task.Delay(50);
    await WindowsHookRegistrations.RequiredKeySequence.CompletionSource.Task;

    WindowManager.ActivateWindow(vlcProcessId);
    await Task.Delay(50);

    WindowsHookRegistrations.RequiredKeySequence = new RequiredKeySequence([Keys.LControlKey,
        Keys.Left,
        Keys.LControlKey,
        Keys.Left,
        Keys.LControlKey,
        Keys.Left,
        Keys.Space]);
    SendKeys.SendWait("^{LEFT}");
    await Task.Delay(50);
    SendKeys.SendWait("^{LEFT}");
    await Task.Delay(50);
    SendKeys.SendWait("^{LEFT}");
    await Task.Delay(50);
    SendKeys.SendWait(" ");
    await Task.Delay(50);
    await WindowsHookRegistrations.RequiredKeySequence.CompletionSource.Task;

    WindowsHookRegistrations.GuardAll = true; //additional turn on here
    WindowsHookRegistrations.RequiredKeySequence = null;
}

async Task HandleVlcToMc(int mcProcessId, int vlcProcessId)
{
    WindowManager.ActivateWindow(vlcProcessId);
    await Task.Delay(50);

    WindowsHookRegistrations.RequiredKeySequence = new RequiredKeySequence([Keys.Space]);
    WindowsHookRegistrations.GuardAll = false; //additional turn of here
    SendKeys.SendWait(" ");
    await Task.Delay(50);
    await WindowsHookRegistrations.RequiredKeySequence.CompletionSource.Task;

    WindowManager.ActivateWindow(mcProcessId);
    await Task.Delay(50);

    WindowsHookRegistrations.RequiredKeySequence = new RequiredKeySequence([Keys.Escape]);
    SendKeys.SendWait("{ESC}");
    await Task.Delay(50);
    await WindowsHookRegistrations.RequiredKeySequence.CompletionSource.Task;

    WindowsHookRegistrations.RequiredKeySequence = new RequiredKeySequence([
        Keys.Enter,
        Keys.LShiftKey,
        Keys.D7,
        Keys.R,
        Keys.E,
        Keys.S,
        Keys.E,
        Keys.T,
        Keys.Enter]);
    SendKeys.SendWait("{ENTER}");
    await Task.Delay(50);
    SendKeys.SendWait("/");
    await Task.Delay(50);
    SendKeys.SendWait("r");
    await Task.Delay(50);
    SendKeys.SendWait("e");
    await Task.Delay(50);
    SendKeys.SendWait("s");
    await Task.Delay(50);
    SendKeys.SendWait("e");
    await Task.Delay(50);
    SendKeys.SendWait("t");
    await Task.Delay(50);
    SendKeys.SendWait("{ENTER}");
    await Task.Delay(50);
    await WindowsHookRegistrations.RequiredKeySequence.CompletionSource.Task;

    WindowsHookRegistrations.RequiredKeySequence = null;
}

void CloseAfterMilliSeconds(int milliseconds)
{
    Task.Run(async () =>
    {
        await Task.Delay(milliseconds);
        Environment.Exit(0);
    });
}

enum CurrentProcess
{
    Minecraft,
    Vlc
}