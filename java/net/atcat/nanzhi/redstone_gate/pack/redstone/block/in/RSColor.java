package net.atcat.nanzhi.redstone_gate.pack.redstone.block.in;

import net.atcat.nanzhi.redstone_gate.com.block.in.BlockColorRender;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public interface RSColor extends BlockColorRender {

    int getMultiplier( BlockState state, int tintIndex ) ;

    @Override
    default int getColor( BlockState state, @Nullable IBlockDisplayReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return RedstoneWireBlock.getColorForPower( this.getMultiplier( state, tintIndex ) ) ;
    }

}

