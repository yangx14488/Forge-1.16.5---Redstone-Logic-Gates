package net.atcat.nanzhi.redstone_gate.pack.redstone.block;

import net.atcat.nanzhi.redstone_gate.com.block.in.BlockTypeRender;
import net.atcat.nanzhi.redstone_gate.pack.redstone.block.in.RSColor;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public class NOTgate extends ComPrototype implements BlockTypeRender, RSColor {

    public NOTgate( ) {
        super( new ComConfigBuilder( "not" )
                .useInput( Direction.SOUTH )
                .useOutput( Direction.NORTH )
                .fastOutput( )
                .updateWhenTheLevelChanges( )
        ) ;
    }

    @Override
    public int onInputSignalChanged(int[] inputSignal, int blockIO, int blockLevel) {
        return inputSignal[2] == 0 ? 1 : 0 ;
    }

    @Override
    public RenderTypes getRenderType( ) {
        return RenderTypes.cutout_mipped ;
    }

    @Override
    public int getSignalLevel( int[] inputSignal, int blockIO, int blockLevel) {
        return ( blockIO & 1 ) == 1 ? 15 : 0 ;
    }

    // 100: north, 101: east, 102: south, 103: west
    @Override
    public int getMultiplier( BlockState state, int tintIndex ) {
        switch ( tintIndex ) {
            case 100 : // north
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 1 ) == 1 ? 15 : 0 ;
            case 102 : // west
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 4 ) == 4 ? 15 : 0 ;
            default :
                return 0 ;
        }
    }

}
