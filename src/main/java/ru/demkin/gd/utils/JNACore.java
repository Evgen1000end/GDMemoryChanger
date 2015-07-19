package ru.demkin.gd.utils;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JNACore {

    private static JNACore instance = null;


    static Kernel32 kernel32 = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
    static User32 user32 = (User32) Native.loadLibrary("user32", User32.class);

    public final int PROCESS_QUERY_INFORMATION = 0x0400;
    public final int PROCESS_VM_READ = 0x0010;
    public final int PROCESS_VM_WRITE = 0x0020;
    public final int PROCESS_VM_OPERATION = 0x0008;
    public final int PROCESS_ALL_ACCESS = 0x001F0FFF;

    /*
     Zezenia client objects
     */
    public int zezeniaPID;
    public Pointer zezeniaProcessHandle;
    private int[] processList = new int[512];
    private int[] dummyList = new int[512];
   // private Memory pTemp = new Memory(8);
   // private Memory toWrite = new Memory(1);
    private IntByReference bytesReturned = new IntByReference();

    /*
     JNACore Constructor
     */
    private JNACore() {
    }

    public static JNACore getInstance() {
        if (instance == null) {
            instance = new JNACore();
        }
        return instance;
    }

    public static int getProcessId(String window) {
        IntByReference pid = new IntByReference(0);
        user32.GetWindowThreadProcessId(user32.FindWindowA(null, window), pid);

        return pid.getValue();
    }

    public Pointer LoadProcess(String processName){
        zezeniaPID = getProcessId(processName);

       // zezeniaProcessHandle = openProcess(PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_VM_OPERATION, zezeniaPID);

        zezeniaProcessHandle = Kernel32.INSTANCE.OpenProcess(PROCESS_ALL_ACCESS, false, zezeniaPID);

        return zezeniaProcessHandle;
    }

    public static Pointer openProcess(int permissions, int pid) {
        Pointer process = kernel32.OpenProcess(permissions, true, pid);
        return process;
    }



    public void getFirstProcesses(String processName) {
        Psapi.INSTANCE.EnumProcesses(processList, 1024, dummyList);
        int pid;
        int i = 0;
        while (i < processList.length) {
            pid = processList[i];
            if (pid != 0) {
                Pointer ph = Kernel32.INSTANCE.OpenProcess(PROCESS_ALL_ACCESS, false, pid);
                if (ph != null) {
                    byte[] filename = new byte[512];
                    Psapi.INSTANCE.GetModuleBaseNameW(ph, new Pointer(0), filename, 512);
                    String test = new String(filename, StandardCharsets.ISO_8859_1);

                      if (test.equals(processName)){
                          zezeniaPID = pid;
//                        zezeniaProcessHandle = ph;
//                        return;
                    }


//                    if (test.contains(processName)) {
//                        zezeniaPID = pid;
//                        zezeniaProcessHandle = ph;
//                        return;
//                    }
                    Kernel32.INSTANCE.CloseHandle(ph);
                }
            }
            i++;
        }
    }

    /*
     Returns a pointer to a process given by a pid.
     */
    public Pointer returnProcess(int pid) {
        Pointer process = Kernel32.INSTANCE.OpenProcess(PROCESS_ALL_ACCESS, false, pid);
        return process;
    }

    public Memory readMemory(Pointer process, long address, int bytesToRead) {
        IntByReference read = new IntByReference(0);
        Memory output = new Memory(bytesToRead);

        boolean ReadProcessMemory = Kernel32.INSTANCE.ReadProcessMemory(process, address, output, bytesToRead, read);
        return output;
    }

    public void writeMemory(Pointer process, long address, byte[] data) {
        int size = data.length;

        Memory toWrite = new Memory(size);

        //i have toWrite size set to 1 byte. if i need to write more than 1 in
        //the future, i will have to change this code.
        for (int i = 0; i < size; i++) {
            toWrite.setByte(i, data[i]);
        }
        IntByReference x = new IntByReference();
        Kernel32.INSTANCE.WriteProcessMemory(process, address, toWrite, size, x);
        if (x.getValue() < 4) {
        }
    }


    /*
     Returns address at the end of a given array of offsets using the base address.

     Use Example - readMemory(zezeniaPointer,findDynAddress(zezeniaPointer,xCoord,baseAddress),4)
     -will read the players xCoordinate from the zezenia client.
     */
    public long findDynAddress(int[] offsets, long baseAddress) {
        long address = baseAddress;
        long pointerAddress = 0;

        int size = 4;
        Memory pTemp = new Memory(size);


        address = address + offsets[0];
        int i = 1;
        while (i < offsets.length) {
            if (i == 1) {
                boolean ReadProcessMemory = Kernel32.INSTANCE.ReadProcessMemory(zezeniaProcessHandle, address, pTemp, 4, bytesReturned);
            }
            pointerAddress = ((pTemp.getInt(0) + offsets[i]));
            if (i != offsets.length - 1) {
                boolean ReadProcessMemory = Kernel32.INSTANCE.ReadProcessMemory(zezeniaProcessHandle, pointerAddress, pTemp, 4, bytesReturned);
            }
            i++;
            //if pTempt returns 0, that means the value in memory isnt occupied yet
            if (pTemp.getInt(0) == 0) {
                return 0;
            }
        }
        return pointerAddress;
    }

    public  long findDynAddress2( int[] offsets, long baseAddress)
    {

        long pointer = baseAddress;

        int size = 4;
        Memory pTemp = new Memory(size);
        long pointerAddress = 0;

        kernel32.ReadProcessMemory(zezeniaProcessHandle, baseAddress, pTemp, size, null);

        long firstPointer = pTemp.getInt(0);

        String _hexFirstValue = Long.toHexString(firstPointer);


        for(int i = 0; i < offsets.length; i++)
        {
            if(i == 0)
            {
                kernel32.ReadProcessMemory(zezeniaProcessHandle, pointer, pTemp, size, null);

            }

            pointerAddress = ((pTemp.getInt(0)+offsets[i]));

            String _hexTemp = Long.toHexString(pointerAddress);


            if(i != offsets.length-1)
                kernel32.ReadProcessMemory(zezeniaProcessHandle, pointerAddress, pTemp, size, null);


        }

        return pointerAddress;
    }

    public int getBaseAddress() {
        try {
            Pointer hProcess = zezeniaProcessHandle;  // LoadProcess("Grim Dawn");

            List<Module> hModules = PsapiTools.getInstance().EnumProcessModules(hProcess);

            for (   Module m : hModules) {

                if (m.getBaseName().equals("grim dawn.exe")) {

                    System.out.println((m.getBaseName() + ": entry point at - 0x" + Long.toHexString(Pointer.nativeValue(m.getEntryPoint()))));
                    System.out.println("Base of dll : " + m.getLpBaseOfDll());
                    System.out.println(Integer.valueOf("" + Pointer.nativeValue(m.getLpBaseOfDll())));

//                  return Long.valueOf("" + Pointer.nativeValue(m.getLpBaseOfDll()));
                    return Integer.valueOf("" +    Pointer.nativeValue(m.getEntryPoint()));
                }

            }
        } catch (Exception e) {
            System.err.println("Something broke in getbaseaddress method");
            return -1;
        }
        return 0;
    }
}