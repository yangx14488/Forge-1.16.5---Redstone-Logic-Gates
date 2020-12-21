/*××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××
 × The MIT License (MIT)
 × Copyright © 2020. 南织( 1448848683@qq.com )
 ×
 × Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 ×
 × The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 ×
 × THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ×××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××*/

package net.atcat.qw_redstone_com.group.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILightReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public abstract class prototype extends RedstoneDiodeBlock {
    //
    protected static class direction_mapping {
        public boolean
                I_east = false ,
                I_south = false ,
                I_west = false ,
                I_north = false ;
        public boolean
                O_east = false ,
                O_south = false ,
                O_west = false ,
                O_north = false ;
        public boolean IN_map ( int i ) {
            int n = i > 3 ? i - 4 : i ;
            switch ( n ) {
                case 0 : return I_east ;
                case 1 : return I_south ;
                case 2 : return I_west ;
                case 3 : return I_north ;
            }
            return false ;
        } ;
        public boolean OUT_map ( int i ) {
            int n = i > 3 ? i - 4 : i ;
            switch ( n ) {
                case 0 : return O_east ;
                case 1 : return O_south ;
                case 2 : return O_west ;
                case 3 : return O_north ;
            }
            return false ;
        } ;
        public int map ( Direction direction ) {
            switch ( direction ) {
                case EAST: return 0 ;
                case SOUTH: return 1 ;
                case WEST: return  2 ;
                case NORTH: return 3 ;
            } ;
            return -1 ;
        }
        public Direction map ( int i ) {
            int n = i > 3 ? i - 4 : i ;
            switch ( n ) {
                case 0 : return Direction.EAST ;
                case 1 : return Direction.SOUTH ;
                case 2 : return Direction.WEST ;
                case 3 : return Direction.NORTH ;
            }
            return Direction.DOWN ;
        } ;
        public boolean IN_change ( int i, Boolean b ) {
            switch ( i ) {
                case 0 : I_east = b ; break ;
                case 1 : I_south = b ; break ;
                case 2 : I_west = b ; break ;
                case 3 : I_north = b ; break ;
            }
            return b ;
        }
        public boolean OUT_change ( int i, Boolean b ) {
            switch ( i ) {
                case 0 : O_east = b ; break ;
                case 1 : O_south = b ; break ;
                case 2 : O_west = b ; break ;
                case 3 : O_north = b ; break ;
            }
            return b ;
        }
        public int abs ( int relative, int direction ) {
            return ( relative == -1 || direction == -1 ) ? -1 : relative > direction ? 4 - ( relative - direction ) : direction - relative ;
        } ;
    }
    public static IntegerProperty
            POWER_STRENGTH = IntegerProperty.create( "strength", 0, 15 ) ,
            INPUT_POWER = IntegerProperty.create( "input", 0, 15 ) ,
            OUTPUT_POWER = IntegerProperty.create( "output", 0, 15 ) ;
    protected static Properties __properties = Properties
            .create( Material.WOOD )
            .hardnessAndResistance( 1, 1 )
            .sound( SoundType.WOOD )
            .harvestLevel( 0 )
            .lightValue( 0 )
            .notSolid( ) ;
    protected static int
            io_0 = 1,
            io_1 = 1 << 1 ,
            io_2 = 1 << 2 ,
            io_3 = 1 << 3 ;
    //
    protected direction_mapping DM = new direction_mapping( ) ; // 无所谓，反正所有同类方块IO一致
    //
    public prototype( int input, int output ) { // 1|2|4|8 -> 0,1,2,3 顺时针, 0最上面
        super( __properties ) ;
        init( input, output ) ;
    } ;
    private void init ( int input, int output ) {
        this.setDefaultState( this.stateContainer.getBaseState( )
                .with( HORIZONTAL_FACING, Direction.EAST ) // 默认东方
                .with( POWERED, Boolean.FALSE )
                .with( INPUT_POWER, 0 )
                .with( OUTPUT_POWER, 0 )
                .with( POWER_STRENGTH, 15 ) // 默认强度15
        ) ;
        int i = 4 ;
        while ( i-->0 ) {
            DM.IN_change( i, ( input & ( 1<<i ) ) > 0 ) ;
            DM.OUT_change( i, ( output & ( 1<<i ) ) > 0 ) ;
        } ;
    } ;
    @Override
    public void tick( BlockState state, ServerWorld world, BlockPos pos, Random rand ) {

        int flag_in = state.get( INPUT_POWER ) ;
        int flag_out = state.get( OUTPUT_POWER ) ;
        int calc_in = this.calc_io_input( world, pos, state, true, false, false ) ;
        int calc_out = this.calc_io_output( state, flag_in, flag_out, calc_in, true, false, false ) ;
        if ( ( flag_in != calc_in ) || ( calc_out != flag_out ) ) { // 不一致
            BlockState n_state = this.change_ready( state, calc_in, calc_out ) ;
            if ( state.get( INPUT_POWER ) != calc_in ) {
                n_state = n_state.with( INPUT_POWER, calc_in ) ;
            } ;
            if ( state.get( OUTPUT_POWER ) != calc_out ) {
                n_state = n_state.with( OUTPUT_POWER, calc_out ) ;
            } ;
            world.setBlockState( pos, n_state, 2 ) ; // 触发一次更新
        } ;
        /*
        if ( flag_out == 0 && calc_out == 0 ) { // 都没有输出时才准备更新
            world.getPendingBlockTicks( ).scheduleTick( pos, this, this.getDelay( state ), TickPriority.VERY_HIGH );
        } ;
         */
    }
    @Override
    protected void updateState( World world, BlockPos pos, BlockState state) {

        int flag_in = state.get( INPUT_POWER ) ;
        int flag_out = state.get( OUTPUT_POWER ) ;
        int calc_in = this.calc_io_input( world, pos, state, false, true, false ) ;
        int calc_out = this.calc_io_output( state, flag_in, flag_out, calc_in, false, true, false ) ;

        if ( ( ( flag_in != calc_in ) || ( calc_out != flag_out ) ) && !world.getPendingBlockTicks( ).isTickPending( pos, this ) ) {
            world.getPendingBlockTicks( ).scheduleTick(pos, this, this.getDelay( state ), TickPriority.VERY_HIGH );
        }

    }
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        int calc_in = this.calc_io_input( world, pos, state, false, false, true ) ;
        int calc_out = this.calc_io_output( state, state.get( INPUT_POWER ), state.get( OUTPUT_POWER ), calc_in, false, false, true ) ;
        if ( calc_out > 0 ) {
            world.getPendingBlockTicks( ).scheduleTick( pos, this, 1 ) ;
        }
    }
    @Override // 红石线连接
    public boolean canConnectRedstone( BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side ) {
        if ( side == null ) return false ;
        int c = DM.abs( DM.map( state.get( HORIZONTAL_FACING ) ), DM.map( side.getOpposite( ) ) ) ;

        return DM.OUT_map( c ) || DM.IN_map( c ) ;
    }
    @Override // 输出更新
    protected void notifyNeighbors( @NotNull World worldIn, @NotNull BlockPos pos, BlockState state) {
        Direction d1 = state.get( HORIZONTAL_FACING ) ; // 当前朝向
        int i = 4 ;
        while ( i-->0 ) if ( DM.OUT_map( i ) ) { // 方向存在输出
            Direction d2 = DM.map( i + DM.map( d1 ) ) ;
            BlockPos blockpos = pos.offset( d2 ) ;
            if ( net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(
                    worldIn,
                    pos,
                    worldIn.getBlockState( pos ),
                    java.util.EnumSet.of( d2 ),
                    false
            ).isCanceled( ) ) return ;
            // 通知方块进行NC更新
            worldIn.neighborChanged( blockpos, this, pos ) ;
            worldIn.notifyNeighborsOfStateExcept( blockpos, this, d2.getOpposite( ) ) ;
        } ;
        worldIn.notifyNeighborsOfStateChange( pos, this ) ;
    }
    @Override // 获取当前块的强充能
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return this.getWeakPower( blockState, blockAccess, pos, side );
    }
    @Override // 获取当前块的弱充能
    public int getWeakPower( BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side ) {
        int c = DM.abs( DM.map( state.get( HORIZONTAL_FACING ) ), DM.map( side.getOpposite( ) ) ) ;
        return DM.OUT_map( c ) && ( state.get( OUTPUT_POWER ) & ( 1 << c ) ) > 0 ? this.calculateOutputStrength( state ) : 0 ;
    }
    protected int get_power ( World world, BlockPos pos, BlockState stateIn, int input ) {
        Direction d0 = stateIn.get( HORIZONTAL_FACING ); // 获得当前方向
        if ( DM.IN_map( input ) ) {
            Direction d1 = DM.map( DM.map( d0 ) + input ) ; // 获得更新的方向
            int pwr = world.getRedstonePower( pos.offset( d1 ), d1 ) ;
            if ( pwr >= 15 ) {
                return pwr ;
            } else {
                BlockState state = world.getBlockState( pos.offset( d1 ) ) ;
                return state.getBlock( ) == Blocks.REDSTONE_WIRE ? Math.max( pwr, state.get( RedstoneWireBlock.POWER ) ) : pwr ;
            }
        } else {
            return 0 ;
        }
    } ;
    // 或
    protected int _or ( boolean io_0, boolean io_1, boolean io_2, boolean io_3 ) {
        return ( io_0 ? 1 : 0 ) | ( io_1 ? 2 : 0 ) | ( io_2 ? 4 : 0 ) | ( io_3 ? 8 : 0 ) ;
    } ;
    protected int _or ( int io_0, int io_1, int io_2, int io_3 ) {
        return _or( io_0 > 0 , io_1 > 0 , io_2 > 0 , io_3 > 0 ) ;
    } ;
    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder) {
        builder.add( HORIZONTAL_FACING, POWERED ).add( INPUT_POWER, OUTPUT_POWER, POWER_STRENGTH ) ;
        super.fillStateContainer( builder );
    }
    @Override
    protected int getDelay( BlockState state ) {
        return 2 ;
    }
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return this.getDefaultState().with( HORIZONTAL_FACING , context.getPlacementHorizontalFacing( ) ) ;
    }
    @Override
    public @NotNull List<ItemStack> getDrops( BlockState state, LootContext.Builder builder ) {
        final List<ItemStack> drops = new ArrayList<>() ;
        drops.add( new ItemStack( this.asItem( ) , 1 ) ) ;
        return drops ;
    }

    protected int calculateOutputStrength( BlockState state ) {
        return state.get( POWER_STRENGTH ) ;
    } ;

    protected int calc_io_input( World worldIn, BlockPos pos, BlockState state ) {
        boolean in_0 = DM.I_east && this.get_power( worldIn, pos, state, 0 ) > 0 ;
        boolean in_1 = DM.I_south && this.get_power( worldIn, pos, state, 1 ) > 0 ;
        boolean in_2 = DM.I_west && this.get_power( worldIn, pos, state, 2 ) > 0 ;
        boolean in_3 = DM.I_north && this.get_power( worldIn, pos, state, 3 ) > 0 ;
        return _or( in_0, in_1, in_2, in_3 ) ;
    } ;
    protected abstract int calc_io_output( int power_in ) ;
    // 高级逻辑门支持
    protected int calc_io_input( World worldIn, BlockPos pos, BlockState state, boolean isTick, boolean isUpdate, boolean isPlace ) {
        return this.calc_io_input( worldIn, pos, state ) ;
    } ;
    protected int calc_io_output( BlockState state, int flag_in, int flag_out, int calc_in, boolean isTick, boolean isUpdate, boolean isPlace ) {
        return this.calc_io_output( calc_in ) ;
    } ;
    protected BlockState change_ready( BlockState state, int calc_in, int calc_out ) { // 准备修改前会访问这个函数，提醒做最后修改，访问该函数时意味着前置计算已经做完了
        return state ;
    } ;

}
