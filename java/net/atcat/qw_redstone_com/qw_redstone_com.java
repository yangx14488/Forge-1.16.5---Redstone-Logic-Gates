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

//!- 请不要将本模组用于网易版MineCraft盈利
//!- 请保留原作者名 南织( 1448848683@qq.com ) 和版权

package net.atcat.qw_redstone_com;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.atcat.qw_redstone_com.registry.* ;


/*
* 1: 发出NC更新
* 2: 客户端也会更新
* 4: 阻止客户端绘制更新
* 8: 使用主线程更新
* 16: 阻止PP更新
* 32: 阻止周围方块更新时产生掉落物
* 64: 这个块是被移动而更新的（活塞）
*
* */


@Mod( "qw_redstone_com" )
@Mod.EventBusSubscriber( modid = "qw_redstone_com" , bus = Mod.EventBusSubscriber.Bus.MOD )

public class qw_redstone_com {

    public qw_redstone_com( ) {

        final IEventBus bus = FMLJavaModLoadingContext.get( ).getModEventBus( ) ;

        // 加载
        blocks.load( );

        // 注册
        registry.itemReg.register( bus ) ;
        registry.blockReg.register( bus ) ;

    }

}
