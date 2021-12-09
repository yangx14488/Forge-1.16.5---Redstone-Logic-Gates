package net.atcat.nanzhi.redstone_gate.pack.redstone.block;

import net.atcat.nanzhi.redstone_gate.RedstoneGate;
import net.atcat.nanzhi.redstone_gate.com.block.in.BlockTypeRender;
import net.atcat.nanzhi.redstone_gate.obj.NZMath;
import net.atcat.nanzhi.redstone_gate.pack.redstone.Redstone;
import net.atcat.nanzhi.redstone_gate.pack.redstone.block.in.RSColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class Counter extends ComPrototype implements BlockTypeRender, RSColor {

    protected static final int[][] SCREEN_BIT = new int[][] {
            new int[]{ 15, 15, 15,  0, 15, 15, 15 } , // 0
            new int[]{  0,  0, 15,  0,  0, 15,  0 } , // 1
            new int[]{ 15,  0, 15, 15, 15,  0, 15 } , // 2
            new int[]{ 15,  0, 15, 15,  0, 15, 15 } , // 3
            new int[]{  0, 15, 15, 15,  0, 15,  0 } , // 4
            new int[]{ 15, 15,  0, 15,  0, 15, 15 } , // 5
            new int[]{ 15, 15,  0, 15, 15, 15, 15 } , // 6
            new int[]{ 15,  0, 15,  0,  0, 15,  0 } , // 7
            new int[]{ 15, 15, 15, 15, 15, 15, 15 } , // 8
            new int[]{ 15, 15, 15, 15,  0, 15, 15 } , // 9
    } ;

    protected static VoxelShape SHAPE = VoxelShapes.or(
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D ) ,
            Block.box(1.0D, 5.0D, 1.0D, 15.0D, 6.0D, 15.0D )
    ) ;

    static {
        SHAPE = VoxelShapes.join( SHAPE, Block.box(0D, 2D, 7D, 1D, 5D, 9D ), IBooleanFunction.NOT_SAME ) ;
        SHAPE = VoxelShapes.join( SHAPE, Block.box(15D, 2D, 7D, 16D, 5D, 9D ), IBooleanFunction.NOT_SAME ) ;
        SHAPE = VoxelShapes.join( SHAPE, Block.box(7D, 2D, 0D, 9D, 5D, 1D ), IBooleanFunction.NOT_SAME ) ;
        SHAPE = VoxelShapes.join( SHAPE, Block.box(7D, 2D, 15D, 9D, 5D, 16D ), IBooleanFunction.NOT_SAME ) ;
    } ;

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    public Counter( ) {
        super( new ComConfigBuilder( "counter" )
                .useInput( Direction.EAST, Direction.WEST, Direction.SOUTH )
                .useOutput( Direction.NORTH )
                .fastOutput( )
                .updateWhenTheLevelChanges( )
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

    @Override
    public int getSignalLevel( int[] inputSignal, int blockIO, int blockLevel ) {
        return ( inputSignal[2] != 0 && ( blockIO & 4 ) == 0 )
                ? 0
                : NZMath.limit( 0, blockLevel +( ( inputSignal[1] != 0 && ( blockIO & 2 ) == 0 )
                    ? 1
                    : ( inputSignal[3] != 0 && ( blockIO & 8 ) == 0 ) ? -1 : 0
                ) , 15 ) ;
    }

    // 100: north, 101: east, 102: south, 103: west
    @Override
    public int getMultiplier( BlockState state, int tintIndex ) {
        // 编码规则： 0b00111 这个区间 用来记录可用显示，0b01000 用来标记是否为一个屏幕，0b10000 用来标记是否为10位数
        if ( ( tintIndex & 0b01000 ) != 0 ) {
            int level = state.getValue( SIGNAL_LEVEL ) ;
            int num0 = level >= 10 ? 1 : 0 ;
            return SCREEN_BIT[ ( tintIndex & 0b10000 ) == 0 ? num0 : num0 == 1 ? level -10 : level ][ tintIndex & 0b111 ] ;
        }
        return 0 ;
    } ;

    @Override
    @OnlyIn( Dist.CLIENT )
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader reader, List<ITextComponent> list, ITooltipFlag flag ) {
        list.add( new TranslationTextComponent( "info." + RedstoneGate.ID +"." +this.comConfig.getName( ) ).withStyle( TextFormatting.GRAY ) ) ;
        list.add( new StringTextComponent( "" ) ) ;
        list.add( new TranslationTextComponent( "io." +RedstoneGate.ID ).withStyle( TextFormatting.DARK_GRAY ) );
        list.add( new TranslationTextComponent( "io." +RedstoneGate.ID +".gray" ) );
        list.add( new TranslationTextComponent( "io." + RedstoneGate.ID +"." +this.comConfig.getName( ) +".green" ) ) ;
        list.add( new TranslationTextComponent( "io." + RedstoneGate.ID +"." +this.comConfig.getName( ) +".orange" ) ) ;
        list.add( new TranslationTextComponent( "io." + RedstoneGate.ID +"." +this.comConfig.getName( ) +".blue" ) ) ;

    }


}
