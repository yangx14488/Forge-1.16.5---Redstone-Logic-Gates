package net.atcat.nanzhi.redstone_gate.pack.redstone.block;

import net.atcat.nanzhi.redstone_gate.com.block.in.BlockTypeRender;
import net.atcat.nanzhi.redstone_gate.pack.redstone.block.in.RSColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class SlideRheostat extends ComPrototype implements BlockTypeRender, RSColor {

    // 滑动变阻器：可以调节输入信号的强度，在没有信号输入时，自身也可作为一个可调信号源


    public static IntegerProperty ROTATE_LEVEL = IntegerProperty.create( "rotate", 0, 15 ) ;

    public SlideRheostat( ) {
        super( new ComConfigBuilder( "slide_rheostat" )
                .useInput( Direction.SOUTH )
                .useOutput( Direction.NORTH )
                .fastOutput( )
                .updateWhenTheLevelChanges( )
        ) ;
        this.registerDefaultState( this.defaultBlockState( )
                .setValue( ROTATE_LEVEL, 0 )
        ) ;
    }

    @Override
    public int onInputSignalChanged( int[] inputSignal, int blockIO, int blockLevel ) {
        return 1 ;
    }

    @Override
    public RenderTypes getRenderType( ) {
        return RenderTypes.cutout_mipped ;
    }

    public ActionResultType use( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result ) {
        if ( !player.abilities.mayBuild ) {
            return ActionResultType.PASS ;
        } else {
            int r = ( state.getValue( ROTATE_LEVEL ) + ( player.getPose( ).equals( Pose.CROUCHING ) ? -1 : 1 ) ) & 0b1111 ; // 获得新的旋转量
            world.setBlock( pos,state
                    .setValue( ROTATE_LEVEL, r )
                    .setValue( SIGNAL_LEVEL, getSignalLevelFromRotate( this.getInputSignalFromAll( world, state, pos ), r, directionMapping( state.getValue( FACING ) ) ) ),
                    3 ) ;
            return ActionResultType.sidedSuccess( world.isClientSide ) ;
        }
    }

    @Override
    protected int getSignalLevel( int[] inputSignal, int calcOut, BlockState state ) {
        return getSignalLevelFromRotate( inputSignal, state.getValue( ROTATE_LEVEL ), directionMapping( state.getValue( FACING ) ) ) ;
    } ;

    private static int getSignalLevelFromRotate ( int[] inputSignal, int rotate, int facing ) {
        int ar = ( rotate -( facing << 2 ) ) & 0b1111 ;
        return inputSignal[2] == 0 ? ar : Math.max( 0, inputSignal[2] - ar ) ;
    } ;

    // 100: north, 101: east, 102: south, 103: west
    @Override
    public int getMultiplier( BlockState state, int tintIndex ) {
        switch ( tintIndex ) {
            case 1 :
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 4 ) == 4 ? 0 : 15 ;
            case 100 :
                return state.getValue( ComPrototype.SIGNAL_LEVEL ) ;
            case 102 :
                return ( state.getValue( ComPrototype.SIGNAL_IO ) & 4 ) == 4 ? 7 : 0 ;
            default :
                return 0 ;
        }
    }

    @Override
    protected void createBlockStateDefinition( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( ROTATE_LEVEL ) ;
        super.createBlockStateDefinition( builder ) ;
    }

    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        Direction direction = context.getHorizontalDirection( ) ;
        return this.defaultBlockState( ).setValue( FACING, direction ).setValue( ROTATE_LEVEL, directionMapping( direction ) << 2 );
    }

}
