package net.atcat.nanzhi.redstone_gate.com.client.event;

import net.atcat.nanzhi.redstone_gate.StaticStorage;
import net.atcat.nanzhi.redstone_gate.com.Registry;
import net.atcat.nanzhi.redstone_gate.com.block.in.BlockColorRender;
import net.atcat.nanzhi.redstone_gate.com.block.in.BlockTypeRender;
import net.atcat.nanzhi.redstone_gate.com.client.BlockColor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn( Dist.CLIENT )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
public class BlockRenderTypeEvent {

    /** 用于设置渲染类型
     * @see net.atcat.nanzhi.redstone_gate.StaticStorage #RegArr   - 存储注册器的数组
     * @see Registry           - 注册器
     * */
    @SubscribeEvent
    static void onRenderTypeSetup( FMLClientSetupEvent event ) {
        event.enqueueWork( ( ) -> {
            StaticStorage.RegArr.forEach( reg -> {
                reg.blockReg.getEntries( ).forEach( r -> {
                    Block block = r.get( ) ;
                    RenderType type = null ;
                    if ( block instanceof BlockTypeRender) {
                        switch ( ( (BlockTypeRender) block ).getRenderType( ) ) {
                            case solid:
                                type = RenderType.solid( ) ;
                                break;
                            case cutout:
                                type = RenderType.cutout( ) ;
                                break;
                            case translucent:
                                type = RenderType.translucent( ) ;
                                break;
                            case cutout_mipped:
                                type = RenderType.cutoutMipped( ) ;
                                break;
                        }
                    }
                    if ( type != null ) {
                        RenderTypeLookup.setRenderLayer( block, type ) ;
                    }
                } ) ;
            } ) ;
        } ) ;
    }

    @SubscribeEvent // 着色器
    public static void blockColors( ColorHandlerEvent.Block event) {
        BlockColors colorReg = event.getBlockColors( ) ;
        StaticStorage.RegArr.forEach( reg -> {
            reg.blockReg.getEntries( ).forEach( r -> {
                Block block = r.get( ) ;
                if ( block instanceof BlockColorRender ) {
                    colorReg.register( new BlockColor( (BlockColorRender) block ), block ) ;
                } ;
            } ) ;
        } ) ;
    }

}
