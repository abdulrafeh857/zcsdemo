package com.zcs.zcssdkdemo.utils;

public class StringUtils {

    /**
     * merge two byte arrays
     *
     * @param array1 first part
     * @param array2 last part
     * @return the concatenated array
     */
    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] concatArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, concatArray, 0, array1.length);
        System.arraycopy(array2, 0, concatArray, array1.length, array2.length);
        return concatArray;
    }
}
