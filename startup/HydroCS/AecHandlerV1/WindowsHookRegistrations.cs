using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms.VisualStyles;

namespace AecHandlerV1
{
    #region FirstTry
    //static IntPtr HookCallback(int nCode, IntPtr wParam, IntPtr lParam)
    //{
    //    if (nCode >= 0 && wParam == (IntPtr)WM_KEYDOWN || wParam == (IntPtr)WM_SYSKEYDOWN)
    //    {
    //        int vkCode = Marshal.ReadInt32(lParam);
    //        var key = (Keys)vkCode;
    //        Console.WriteLine((Keys)vkCode);

    //        // Example: block the "A" key
    //        if ((Keys)vkCode == Keys.A)
    //        {
    //            return (IntPtr)1; // Block the key
    //        }
    //    }
    //    return CallNextHookEx(IntPtr.Zero, nCode, wParam, lParam);

    //    //IntPtr.Zero -> was originally set to the hookID but i didnt quiet get why i need another hook so i removed it and its still running as i expected
    //    //possibly i broke something but the add and remove still work fine and ig i understand it
    //    //but what i originally thought that the value would be still zero anyways since at the time of passing the delegate it would be zero - well that is not correct
    //    //it would be if i were to pass lets say a variable that is null and then set it -> because (ofc excluding the case where passed with ref) when passed without ref - the value that is passed would be null
    //    //and there for the pointer does not point to anything but since i pass an object that already has a value
    //    //actually just changed my mind when assiging the hookID the do not assing a value of the hookID object but we assing hookID variable with a new object
    //    //but our CallNextHookEx does not have the variable since i do not use ref so in that case i would actually have been right and it would be set to Zero
    //    //since even if changed hookID after assigning -> the function does not have the hookID variable but just a .Zero which well is a different struct / obj in that case
    //    //only if i were to either use ref or have a custom wrapper object that i pass around then both could access the same pointer / data / instance
    //}

    //[STAThread] //only required for when using windows as far as im concerned
    //would be required on main methods
    #endregion

    #region BlockInput usage i had in powershell
    //just found it while googeling i think
    //$code = @'
    //[DllImport("user32.dll")]
    //public static extern bool BlockInput(bool fBlockIt);
    //'@

    //$userInput = Add-Type -MemberDefinition $code -Name Blocker -Namespace UserInput -PassThru

    //# block user input
    //$null = $userInput::BlockInput($true)

    //Write-Warning "Your input has been disabled for 4 seconds..."
    //Start-Sleep -Seconds 4

    //# unblock user input
    //$null = $userInput::BlockInput($false)
    #endregion

    #region Pnp Device / Devcon
    //are two ways of complelty disabeling a device from your pc
    //sadly after many hours of trying i came to the conclusion that
    //Get-PnpDevice -Class Keyboard -InstanceId "HID\VID_03F0&PID_1024&MI_00\7&33D74131&0&0000" | Disable-PnpDevice -Confirm:$false
    //and
    //Get-PnpDevice -Class Keyboard -InstanceId "HID\VID_03F0&PID_1024&MI_00\7&33D74131&0&0000" | Enable-PnpDevice -Confirm:$false
    //do not work for all devices since some are marked as critical (keyboards are one of those) at least as of my observation - mouse was working fine
    //then after some more research i found a post stating that a restart applies this change - even if the execution of the command yields an error
    //and after trying that well it worked - but since i cannot reboot the computer every time i wanna deactivate the keyboard this solution does not come in handy
    //if i every try to replicate this - keep in mind that there are many placed in which a keyboard of your device can be found in the device manager
    //not only in the keyboards section but also in mouse and other inputs or however its called and even general usb or hud or sth like that
    //the tick i used was plug out plug in again and again until i tracked the correct instanceId then i got it to work but sadly - this doesnt help in my current use case
    #endregion

    //so there is this windows low level api called RawInput
    //but its really hard to use and ig not worth it at least not for today - im done

    //user32.dll definitions can be found here in case i need it in the future
    //https://learn.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-setwindowshookexa
    public class WindowsHookRegistrations
    {
        #region Helper Structs / Delegates
        public delegate IntPtr LowLevelHookProc(int nCode, IntPtr wParam, IntPtr lParam);

        [StructLayout(LayoutKind.Sequential)]
        private struct POINT
        {
            public int x;
            public int y;
        }

        [StructLayout(LayoutKind.Sequential)]
        private struct MSLLHOOKSTRUCT
        {
            public POINT pt;
            public uint mouseData;
            public uint flags;
            public uint time;
            public IntPtr dwExtraInfo;
        }
        #endregion

        #region Consts
        // Win32 Hook Ids (WM -> standing for Windows Message and LL for a certain windows even)
        private const int WH_KEYBOARD_LL = 13;
        private const int WH_MOUSE_LL = 14;

        // Win32 Button Ids
        private const int WM_KEYDOWN = 0x0100;
        private const int WM_SYSKEYDOWN = 0x0104;
        private const int WM_LBUTTONDOWN = 0x0201;
        private const int WM_RBUTTONDOWN = 0x0204;
        private const int WM_MOUSEMOVE = 0x0200;
        //also trigger on keyup btw - that is why i specify only to proceed on keydown codes - does also mean that on Guarding in only block down not up
        #endregion

        #region Default HookProcedures
        private static LowLevelHookProc DefaultKeyboardHookProcedure = (int nCode, IntPtr wParam, IntPtr lParam) =>
        {
            TimeOfLastInteraction = DateTime.UtcNow;

            if (GuardAll || GuardKeyboard)
                return (IntPtr)1;

            if (nCode < 0 || ((int)wParam != WM_KEYDOWN && (int)wParam != WM_SYSKEYDOWN))
                return CallNextHookEx(IntPtr.Zero, nCode, wParam, lParam);

            int vkCode = Marshal.ReadInt32(lParam);
            Keys key = (Keys)vkCode;

            if (RequiredKeySequence != null) //can only validate in here since lParam might be invalid if nCode is not fine
            {
                if (RequiredKeySequence.IsDone() || RequiredKeySequence.GetCurrentKey() != key)
                    return (IntPtr)1;

                RequiredKeySequence.Move();
                if (RequiredKeySequence.IsDone())
                {
                    RequiredKeySequence.CompletionSource.SetResult(true);
                }
            }

            Console.WriteLine("Key Pressed: " + key);

            return CallNextHookEx(IntPtr.Zero, nCode, wParam, lParam);
        };

        private static LowLevelHookProc DefaultMouseHookProcedure = (int nCode, IntPtr wParam, IntPtr lParam) =>
        {
            TimeOfLastInteraction = DateTime.UtcNow;

            if (GuardAll || GuardMouse || RequiredKeySequence != null)
                return (IntPtr)1;

            if (nCode < 0)
                return CallNextHookEx(IntPtr.Zero, nCode, wParam, lParam);

            MSLLHOOKSTRUCT hookStruct = Marshal.PtrToStructure<MSLLHOOKSTRUCT>(lParam);

            switch ((int)wParam)
            {
                case WM_LBUTTONDOWN:
                    Console.WriteLine("Left Button Down");
                    break;
                case WM_RBUTTONDOWN:
                    Console.WriteLine("Right Button Down");
                    break;
                case WM_MOUSEMOVE:
                    //Console.WriteLine($"Mouse Moved: {hookStruct.pt.x}, {hookStruct.pt.y}");
                    break;
            }

            return CallNextHookEx(IntPtr.Zero, nCode, wParam, lParam);
        };
        #endregion

        public static bool GuardAll { get; set; } = false;
        public static bool GuardMouse { get; set; } = false;
        public static bool GuardKeyboard { get; set; } = false;

        public static DateTime? TimeOfLastInteraction { get; set; } = null;
        public static RequiredKeySequence? RequiredKeySequence { get; set; } = null;

        //API
        public static List<nint> RegisterKeyboardAndMouse()
        {
            (int Hookid, LowLevelHookProc Procedure)[] procedureContexts = [
                (WH_KEYBOARD_LL, DefaultKeyboardHookProcedure),
                (WH_MOUSE_LL, DefaultMouseHookProcedure)
            ];

            return Register(procedureContexts);
        }

        public static List<nint> Register(params (int Hookid, LowLevelHookProc Procedure)[] procedureContexts)
        {
            List<nint> registeredIds = [];

            using (Process curProcess = Process.GetCurrentProcess())
            using (ProcessModule curModule = curProcess.MainModule!)
            {
                foreach(var procedureContext in procedureContexts)
                {
                    var curid = SetWindowsHookEx(procedureContext.Hookid, procedureContext.Procedure, GetModuleHandle(curModule.ModuleName), 0);
                    registeredIds.Add(curid);
                }                
            }

            return registeredIds;
        }
        public static void UnRegister(List<nint> registeredIds)
        {
            foreach (var id in registeredIds)
                UnhookWindowsHookEx(id);
        }

        //Internal
        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        private static extern IntPtr SetWindowsHookEx(int idHook, LowLevelHookProc lpfn, IntPtr hMod, uint dwThreadId);
        //keep in mind that im passing a pointer to the lpfn ofc -> which means that when Block variables change than the behaviour still changes
        //i do not pass data the way i do it with expressions -> nor do i catch any anonymous state -> here or every when using anonymous changes and even disposal can happen even after passing since everything is call by reference
        //i am btw pretty sure that its always like that (i only remember chatgpt once telling me different - not sure ig)

        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        private static extern bool UnhookWindowsHookEx(IntPtr hhk);

        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        private static extern IntPtr CallNextHookEx(IntPtr hhk, int nCode, IntPtr wParam, IntPtr lParam);

        [DllImport("kernel32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        private static extern IntPtr GetModuleHandle(string lpModuleName);
    }

    public class RequiredKeySequence
    {
        private int CurrentIndex { get; set; } = 0;
        private Keys[] Sequence { get; set; }
        public TaskCompletionSource<bool> CompletionSource { get; set; } = new();

        public RequiredKeySequence(Keys[] toExecute)
        {
            this.Sequence = toExecute;
        }

        public Keys GetCurrentKey()
        {
            return this.Sequence[CurrentIndex];
        }

        public void Move()
        {
            this.CurrentIndex++;
        }

        public bool IsDone()
        {
            return this.CurrentIndex >= this.Sequence.Length;
            //since i move and then check im only done when im already one bigger than the actual last index that is why i dont do -1
        }
    }
}