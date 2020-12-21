/*××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××
 × The MIT License (MIT)
 × Copyright © 2020. 南织( 1448848683@qq.com )
 ×
 × Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”o), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 ×
 × The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 ×
 × THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ×××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××*/

package net.atcat.qw_redstone_com;

import net.atcat.qw_redstone_com.registry.blocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.atcat.qw_redstone_com.group.block.color.* ;

// 客户端
@Mod.EventBusSubscriber( modid = "qw_redstone_com", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD )
public class render {

    // 二值
    private static void cutoutMipped( Block... block ) {
        for ( Block b : block ) RenderTypeLookup.setRenderLayer( b, RenderType.getCutoutMipped( ) ) ;
    } ;

    // 半透明
    private static void tanslucent( Block... block ) {
        for ( Block b : block ) RenderTypeLookup.setRenderLayer( b, RenderType.getTranslucent( ) ) ;
    } ;

    @SubscribeEvent // 渲染器
    public static void onRenderTypeSetup( FMLClientSetupEvent event ) {
        cutoutMipped( blocks.xor, blocks.or, blocks.and, blocks.not, blocks.xor_step, blocks.xor_step_inv ) ;
        cutoutMipped( blocks.t_trigger, blocks.random, blocks.rs_latch ) ;
    } ;

    @SubscribeEvent // 着色器
    public static void blockColors( ColorHandlerEvent.Block event) {
        BlockColors colorReg = event.getBlockColors( ) ;
        colorReg.register( new rs_latch( ), blocks.rs_latch ) ;
        colorReg.register( new t_trigger( ), blocks.t_trigger ) ;
    }

}
