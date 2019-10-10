package org.pp.util;

public class ByteUtil {


    public static byte[] hexStr2Byte(String src) {
        char[] cs = src.toCharArray();
        //int size = ((cs.length % 2==1)) ? (cs.length >> 1) + 1 : cs.length >> 1;
        int size = cs.length >> 1;
        if (cs.length % 2 == 1) size++;

        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            int o = i << 1;
            int p = (i << 1) + 1;
            byte u1 = MsgCost.map[cs[o]];
            if (cs.length > p) {
                byte u2 = MsgCost.map[cs[p]];
                bytes[i] = (byte) (((u1 << 4) + u2) & 0xff);
            } else {
                bytes[i] = (byte) ((u1 << 4) & 0xff);
            }
        }
        return bytes;
    }

    public static Integer getIntByPos(byte[] bytes, int pos) {
//        byte[] dest = new byte[4];
//        System.arraycopy(bytes, pos * 4, dest, 0, 4);
//        return byte2Int(dest);

        if (bytes.length < 4 + pos)
            return null;
        byte[] dest = {bytes[pos], bytes[pos + 1], bytes[pos + 2], bytes[pos + 3]};
        return byte2Int(dest);
    }

    public static Short getShortByPos(byte[] bytes, int pos) {
        if (bytes.length < 2 + pos)
            return null;
        byte[] dest = {bytes[pos], bytes[pos + 1]};
        return byte2Short(dest);
    }

    public static Long getLongByPos(byte[] bytes, int pos) {
        if (bytes.length < 4 + pos)
            return null;
        byte[] dest = {bytes[pos], bytes[pos + 1], bytes[pos + 2], bytes[pos + 3],
                bytes[pos + 4], bytes[pos + 5], bytes[pos + 6], bytes[pos + 7]
        };
        return byte2Long(dest);
    }

    public static byte[] getOtherBytes(byte[] target, int after) {
        byte[] tmp = new byte[target.length - after];
        System.arraycopy(target, after, tmp, 0, tmp.length);
        return tmp;
    }

    public static byte[] groupByte(byte[]... bytes) {
        int allSize = 0;
        for (byte[] aByte : bytes) {
            allSize += aByte.length;
        }
        byte[] tmp = new byte[allSize];

        int desPos = 0;
        for (int i = 0; i < bytes.length; i++) {
            byte[] t = bytes[i];
            System.arraycopy(t, 0, tmp, desPos, t.length);
            desPos += t.length;
        }
        return tmp;
    }


    public static int byte2Int(byte[] b) {
        if (b.length == 0) {
            throw new IllegalArgumentException("直接数组不合法");
        }
        int intValue = 0;
        for (byte i = 0; i < 4; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }

    public static byte[] int2Byte(int intValue) {
        byte[] b = {0, 0, 0, 0};
        for (byte i = 0; i < 4; i++) {
            b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
        }
        return b;
    }

    public static short byte2Short(byte[] b) {
        short shortVal = 0;
        for (byte i = 0; i < 2; i++) {
            shortVal += (b[i] & 0xFF) << (8 * (1 - i));
        }
        return shortVal;
    }

    public static byte[] short2Byte(short shortVal) {
        byte[] b = {0, 0};
        /*
        * b[0] = (byte) (a >> 8);
        * b[1] = (byte) (a);
        */
        for (byte i = 0; i < 2; i++) {
            b[i] = (byte) (shortVal >> 8 * (1 - i) & 0xFF);
        }
        return b;
    }

    public static void main(String[] args) {
        byte[] vv = short2Byte((short) 33100);
        short s = byte2Short(vv);
        System.out.println(s);
    }


    //------------------------------------
    /*public static long byte2Long(byte[] bts) {
        if(bts.length!=8) return 0;
        ChannelBuffer buffer = ChannelBuffers.buffer(8);
        buffer.writeBytes(bts);
        return buffer.readLong();
    }
    public static byte[] long2Byte(long value){
        ChannelBuffer buffer = ChannelBuffers.buffer(8);
        buffer.writeLong(value);
        return buffer.array();
    }*/

    public static long byte2Long(byte[] b) {
        //if(b.length!=8) return 0;
        long longValue = 0;
        for (byte i = 0; i < 8; i++) {
            longValue += (b[i] & 0xFFL) << (8 * (7 - i));
        }
        return longValue;
    }

    public static byte[] long2Byte(long intValue) {
        byte[] b = {0, 0, 0, 0, 0, 0, 0, 0};
        for (byte i = 0; i < 8; i++) {
            b[i] = (byte) (intValue >> 8 * (7 - i) & 0xFF);
        }
        return b;
    }


    public static byte[] encryption(byte[] bts) {
        for (int i = 0; i < bts.length; i++) {
            char key = MsgCost.keys[i % MsgCost.keys.length];
            bts[i] = (byte) (bts[i] ^ key);
        }
        return bts;
    }

    public static byte[] decryption(byte[] bts) {
        for (int i = 0; i < bts.length; i++) {
            char key = MsgCost.keys[i % MsgCost.keys.length];
            bts[i] = (byte) (bts[i] ^ key);
        }
        return bts;
    }








    /*public static void main(String[] args) {
        String s = "个人官网而过";
        byte[] b = encode(s.getBytes());
        String v = new String(b);
        System.out.println("v = " + v);
        b = decode(b);
        v = new String(b);
        System.out.println("v = " + v);
    }*/
    /*public static void main(String[] args) {
        byte[] p = {1,2,3,4};
        byte[] p2 = {5,6,7,8};
        byte[] cs = groupByte(p, p2);
        System.out.println("cs = " + cs);
    }*/

//    public static void main(String[] args) {
//        StringBuffer str = new
//
//
//
// StringBuffer();
//        for (int i=0;i<1;i++){
//            str.append("破开发票未付款哦");
//            str.append("个人热管");
//        }
//        byte[] ss = str.toString().getBytes();
//        long l = System.nanoTime();
//        byte[] a = getOtherBody(ss, 4);
//        l-=System.nanoTime();
//        System.out.println("l = " + l);
//
//
//        l = System.nanoTime();
//        int c = getIntByBytesPos(ss, 4);
//        l-=System.nanoTime();
//        System.out.println("l = " + l);
//    }

}
