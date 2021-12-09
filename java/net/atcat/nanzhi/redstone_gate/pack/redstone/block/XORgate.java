package net.atcat.nanzhi.redstone_gate.pack.redstone.block;

import net.atcat.nanzhi.redstone_gate.com.block.in.BlockTypeRender;
import net.atcat.nanzhi.redstone_gate.pack.redstone.block.in.RSColor;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public class XORgate extends ComPrototype implements BlockTypeRender, RSColor {

    public XORgate( ) {
        super( new ComConfigBuilder( "xor" )
                .useInput( Direction.EAST, Direction.WEST )
                .useOutput( Direction.NORTH )
                .fastOutput( )
                .updateWhenTheLevelChanges( )
        ) ;
    }

    @Override
    public int onInputSignalChanged( int[] inputSignal, int blockIO, int blockLevel ) {
        return inputSignal[1] > 0 != inputSignal[3] > 0 ? 1 : 0 ;
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
            case 100 : // n out
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 1 ) == 1 ? 15 : 0 ;
            case 101 : // e in
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 2 ) == 2 ? 15 : 0 ;
            case 103 : // w in
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 8 ) == 8 ? 15 : 0 ;
            default :
                if ( tintIndex == 20 || tintIndex == 21 ) {
                    int n = 0 ;
                    int io = state.getValue( ComPrototype.SIGNAL_IO ) ;
                    n += ( io & 2 ) == 2 ? 1 : 0 ;
                    n += ( io & 8 ) == 8 ? 1 : 0 ;
                    return ( tintIndex == 20 ? ( n & 1 ) == 1 : ( n & 2 ) == 2 ) ? 15 : 0 ;
                }
                return 0 ;
        }
    }

}
