package net.atcat.nanzhi.redstone_gate.data.transform;

// 转换之前自己校验下
public class Byte2 {
    // 短数
    public static short Short( byte[] bytes, int offset ) {
        return (short) ( ( bytes[offset] & 0xff ) << 8 | ( 0xff & bytes[offset +1] ) ) ;
    }
    public static short Short( byte[] bytes ) {
        return Short( bytes, 0 ) ;
    }
    // 数
    public static int Int( byte[] bytes, int offset ) {
        int val = 0 ;
        for ( int i = 0 ; i < 4 ; i++ ) {
            val |= ( bytes[i +offset] & 0xff ) << ( ( 3 -i ) << 3 ) ;
        }
        return val ;
    }

    public static int Int( byte[] bytes ) {
        return Int( bytes, 0 ) ;
    }

    // 浮点
    public static float Float( byte[] bytes, int offset ) {
        return Float.intBitsToFloat( Int( bytes, offset ) ) ;
    }

    public static float Float( byte[] bytes ) {
        return Float( bytes, 0 ) ;
    }

    // 长数
    public static long Long( byte[] bytes, int offset ) {
        long ret = 0 ;
        for ( int i = 0 ; i < 8 ; i ++ ) {
            ret |= ( (long) ( bytes[i +offset] & 255 ) ) << ( ( 7 -i ) << 3 ) ;
        }
        return ret ;
    }

    public static long Long( byte[] bytes ) {
        return Long( bytes, 0 ) ;
    }

    // 双精度
    public static double Double( byte[] bytes, int offset ) {
        return Double.longBitsToDouble( Long( bytes, offset ) ) ;
    }
    public static double Double( byte[] bytes ) {
        return Double( bytes, 0 ) ;
    }
}
