package ru.demkin.gd.services;

import com.sun.jna.Memory;
import ru.demkin.gd.utils.JNACore;

/**
 * Created by evgen1000end on 19.07.2015.
 */
public class AddressService {

    private int currentProcessAddress;

    public  boolean isDetectGame = false;


    private long skillDynamicAddress;
    private final long skillBaseAddress = 0x002291E0;
    private final int[] skillsOffsets = new int[]{0x68, 0x34C, 0xA74};

    private long attributeDynamicAddress;
    private final long attributeBaseAddress = 0x002291E0;
    private final int[] attributeOffsets = new int[]{0x68, 0x34C, 0xA70};

    private static JNACore jnaCore;

    public static void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }


    public AddressService(){

        jnaCore = JNACore.getInstance();
        jnaCore.LoadProcess("Grim Dawn");

        if (jnaCore.Pid ==0) {
            return;
        }

        isDetectGame = true;

        currentProcessAddress = jnaCore.getBaseAddress();

        skillDynamicAddress = jnaCore.findDynAddress2(skillsOffsets, currentProcessAddress + skillBaseAddress);
        attributeDynamicAddress =  jnaCore.findDynAddress2(attributeOffsets, currentProcessAddress+attributeBaseAddress);
    }

    public int getSkill(){
        Memory scoreMem = jnaCore.readMemory(jnaCore.process, skillDynamicAddress, 4);
        return scoreMem.getInt(0);
    }

    public void setSkill(byte[] value){
        reverse(value);
        jnaCore.writeMemory(jnaCore.process,skillDynamicAddress, value );
    }

    public int getAttribute(){
        Memory scoreMem = jnaCore.readMemory(jnaCore.process, attributeDynamicAddress, 4);
        return scoreMem.getInt(0);
    }

    public void setAttribute(byte[] value){
        reverse(value);
        jnaCore.writeMemory(jnaCore.process,attributeDynamicAddress, value );
    }


}
