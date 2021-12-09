package net.atcat.nanzhi.redstone_gate.data;

import com.mojang.brigadier.StringReader;
import net.atcat.nanzhi.redstone_gate.data.transform.Byte2;
import net.atcat.nanzhi.redstone_gate.data.transform.Num2;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;

import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/** 被封装的转换方法，在比特转数值的时候可能会抛出错误，你也可以忽略( 你要是有绝对的信心 )
 *  该方法不是基础类型的转换，基础类型转换请查看<net.atcat.nanzhi.core.obj.ts.*>或看看类型自身有没有转换方法
 *  该方法转换得到的byte会包含一个头部信息，请参阅enum BYTETYPE
 * */

public class Transform {
    public static final Charset defCs = StandardCharsets.UTF_8 ;
    /**
     * @param origin: 数据
     * @param target: 目标的类型（需要的类型）
     * */
    private static boolean typeIs( byte[] origin, DATATYPE... target ) {
        DATATYPE type ;
        if ( origin.length > 0 && ( type = DATATYPE.valueOf( origin[0] ) ) != DATATYPE.UNKNOWN ) {
            for ( DATATYPE t : target ) {
                if ( type == t && ( t.size( ) == -1 || origin.length -1 == t.size( ) ) ) { // 数据类型匹配且长度匹配
                    return true ;
                }
            }
        }
        return false ;
    }

    // 私有的快速构建的方法
    private static byte[] dataArr( DATATYPE t, @Nullable byte[] c, int l ) {
        byte[] d ;
        l = t.size( ) != -1 ? t.size( ) : Math.max( -1, l ) ; // 优先让l等于数据长度，如果数据长度未定义，那么等于l自身
        d = new byte[l +1] ;
        if ( l >= 0 ) {
            d[0] = t.value( ) ;
            if ( l > 0 && c != null ) {
                System.arraycopy( c, 0, d, 1, l ) ;
            }
        }
        return d ;
    }

    private static byte[] dataArr( DATATYPE t, @Nullable byte[] c ) {
        return dataArr( t, c, c != null ? c.length : -1 ) ;
    }
    private static byte[] ofs1 ( byte[] origin ) {
        byte[] d = new byte[origin.length -1] ;
        System.arraycopy( origin, 1, d, 0, d.length );
        return d ;
    }

    private static <T> T errCall ( T defv, Exception err, @Nullable Function<Exception,T> func ) {
        return func == null ? defv : func.apply( err ) ;
    }

    private static <T> T errCall ( T defv, @Nullable Function<Exception,T> func ) {
        return errCall( defv, new Exception( "Data type is not a " + defv.getClass( ).getSimpleName( ) ), func ) ;
    }

    // 布尔
    public static byte[] bool2byte( boolean val ) {
        return dataArr( val ? DATATYPE.TRUE : DATATYPE.FALSE, null ) ;
    }

    public static boolean byte2bool( byte[] val, @Nullable Function<Exception,Boolean> err ) {
        return typeIs( val, DATATYPE.FALSE, DATATYPE.TRUE )
                ? DATATYPE.valueOf( val[0] ) == DATATYPE.TRUE
                : errCall( false, err ) ;
    }

    // 短数
    public static byte[] num2byte( short val ) {
        return dataArr( DATATYPE.SHORT, Num2.Byte( val ) ) ;
    }

    public static short byte2short ( byte[] val, @Nullable Function<Exception,Short> err ) {
        return typeIs( val, DATATYPE.SHORT )
                ? Byte2.Short( val, 1 )
                : errCall( (short) 0, err ) ;
    }

    // 数
    public static byte[] num2byte( int val ) {
        return dataArr( DATATYPE.INT, Num2.Byte( val ) ) ;
    }

    public static int byte2int ( byte[] val, @Nullable Function<Exception,Integer> err ) {
        return typeIs( val, DATATYPE.INT )
                ? Byte2.Int( val, 1 )
                : errCall( 0, err ) ;
    }

    // 浮点
    public static byte[] num2byte( float val ) {
        return dataArr( DATATYPE.FLOAT, Num2.Byte( val ) ) ;
    }

    public static float byte2float ( byte[] val, @Nullable Function<Exception,Float> err ) {
        return typeIs( val, DATATYPE.FLOAT )
                ? Byte2.Float( val, 1 )
                : errCall( 0f, err ) ;
    }

    // 长数
    public static byte[] num2byte( long val ) {
        return dataArr( DATATYPE.LONG, Num2.Byte( val ) ) ;
    }

    public static long byte2long ( byte[] val, @Nullable Function<Exception,Long> err ) {
        return typeIs( val, DATATYPE.LONG )
                ? Byte2.Long( val, 1 )
                : errCall(0L, err ) ;
    }

    // 双精度
    public static byte[] num2byte( double val ) {
        return dataArr( DATATYPE.DOUBLE, Num2.Byte( val ) ) ;
    }

    public static double byte2double ( byte[] val, @Nullable Function<Exception,Double> err ) {
        return typeIs( val, DATATYPE.DOUBLE )
                ? Byte2.Double( val, 1 )
                : errCall( 0d, err ) ;
    }

    // 字符串
    public static byte[] string2byte( String val, Charset cs ) {
        return dataArr( DATATYPE.STR, val.getBytes( cs ) ) ;
    }

    public static byte[] string2byte( String val ) {
        return string2byte( val, defCs ) ;
    }

    public static String byte2string ( byte[] val, Charset cs, @Nullable Function<Exception,String> err ) {
        if ( typeIs( val, DATATYPE.STR ) ) {
            byte[] sa = ofs1( val ) ;
            return new String( sa, cs ) ;
        }
        return errCall( "", err ) ;
    }

    public static String byte2string ( byte[] val, @Nullable Function<Exception,String> err ) {
        return byte2string( val, defCs, err ) ;
    }

    // NBT
    public static byte[] nbt2byte( CompoundNBT val, Charset cs ) {
        return dataArr( DATATYPE.NBT, val.toString( ).getBytes( cs ) ) ;
    }

    public static byte[] nbt2byte( CompoundNBT val ) {
        return nbt2byte( val, defCs ) ;
    }

    public static CompoundNBT byte2nbt ( byte[] val, Charset cs, @Nullable Function<Exception,CompoundNBT> err ) {
        if ( typeIs( val, DATATYPE.NBT ) ) {
            byte[] sa = ofs1( val ) ;
            try {
                return new JsonToNBT( new StringReader( new String( sa, cs ) ) ).readStruct( ) ;
            } catch ( Exception e ) {
                return errCall( new CompoundNBT( ), e, err ) ;
            }
        }
        return errCall( new CompoundNBT( ), err ) ;
    }

    public static CompoundNBT byte2nbt ( byte[] val, @Nullable Function<Exception,CompoundNBT> err ) {
        return byte2nbt( val, defCs, err ) ;
    }

    // 比特地图
    public static byte[] bitmap2byte( Unit.bitmap val ) {
        return dataArr( DATATYPE.BITMAP, val.getByte( ) ) ;
    }

    public static Unit.bitmap byte2bitmap ( byte[] val, @Nullable Function<Exception,Unit.bitmap> err ) {
        return typeIs( val, DATATYPE.BITMAP )
                ? new Unit.bitmap( ofs1( val ) )
                : errCall( new Unit.bitmap( 1 ), err ) ;
    }

}
