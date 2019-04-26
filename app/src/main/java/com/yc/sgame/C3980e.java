package com.yc.sgame;


import java.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class C3980e {
    /* renamed from: a */
    public static int m14130a(byte b) {
        return (b + 256) % 256;
    }

    /* renamed from: a */
    public static String m14131a(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
            for (byte b : digest) {
                String toHexString = Integer.toHexString(b & 255);
                if (toHexString.length() == 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("0");
                    stringBuilder.append(toHexString);
                    toHexString = stringBuilder.toString();
                }
                stringBuffer.append(toHexString);
            }
        } catch (NoSuchAlgorithmException unused) {

        }
        return stringBuffer.toString();
    }

    /* renamed from: a */
    public static String m14132a(String str, int i) {
        return C3980e.m14133a(str, i, str.length());
    }

    /* renamed from: a */
    public static String m14133a(String str, int i, int i2) {
        int i3 = 0;
        if (i >= 0) {
            if (i2 < 0) {
                i2 *= -1;
                int i4 = i - i2;
                if (i4 < 0) {
                    i2 = i;
                } else {
                    i3 = i4;
                }
            } else {
                i3 = i;
            }
            if (i3 > str.length()) {
                return "";
            }
        } else if (i2 < 0) {
            return "";
        } else {
            i2 += i;
            if (i2 <= 0) {
                return "";
            }
        }
        if (str.length() - i3 < i2) {
            i2 = str.length() - i3;
        }
        return str.substring(i3, i2 + i3);
    }

    /* renamed from: a */
    public static String m14134a(String str, String str2) {
        return C3980e.m14135a(str, str2);
    }

    /* renamed from: a */
    private static String m14135a(String str, String str2 ) {
        if (str == null || str2 == null) {
            return "";
        }
        try {
            str2 = C3980e.m14131a(str2);
            String a = C3980e.m14131a(C3980e.m14133a(str2, 0, 16));
            str2 = C3980e.m14131a(C3980e.m14133a(str2, 16, 16));
            String a2 = C3980e.m14133a(str, 0, 5);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(a);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(a);
            stringBuilder2.append(a2);
            stringBuilder.append(C3980e.m14131a(stringBuilder2.toString()));
            a = stringBuilder.toString();
            StringBuilder stringBuilder3;
            a2 = new String(C3980e.m14137a(Base64.getDecoder().decode(C3980e.m14132a(str, 5).getBytes("UTF-8")), a), "UTF-8");
            String a3 = C3980e.m14133a(a2, 10, 16);
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(C3980e.m14132a(a2, 26));
            stringBuilder4.append(str2);
            if (a3.equals(C3980e.m14133a(C3980e.m14131a(stringBuilder4.toString()), 0, 16))) {
                return C3980e.m14132a(a2, 26);
            }
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append(str);
            stringBuilder5.append("=");
            a3 = new String(C3980e.m14137a(Base64.getDecoder().decode(C3980e.m14132a(stringBuilder5.toString(), 5).getBytes("UTF-8")), a), "UTF-8");
            a2 = C3980e.m14133a(a3, 10, 16);
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append(C3980e.m14132a(a3, 26));
            stringBuilder4.append(str2);
            if (a2.equals(C3980e.m14133a(C3980e.m14131a(stringBuilder4.toString()), 0, 16))) {
                return C3980e.m14132a(a3, 26);
            }
            stringBuilder5 = new StringBuilder();
            stringBuilder5.append(str);
            stringBuilder5.append("==");
            String str3 = new String(C3980e.m14137a(Base64.getDecoder().decode(C3980e.m14132a(stringBuilder5.toString(), 5).getBytes("UTF-8")), a), "UTF-8");
            str = C3980e.m14133a(str3, 10, 16);
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(C3980e.m14132a(str3, 26));
            stringBuilder3.append(str2);
            return str.equals(C3980e.m14133a(C3980e.m14131a(stringBuilder3.toString()), 0, 16)) ? C3980e.m14132a(str3, 26) : "2";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* renamed from: a */
    private static byte[] m14136a(byte[] bArr, int i) {
        int i2;
        byte[] bArr2 = new byte[i];
        for (i2 = 0; i2 < i; i2++) {
            bArr2[i2] = (byte) i2;
        }
        i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            i2 = ((i2 + ((bArr2[i3] + 256) % 256)) + bArr[i3 % bArr.length]) % i;
            byte b = bArr2[i3];
            bArr2[i3] = bArr2[i2];
            bArr2[i2] = b;
        }
        return bArr2;
    }

    /* renamed from: a */
    private static byte[] m14137a(byte[] bArr, String str) {
        if (bArr == null || str == null) {
            return null;
        }
        byte[] bArr2 = new byte[bArr.length];
        byte[] a = C3980e.m14136a(str.getBytes(), 256);
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            i = (i + 1) % a.length;
            i2 = (i2 + ((a[i] + 256) % 256)) % a.length;
            byte b = a[i];
            a[i] = a[i2];
            a[i2] = b;
            bArr2[i3] = (byte) (bArr[i3] ^ C3980e.m14130a(a[(C3980e.m14130a(a[i]) + C3980e.m14130a(a[i2])) % a.length]));
        }
        return bArr2;
    }
}