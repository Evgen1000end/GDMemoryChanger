package ru.demkin.gd.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.util.List;

/**
 * Created by evgen1000end on 18.07.2015.
 */
public interface Psapi extends StdCallLibrary{

    Psapi INSTANCE = (Psapi) Native.loadLibrary("Psapi", Psapi.class);

    boolean EnumProcesses(int[] pProcessIdsReturned, int size, int[] pBytesReturned);

    boolean EnumProcessModules(Pointer hProcess, Pointer[] lphModule,int cb, IntByReference lpcbNeededs);

    int GetModuleBaseNameW(Pointer hProcess, Pointer hModule, byte[] lpBaseName, int nSize);

    boolean GetModuleInformation(Pointer hProcess, Pointer hModule, luz.dsexplorer.winapi.jna.Psapi.LPMODULEINFO lpmodinfo, int cb);
}
