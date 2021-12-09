package net.atcat.nanzhi.redstone_gate.com.block.in;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public interface BlockColorRender {

    int getColor( BlockState state, @Nullable IBlockDisplayReader reader, @Nullable BlockPos pos, int tintIndex ) ;

}
