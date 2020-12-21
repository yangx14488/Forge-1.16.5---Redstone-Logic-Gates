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

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class registry {

    public static final DeferredRegister<Block> blockReg = new DeferredRegister<>( ForgeRegistries.BLOCKS, "qw_redstone_com" ) ;
    public static final DeferredRegister<Item> itemReg = new DeferredRegister<>( ForgeRegistries.ITEMS, "qw_redstone_com" ) ;

    // 方块注册
    public static Block register(String name, Block block, Item.Properties properties) {
        blockReg.register( name, ( ) -> block ) ;
        itemReg.register( name, ( ) -> new BlockItem( block, properties ) );
        return block ;
    }
    // 带有域的方块注册
    public static Block register(String domain, String name, Block block, Item.Properties properties) {
        return register( domain + "/" + name, block, properties ) ;
    }
    // 物品注册
    public static Item register(String name, Item item) {
        itemReg.register( name, ( ) -> item );
        return item ;
    }
    // 带有域的物品注册
    public static Item register( String domain, String name, Item item ) {
        return register( domain + "/" + name, item ) ;
    }
    // 独立物品的方块注册
    public static Block register( String name, BlockItem BlockItemIn ) {
        blockReg.register( name, BlockItemIn::getBlock ) ;
        itemReg.register( name, ( ) -> BlockItemIn );
        return BlockItemIn.getBlock( ) ;
    }
    // 域
    public static Block register( String domain, String name, BlockItem BlockItemIn ) {
        return register( domain + "/" + name, BlockItemIn ) ;
    }
}
