package net.atcat.nanzhi.redstone_gate.pack.redstone.block;

import net.atcat.nanzhi.redstone_gate.com.block.in.BlockTypeRender;
import net.atcat.nanzhi.redstone_gate.pack.redstone.block.in.RSColor;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public class RStrigger extends ComPrototype implements BlockTypeRender, RSColor {

    protected static final int defVal = 0b00100 ;

    public RStrigger( ) {
        super( new ComConfigBuilder( "rs_trigger" )
                .useInput( Direction.EAST, Direction.WEST )
                .useOutput( Direction.NORTH, Direction.SOUTH )
                .fastOutput( )
                .updateWhenTheLevelChanges( )
        ) ;
    }

    @Override
    public int onInputSignalChanged(int[] inputSignal, int blockIO, int blockLevel) {
        int ret = ( inputSignal[3] > 0 ? 0b0100 : 0 ) | ( inputSignal[1] > 0 ? 0b0001 : 0 ) ;
        int out = blockIO & this.bitOut ;
        return ret == 0 ? ( ( out == 0 || out == 0b0101 ) ? 0b0001 : out ) : ret ;
    }

    @Override
    public RenderTypes getRenderType( ) {
        return RenderTypes.cutout_mipped ;
    }

    @Override
    public int getSignalLevel( int[] inputSignal, int blockIO, int blockLevel) {
        return 15 ;
    }

    // 100: north, 101: east, 102: south, 103: west
    @Override
    public int getMultiplier( BlockState state, int tintIndex ) {
        switch ( tintIndex ) {
            case 10 : // north
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 4 ) == 4 ? 0 : 15 ;
            case 12 : // west
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 1 ) == 1 ? 0 : 15 ;
            case 100 : // north
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 1 ) == 1 ? 15 : 0 ;
            case 102 : // west
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 4 ) == 4 ? 15 : 0 ;
            default :
                return 0 ;
        }
    }

}
