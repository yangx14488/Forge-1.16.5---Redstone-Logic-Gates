package net.atcat.nanzhi.redstone_gate.pack.redstone.block;

import net.atcat.nanzhi.redstone_gate.RedstoneGate;
import net.atcat.nanzhi.redstone_gate.com.block.in.BlockTypeRender;
import net.atcat.nanzhi.redstone_gate.pack.redstone.Redstone;
import net.atcat.nanzhi.redstone_gate.pack.redstone.block.in.RSColor;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class Dflipflop extends ComPrototype implements BlockTypeRender, RSColor {

    protected static final int defVal = 0b00100 ;

    public Dflipflop( ) {
        super( new ComConfigBuilder( "d_flipflop" )
                .useInput( Direction.EAST, Direction.WEST, Direction.SOUTH )
                .useOutput( Direction.NORTH )
                .fastOutput( )
                .updateWhenTheLevelChanges( )
        ) ;
    }

    @Override
    public int onInputSignalChanged(int[] inputSignal, int blockIO, int blockLevel ) {
        return ( inputSignal[3] > 0 || inputSignal[1] > 0 ) ? inputSignal[2] > 0 ? 1 : 0 : blockIO ;
    }

    @Override
    public RenderTypes getRenderType( ) {
        return RenderTypes.cutout_mipped ;
    }

    @Override
    public int getSignalLevel( int[] inputSignal, int blockIO, int blockLevel ) {
        return ( blockIO & 1 ) == 1 ? 15 : 0 ;
    }

    // 100: north, 101: east, 102: south, 103: west
    @Override
    public int getMultiplier( BlockState state, int tintIndex ) {
        switch ( tintIndex ) {
            case 1 : // north
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 4 ) == 4 ? 0 : 15 ;
            case 100 : // north
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 1 ) == 1 ? 15 : 0 ;
            case 101 : // west
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 2 ) == 2 ? 15 : 0 ;
            case 102 : // north
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 4 ) == 4 ? 15 : 0 ;
            case 103 : // west
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 8 ) == 8 ? 15 : 0 ;
            default :
                return 0 ;
        }
    }
    @Override
    @OnlyIn( Dist.CLIENT )
    public void appendHoverText( ItemStack stack, @Nullable IBlockReader reader, List<ITextComponent> list, ITooltipFlag flag ) {
        super.appendHoverText( stack, reader, list, flag ) ;
        list.add( new TranslationTextComponent( "io." + RedstoneGate.ID +"." +this.comConfig.getName( ) +".green" ) ) ;

    }

}
