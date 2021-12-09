package net.atcat.nanzhi.redstone_gate.data;

import net.minecraft.nbt.CompoundNBT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

// 你可以继承这个方法并改写或添加某些方法以达到你的目的，比如你可以改写errFunc
public class GeneralData extends Transform {

    protected static final Logger logger = LogManager.getLogger( ) ;

    protected byte[] data = null ;
    protected DATATYPE type = null ;
    public GeneralData( byte[] data ) {
        this.setData( data ) ;
    }

    public GeneralData( ) {
        this.setData( new byte[0] ) ;
    }

    protected void setData( byte[] data ) {
        this.data = data ;
        this.type = data.length == 0 ? DATATYPE.UNKNOWN : DATATYPE.valueOf( data[ 0 ] ) ;
    }

    public DATATYPE getType ( ) { return this.type ; }

    public byte[] getData ( ) { return this.data.clone( ) ; }

    protected <T> T errFunc ( T val, Exception err ) { // 有需要自行改写
        // 配置文件里搞个开关，遇到数据错误时的关注度：警告(简洁输出)，错误(详细输出)，致命(结束程序)
        // 如果配置文件需要崩溃，那么关闭客户端和服务器
        // 详细
        // logger.error( "Database: data is called incorrectly!", err ) ;
        // 简洁
        // logger.warn( "Database: data is called incorrectly!" ) ;
        // logger.warn( err ) ;
        // 致命
        logger.fatal( "Database: data is called incorrectly!\r\n------------ THIS IS NOT A BUG !!! ------------\r\nDo not submit the report, because the switch in the configuration file is turned on: the level at which data failed to getDirectionSwitch - fatal<2>.", err ) ;
        Runtime.getRuntime( ).exit( 1 ) ;
        return val ;
    }

    // 布尔值
    public GeneralData set ( boolean val ) {
        this.setData( bool2byte( val ) ) ;
        return this ;
    }

    public boolean getBool ( ) {
        return byte2bool( this.data, e -> errFunc( false, e ) ) ;
    }

    // 短数
    public GeneralData set ( short val ) {
        this.setData( num2byte( val ) ) ;
        return this ;
    }

    public short getShort ( ) {
        return byte2short( this.data, e -> errFunc( (short) 0, e ) ) ;
    }

    // 数
    public GeneralData set ( int val ) {
        this.setData( num2byte( val ) ) ;
        return this ;
    }

    public int getInt ( ) {
        return byte2int( this.data, e -> errFunc( 0, e ) ) ;
    }

    // 浮点
    public GeneralData set ( float val ) {
        this.setData( num2byte( val ) ) ;
        return this ;
    }

    public float getFloat ( ) {
        return byte2float( this.data, e -> errFunc( 0f, e ) ) ;
    }

    // 长数
    public GeneralData set ( long val ) {
        this.setData( num2byte( val ) ) ;
        return this ;
    }

    public long getLong ( ) {
        return byte2long( this.data, e -> errFunc( 0L, e ) ) ;
    }

    // 双精度
    public GeneralData set ( double val ) {
        this.setData( num2byte( val ) ) ;
        return this ;
    }

    public double getDouble ( ) {
        return byte2double( this.data, e -> errFunc( 0d, e ) ) ;
    }

    // 字符串
    public GeneralData set ( String val, Charset cs ) {
        this.setData( string2byte( val, cs ) ) ;
        return this ;
    }

    public GeneralData set ( String val ) {
        return this.set( val, StandardCharsets.UTF_8 ) ;
    }

    public String getString( Charset cs ) {
        return byte2string( this.data, e -> errFunc( "", e ) ) ;
    }
    public String getString ( ) {
        return this.getString( StandardCharsets.UTF_8 ) ;
    }

    // NBT
    public GeneralData set ( CompoundNBT val, Charset cs ) {
        this.setData( nbt2byte( val, cs ) ) ;
        return this ;
    }

    public GeneralData set ( CompoundNBT val ) {
        return this.set( val, StandardCharsets.UTF_8 ) ;
    }

    public CompoundNBT getNBT( Charset cs ) {
        return byte2nbt( this.data, e -> errFunc( new CompoundNBT( ), e ) ) ;
    }
    public CompoundNBT getNBT ( ) {
        return this.getNBT( StandardCharsets.UTF_8 ) ;
    }

    // BitMap
    public GeneralData set ( Unit.bitmap val ) {
        this.setData( bitmap2byte( val ) ) ;
        return this ;
    }

    public Unit.bitmap getBitMap ( ) {
        return byte2bitmap( this.data, e -> errFunc( new Unit.bitmap( 1 ), e ) ) ;
    }

}
