package ru.demkin.gd.utils;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.nio.ByteBuffer;


/**
 * Created by evgen1000end on 17.07.2015.
 */
public  class MemoryUtils  {

    static Kernel32 kernel32 = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
    static User32 user32 = (User32) Native.loadLibrary("user32", User32.class);
    static Psapi psapi = (Psapi) Native.loadLibrary("Psapi", Psapi.class);

    public static int PROCESS_VM_READ= 0x0010;
    public static int PROCESS_VM_WRITE = 0x0020;
    public static int PROCESS_VM_OPERATION = 0x0008;


    static {

    }

    public static Pointer openProcess(int permissions, int pid) {
        Pointer process = kernel32.OpenProcess(permissions, true, pid);
        return process;
    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    public static byte[] intToBytes(int i){
       return ByteBuffer.allocate(4).putInt(i).array();
    }

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    public static int getProcessId(String window) {
        IntByReference pid = new IntByReference(0);
        user32.GetWindowThreadProcessId(user32.FindWindowA(null, window), pid);

        return pid.getValue();
    }





    public static long findDynAddress(Pointer process, long[] offsets, long baseAddress)
    {

        long pointer = baseAddress;

        int size = 4;
        Memory pTemp = new Memory(size);
        long pointerAddress = 0;


      //  long pointer2 = process.getLong(0); //Long.toHexString(pointer);

       // pointer += pointer2;


        //22

       // long pointer2 = process.getNativeLong(0);

        kernel32.ReadProcessMemory(process, 0x229AA4, pTemp, size, null);


        long firstPointer = pTemp.getInt(0);

        String _hexFirstValue = Long.toHexString(firstPointer);


        for(int i = 0; i < offsets.length; i++)
        {
            if(i == 0)
            {
                kernel32.ReadProcessMemory(process, pointer, pTemp, size, null);

            }

            pointerAddress = ((pTemp.getInt(0)+offsets[i]));

            String _hexTemp = Long.toHexString(pointerAddress);


            if(i != offsets.length-1)
                kernel32.ReadProcessMemory(process, pointerAddress, pTemp, size, null);


        }

        return pointerAddress;
    }



    public static Memory readMemory(Pointer process, long address, int bytesToRead) {
        IntByReference read = new IntByReference(0);
        Memory output = new Memory(bytesToRead);

        kernel32.ReadProcessMemory(process, address, output, bytesToRead, read);
        return output;
    }

    public static void writeMemory(Pointer process, long address, byte[] data)
    {
        int size = data.length;
        Memory toWrite = new Memory(size);

        for(int i = 0; i < size; i++)
        {
            toWrite.setByte(i, data[i]);
        }

        boolean b = kernel32.WriteProcessMemory(process, address, toWrite, size, null);
    }


    public static void ChangeValue(String processName, long baseAddress, long[] offsets, byte[] newValue){
        int pid = getProcessId(processName);

        Pointer process = openProcess(PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_VM_OPERATION, pid);

       // String test = process.toString();

        long dynAddress = findDynAddress(process, offsets, baseAddress);


        final long tempAdress =0x1B2B7DC0;
        // Memory scoreMem = readMemory(process,dynAddress,4);
        Memory scoreMem = readMemory(process,tempAdress,4);

        int score = scoreMem.getInt(0);

        writeMemory(process, tempAdress, newValue);
    }


}
