package org.pp.util;

public class MsgCost {

    public static final int udp_port = 5888;

    public static final Byte map[] = new Byte['f' + 1];

    static {
        for (byte i = '0', j = 0; i <= '9'; i++, j++) {
            map[i] = j;
        }
        for (byte i = 'a', j = 10; i <= 'f'; i++, j++) {//10~15
            map[i] = j;
        }
        for (byte i = 'A', j = 10; i <= 'F'; i++, j++) {//10~15
            map[i] = j;
        }
    }

    public static final String[] hexs = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    public static final char[] keys = {
            0xcfef,
            0xccdf,
            0x936f,
            0x377f,
            0x3eed,
            0xe8fd
    };
}
