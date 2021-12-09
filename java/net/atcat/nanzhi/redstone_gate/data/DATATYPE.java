package net.atcat.nanzhi.redstone_gate.data;

/** 类型命名规则
 *  前4比特为类型，后4比特为序号或储值
 *  0000: 布尔,未知,比特地图
 *  0001: 数字
 *  0010: 字符串,JSON(NBT)
 * */

public enum DATATYPE {
    UNKNOWN( 0x00, -1 ), FALSE(0x01, 0 ), TRUE( 0x02, 0 ), BITMAP( 0x03, -1 ) ,
    SHORT( 0x10, 2 ), INT( 0x11, 4 ), FLOAT( 0x12, 4 ), LONG( 0x13, 8 ), DOUBLE( 0x14, 8 ),
    STR( 0x20, -1 ), NBT( 0x21, -1 ),
    ;

    private final byte val ;
    private final int size ;
    DATATYPE(int val, int size ) {
        this.val = (byte) val ;
        this.size = size ;
    }
    public byte value ( ) {
        return this.val ;
    }
    public int size ( ) {
        return this.size ;
    }
    public static DATATYPE valueOf (int val ) {
        switch ( val ) {
            case 0x01 : return DATATYPE.FALSE ;
            case 0x02 : return DATATYPE.TRUE ;
            case 0x03 : return DATATYPE.BITMAP ;
            case 0x10 : return DATATYPE.SHORT ;
            case 0x11 : return DATATYPE.INT ;
            case 0x12 : return DATATYPE.FLOAT ;
            case 0x13 : return DATATYPE.LONG ;
            case 0x14 : return DATATYPE.DOUBLE ;
            case 0x20 : return DATATYPE.STR ;
            case 0x21 : return DATATYPE.NBT ;
            default : return DATATYPE.UNKNOWN ;
        }
    }

    public static DATATYPE valueOf (byte val ) {
        return valueOf( val & 255 ) ;
    }

    public static byte valueOf ( DATATYPE type ) {
        return type.value( ) ;
    }

    public static int sizeOf ( DATATYPE type ) {
        return type.size( ) ;
    }

}
