package net.atcat.nanzhi.redstone_gate.data;

import net.atcat.nanzhi.redstone_gate.data.transform.Byte2;
import net.atcat.nanzhi.redstone_gate.data.transform.Num2;

public class Unit {
    public static class bitmap {
        public final int size ;
        protected byte[] map ; // 前4位存储了长度信息，最低长度5
        public bitmap ( int size ) {
            this.size = Math.max( size, 1 ) ; // 尺寸设置
            this.map = new byte[( ( size -1 ) >>> 3 ) +5] ; // 数组初始化
            System.arraycopy( Num2.Byte( this.size ), 0, this.map, 0, 4 ) ; // 长度写入
        }

        public bitmap ( byte[] byteArr ) {
            this.size = Byte2.Int( byteArr ) ; // 尺寸设置
            this.map = byteArr.clone( ) ; // 地图导入
        }

        public bitmap ( int size, byte[] map ) {
            this( size ) ;
            System.arraycopy( map, 0, this.map, 4, map.length ) ; // 地图写入
        }

        public boolean get( int offset ) {
           if ( offset >= 0 && offset < size ) {
               return ( ( this.map[ ( offset >>> 3 ) +4 ] >>> ( offset & 7 ) ) & 1 ) == 1 ;
           } else {
               return false ;
           }
        }

        public bitmap set( int offset, boolean val ) {
            if ( offset >= 0 && offset < size ) {
                int p = ( offset >>> 3 ) +4 ;
                int v = 1 << ( offset & 7 ) ;
                this.map[ p ] = (byte) ( val ? this.map[ p ] | v : this.map[ p ] &~ v ) ;
            }
            return this ;
        }

        public bitmap and ( ) { // 还没实现呢
            return this ;
        }

        public bitmap or ( ) {
            return this ;
        }

        public bitmap not ( ) {
            return this ;
        }

        public bitmap setAll ( ) {
            return this ;
        }

        public byte[] getByte ( ) {
            return this.map.clone( ) ;
        }

        public byte[] getMap ( ) {
            byte[] ret = new byte[ this.map.length -4 ] ;
            System.arraycopy( this.map, 4, ret, 0, ret.length ) ;
            return ret ;
        }
    }
}
