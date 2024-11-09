using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace AecHandlerV1
{
    public class WindowManager
    {
        private const int SW_RESTORE = 9;

        [DllImport("user32.dll")]
        [return: MarshalAs(UnmanagedType.Bool)]
        private static extern bool SetForegroundWindow(IntPtr hWnd);

        [DllImport("user32.dll")]
        private static extern bool ShowWindow(IntPtr hWnd, int nCmdShow);

        public static void ActivateWindow(int processId)
        {
            Process process = Process.GetProcessById(processId);
            if (process == null || process.MainWindowHandle == IntPtr.Zero)
                throw new InvalidOperationException("ProcessId " + processId + " not found or invalid");

            ShowWindow(process.MainWindowHandle, SW_RESTORE);
            SetForegroundWindow(process.MainWindowHandle);
        }
    }
}