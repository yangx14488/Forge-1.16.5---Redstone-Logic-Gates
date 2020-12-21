/*××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××
 × The MIT License (MIT)
 × Copyright © 2020. 南织( 1448848683@qq.com )
 ×
 × Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 ×
 × The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 ×
 × THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ×××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××*/

package net.atcat.qw_redstone_com.registry;

import net.atcat.qw_redstone_com.group.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.Properties;

public class blocks {
    public static void load ( ) { } ;
    private static Item.Properties $g ( ) {
        return new Item.Properties( ).group( ItemGroup.REDSTONE ) ;
    }
    public static final Block
            and = registry.register( "and", new and( ), $g( ) ) ,
            or = registry.register( "or", new or( ), $g( ) ) ,
            not = registry.register( "not", new not( ), $g( ) ) ,
            xor = registry.register( "xor", new xor( ), $g( ) ) ,
            xor_step = registry.register( "xor_step", new xor_step( ), $g( ) ) ,
            xor_step_inv = registry.register( "xor_step_inv", new xor_step_inv( ), $g( ) ) ,
            t_trigger = registry.register( "t_trigger", new t_trigger( ), $g( ) ) ,
            random = registry.register( "random", new random( ), $g( ) ) ,
            rs_latch = registry.register( "rs_latch", new rs_latch( ), $g( ) ) ;
} ;
