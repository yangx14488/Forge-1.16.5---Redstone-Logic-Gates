package net.atcat.nanzhi.redstone_gate.data.transform;

public class Num2 {
    // 短数
    public static byte[] Byte( short val ) {
        byte[] b = new byte[2] ;
        b[0] = (byte) ( val >> 8 ) ;
        b[1] = (byte) ( val & 0xff ) ;
        return b ;
    }
    // 数
    public static byte[] Byte( int val ) {
        byte[] ret = new byte[ 4 ] ;
        ret[0] = (byte) ( ( val >> 24 ) & 0xff ) ;
        ret[1] = (byte) ( ( val >> 16 ) & 0xff ) ;
        ret[2] = (byte) ( ( val >> 8 ) & 0xff ) ;
        ret[3] = (byte) ( val & 0xff ) ;
        return ret ;
    }

    // 浮点
    public static byte[] Byte( float val ) {
        int bytes = Float.floatToIntBits( val ) ;
        return Byte( bytes ) ;
    }

    // 长数
    public static byte[] Byte( long val ) {
        byte[] ret = new byte[ 8 ] ;
        for ( int i = 0 ; i < 8 ; i ++ ) {
            ret[i] = (byte) ( 0xff & ( val >> ( ( 7 -i ) << 3 ) ) ) ;
        }
        return ret ;
    }

    // 双精度
    public static byte[] Byte( double val ) {
        long longbits = Double.doubleToLongBits( val );
        return Byte( longbits ) ;
    }

}
