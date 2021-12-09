package net.atcat.nanzhi.redstone_gate.com.client;

import net.atcat.nanzhi.redstone_gate.com.block.in.BlockColorRender;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public class BlockColor implements IBlockColor {

    protected final BlockColorRender render ;

    public BlockColor ( BlockColorRender render ) {
        this.render = render ;
    }

    @Override
    public int getColor(BlockState state, @Nullable IBlockDisplayReader reader, @Nullable BlockPos pos, int tintIndex ) {
        return render.getColor( state, reader, pos, tintIndex ) ;
    }
}
