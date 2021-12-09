package net.atcat.nanzhi.redstone_gate.pack.redstone.block;

import net.atcat.nanzhi.redstone_gate.com.block.in.BlockTypeRender;
import net.atcat.nanzhi.redstone_gate.pack.redstone.block.in.RSColor;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public class ORgate extends ComPrototype implements BlockTypeRender, RSColor {

    public ORgate( ) {
        super( new ComConfigBuilder( "or" )
                .useInput( Direction.EAST, Direction.WEST )
                .useOutput( Direction.NORTH )
                .fastOutput( )
                .updateWhenTheLevelChanges( )
        ) ;
    }

    @Override
    public int onInputSignalChanged(int[] inputSignal, int blockIO, int blockLevel) {
        return inputSignal[1] > 0 || inputSignal[3] > 0 ? 1 : 0 ;
    }

    @Override
    public RenderTypes getRenderType( ) {
        return RenderTypes.cutout_mipped ;
    }

    @Override
    public int getSignalLevel(int[] inputSignal, int blockIO, int blockLevel) {
        return ( blockIO & 1 ) == 1 ? 15 : 0 ;
    }

    // 100: north, 101: east, 102: south, 103: west
    @Override
    public int getMultiplier( BlockState state, int tintIndex ) {
        switch ( tintIndex ) {
            case 1 : // line
            case 2 : // center
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 1 ) == 1 ? 0 : 15 ;
            case 100 : // north
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 1 ) == 1 ? 15 : 0 ;
            case 101 : // east
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 2 ) == 2 ? 15 : 0 ;
            case 103 : // west
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 8 ) == 8 ? 15 : 0 ;
            default :
                return 0 ;
        }
    }

}
