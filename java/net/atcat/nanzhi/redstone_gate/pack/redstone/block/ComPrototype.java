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

package net.atcat.nanzhi.redstone_gate.pack.redstone.block;

import net.atcat.nanzhi.redstone_gate.RedstoneGate;
import net.atcat.nanzhi.redstone_gate.obj.NZMath;
import net.atcat.nanzhi.redstone_gate.pack.Pack;
import net.atcat.nanzhi.redstone_gate.pack.redstone.Redstone;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// 请注意：所有的输入都是数字输入，以方便计算和节省性能

/** 方块逻辑原型
 *
 * 标准和定义
 * @apiNote 方向定义和映射
 * |- 方向的映射永远是 north(0), east(1), south(2), west(3), other(-1)
 * |- 可使用映射方法 directionMapping 进行转换
 * @apiNote 输入参数是描述各个方向的状态的 int[4] 时
 * |- 方向的数字映射即元素在数组中的位置
 * |- 对于数组中的任意元素 x，都是一个大于或等于 0，小于或等于 15 的整数，用来表明一个信号强度
 * |- 若未启用 信号等级输入，则数组中的任意元素 x 可以表示为 x ∈ {0,1}
 * |- 任何表明一个输出状态的数组，对于数组中的任意元素 x，也都可以表示为 x ∈ {0,1}
 */
public abstract class ComPrototype extends RedstoneDiodeBlock {

    // 地图映射 north(0), east(1), south(2), west(3)
    public static Direction directionMapping( int index ) {
        switch ( index & 0b11 ) {
            case 0 : return Direction.NORTH ;
            case 1 : return Direction.EAST ;
            case 2 : return Direction.SOUTH ;
            case 3 : return Direction.WEST ;
            default : return Direction.DOWN ;
        }
    } ;
    public static int directionMapping( Direction direction ) {
        switch ( direction ){
            case NORTH : return 0 ;
            case EAST : return 1 ;
            case SOUTH : return 2 ;
            case WEST : return 3 ;
            default : return -1 ;
        }
    } ;
    // 偏差修正
    protected static int getAbsoluteDirection( int relative, int direction ) { return ( 4 +relative -direction ) & 0b11 ; } ;
    protected static Direction getAbsoluteDirection( Direction relative, Direction direction ) {
        return directionMapping( getAbsoluteDirection( directionMapping( relative ), directionMapping( direction ) ) ) ;
    } ;
    // 快速获得io
    protected static int getIO( int[ ] input, int outputSwitch ) {
        return outputSwitch | ( input[0] == 0 ? 0 : 1 ) | ( input[1] == 0 ? 0 : 2 ) | ( input[2] == 0 ? 0 : 4 ) | ( input[3] == 0 ? 0 : 8 ) ;
    } ;

    public static IntegerProperty SIGNAL_LEVEL = BlockStateProperties.POWER ;
    public static IntegerProperty SIGNAL_IO = IntegerProperty.create( "io", 0, 15 ) ;

    protected static Properties __properties = Properties
            .of( Material.STONE )
            .strength( 2, 1 )
            .sound( SoundType.STONE )
            .harvestLevel( 0 )
            .lightLevel( state -> 0 )
            .noOcclusion( ) ;

    // 或
    protected static int or( boolean io0, boolean io1, boolean io2, boolean io3 ) {
        return ( io0 ? 0b0001 : 0 ) | ( io1 ? 0b0010 : 0 ) | ( io2 ? 0b0100 : 0 ) | ( io3 ? 0b1000 : 0 ) ;
    } ;
    protected static int or( Direction ...ds ) {
        int i = 0 ;
        for ( Direction d : ds ) switch ( d ) {
            case NORTH : i |= 0b0001 ; break ;
            case EAST  : i |= 0b0010 ; break ;
            case SOUTH : i |= 0b0100 ; break ;
            case WEST  : i |= 0b1000 ; break ;
        } ;
        return i ;
    } ;
    // 编码传递
    protected static int signalEncode ( int north, int east, int south, int west ) {
        int signal = 0 ;
        signal = NZMath.setBitToInt( signal, 4, 0, north ) ;
        signal = NZMath.setBitToInt( signal, 4, 1, east ) ;
        signal = NZMath.setBitToInt( signal, 4, 2, south ) ;
        signal = NZMath.setBitToInt( signal, 4, 3, west ) ;
        return signal ;
    } ;
    protected static int signalDecode ( int signal, int direction ) {
        return NZMath.getBitFromInt( signal, 4, NZMath.limit( 0, direction, 3 ) ) ;
    } ;
    // 配置类，不具备读取的能力，但是可写
    public static class ComConfigBuilder {
        private final boolean[] input = new boolean[4];
        private final boolean[] output = new boolean[4];
        private final String name ;
        private boolean levelChanges = false ;
        private boolean fastOutput = false ;
        public ComConfigBuilder( String name ) {
            this.name = name ;
        } ;
        private static void setIOFormDirection( Direction[] direction, boolean[] io, String className ) {
            for ( Direction d : direction ) {
                int index = directionMapping( d ) ;
                if ( index != -1 ) {
                    io[ index ] = true ;
                } else {
                    Pack.logger.warn( "An unsupported direction. Class<" +className +">, Direction<"+d.getName( ) +">" ) ;
                } ;
            } ;
        } ;
        public static void setIOFormInt( int[] direction, boolean[] io, String className ) {
            for ( int i : direction ) {
                if ( ( i & 3 ) == i ) {
                    io[ i ] = true ;
                } else {
                    Pack.logger.warn( "An unsupported direction. Class<" +className +">, Index<"+i +">" ) ;
                } ;
            } ;
        } ;
        /** 设置输入接口
         *
         * @param direction   方向
         */
        public ComConfigBuilder useInput(Direction... direction ) {
            setIOFormDirection( direction, input, this.getClass( ).getName( ) ) ;
            return this ;
        } ;
        public ComConfigBuilder useInput(int... direction ) {
            setIOFormInt( direction, input, this.getClass( ).getName( ) ) ;
            return this ;
        } ;
        /** 设置输出接口
         *
         * 用法同上
         */
        public ComConfigBuilder useOutput(Direction...direction ) {
            setIOFormDirection( direction, output, this.getClass( ).getName( ) ) ;
            return this ;
        } ;
        public ComConfigBuilder useOutput(int... direction ) {
            setIOFormInt( direction, output, this.getClass( ).getName( ) ) ;
            return this ;
        } ;
        /** ⚠️快速输出
         *
         * 访问这个方法后，将不再触发 onWorldGetSignal 方法
         * 输出将由一套快速处理代码来实现，开发者只需要再信号变动时计算最终输出信号等级
         *
         * 若需要定义高级逻辑门，请不要启用快速输出，该开关启用后会接管部分计算
         *
         */
        public ComConfigBuilder fastOutput ( ) {
           this.fastOutput = true ;
           return this ;
        } ;
        /** ⚠️电平变化时更新
         *
         * 访问这个方法后，只有输入电平变化时才会触发更新方法（从0到1~15，或从1~15到0）
         * 这会提升性能，但这会不利于计算
         *
         */
        public ComConfigBuilder updateWhenTheLevelChanges ( ) {
            this.levelChanges = true ;
            return this ;
        } ;
        // 构建
        public ComConfig build ( ) {
            return new ComConfig( this.name, this.input, this.output, this.fastOutput, this.levelChanges ) ;
        } ;
    }
    // 存储类，不具备外可写的能力，但是可读
    public static class ComConfig {
        private final boolean[] input = new boolean[4] ;
        private final boolean[] output = new boolean[4] ;
        private final String name ;
        private final boolean levelChanges ;
        private final boolean fastOutput ;
        public ComConfig( String name, boolean[] i, boolean[] o, boolean f, boolean l) {
            System.arraycopy( i, 0, this.input, 0, 4 ) ;
            System.arraycopy( o, 0, this.output, 0, 4 ) ;
            this.levelChanges = l ;
            this.fastOutput = f ;
            this.name = name ;
        } ;
        public boolean canInput( int index ) {
            return this.input[ index & 0b11 ] ;
        } ;
        public boolean canInput( Direction direction ) {
            return this.canInput( directionMapping( direction ) ) ;
        } ;
        public boolean canOutput( int index ) {
            return this.output[ index & 0b11 ] ;
        } ;
        public boolean canOutput( Direction direction ) { return this.canOutput( directionMapping( direction ) ) ; } ;
        public boolean ioIsTurnedOn( int index ) { return this.canInput( index ) || this.canOutput( index ) ; } ;
        public boolean ioIsTurnedOn( Direction direction ) { return this.canInput( direction ) || this.canOutput( direction ) ; } ;
        public String getName( ) { return this.name ; } ;
    } ;


    // 配置
    public final ComConfig comConfig ;
    protected final int bitIn ;
    protected final int bitOut ;

    /**
     *
     * @param config   配置
     */
    public ComPrototype( ComConfigBuilder config ) {
        this( config, __properties ) ;
    } ;
    public ComPrototype( ComConfigBuilder config, Properties properties ) {
        super( properties ) ;
        this.registerDefaultState( this.stateDefinition.any( )
                .setValue( FACING, Direction.NORTH ) // 默认北方
                .setValue( SIGNAL_IO, 0 )
                .setValue( SIGNAL_LEVEL, 0 ) // 默认输出强度0
        ) ;
        this.comConfig = config.build( ) ;
        int bitIn = 0 ;
        int bitOut = 0 ;
        for ( int i = 0 ; i < 4 ; i++ ) {
            bitIn |= this.comConfig.canInput( i ) ? 1 << i : 0 ;
            bitOut |= this.comConfig.canOutput( i ) ? 1 << i : 0 ;
        } ;
        this.bitIn = bitIn ;
        this.bitOut = bitOut ;

    } ;

    // 此处并非指每游戏刻或者方块刻，而是指触发后的处理机制
    @Override
    public void tick( BlockState state, ServerWorld world, BlockPos pos, Random rand )  {

        // 输入
        int[] signalIn = getInputSignalFromAll( world, state, pos ) ;
        // 输出
        int calcOut = this.inputSignalChanged( signalIn, state ) ;
        // 等级
        int calcLevel = this.getSignalLevel( signalIn, calcOut, state ) ;
        // io
        int io = getIO( signalIn, calcOut ) ;

        // io不匹配或者信号等级不匹配
        if ( io != state.getValue( SIGNAL_IO ) || calcLevel != state.getValue( SIGNAL_LEVEL ) ) {
            // 更新方块
            world.setBlock( pos, state
                    .setValue( SIGNAL_IO, io )
                    .setValue( SIGNAL_LEVEL, calcLevel ), 2 ) ;
        } ;

    }
    // 检查红石线是否可连接
    @Override
    public boolean canConnectRedstone( BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side ) {
        // 转为数字输入，节省性能
        return side != null && comConfig.ioIsTurnedOn( getAbsoluteDirection( directionMapping( state.getValue( FACING ) ), directionMapping( side ) +2 ) ) ;
    }
    // 检查更新刻（由 neighborChanged 触发）
    @Override
    protected void checkTickOnNeighbor( World world, BlockPos pos, BlockState state ) {

        int[] signalIn = getInputSignalFromAll( world, state, pos ) ;
        int blockIO = state.getValue( SIGNAL_IO ) ;

        boolean flag0 = false ; // 更新标记
        boolean flag1 = false ; // 方向变化（最高更新）
        boolean flag2 = false ; // 开机状态（高更新）

        if ( this.comConfig.levelChanges ) { // 电平模式
            // 不同电平时标记
            flag0 = getIO( signalIn, 0 ) != ( blockIO & this.bitIn ) ;
        } else {
            // 取得输出
            int blockLevel = state.getValue( SIGNAL_LEVEL ) ;
            int calcOut = this.inputSignalChanged( signalIn, state ) ;
            // IO不匹配时标记
            if ( getIO( signalIn, calcOut ) != state.getValue( SIGNAL_IO ) ) {
                flag1 = true ;
            } else {
                // 信号不匹配时标记
                flag0 = blockLevel != this.getSignalLevel( signalIn, calcOut, state ) ;
                // 记录开机状态
                flag2 = blockLevel > 0 ;
            } ;
        } ;
        if ( ( flag0 || flag1 || flag2 ) && !world.getBlockTicks( ).willTickThisTick( pos, this ) ) { // 更新的
            TickPriority tickPriority = TickPriority.HIGH ;
            if ( flag1 ) {
                tickPriority = TickPriority.EXTREMELY_HIGH ;
            } else if ( flag2 ) {
                tickPriority = TickPriority.VERY_HIGH ;
            } ;
            world.getBlockTicks( ).scheduleTick( pos, this, this.getDelay( state ), tickPriority ) ;
        } ;

    }
    @Override // 被放置时
    public void setPlacedBy( World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack ) {
        if ( !world.isClientSide( ) ) {
            // 触发一次更新
            this.tick( state, (ServerWorld) world, pos, world.random ) ;
        }
    }

    @Override // 通知附近，这里有更新
    protected void updateNeighborsInFront( @Nonnull World worldIn, @Nonnull BlockPos pos, BlockState state ) {
        int od = directionMapping( state.getValue( FACING ) ) ;
        int i = 4 ;
        while ( i-->0 ) if ( comConfig.canOutput( i ) ) {
            Direction ad = directionMapping( i + od ) ; // 获得绝对方向
            BlockPos blockpos = pos.relative( ad ) ;
            // 检查是否可通知
            if ( !net.minecraftforge.event.ForgeEventFactory.onNeighborNotify( worldIn, pos, worldIn.getBlockState( pos ), java.util.EnumSet.of( ad ), false ).isCanceled( ) ) {
                // 通知方块进行NC更新
                worldIn.neighborChanged( blockpos, this, pos ) ;
                worldIn.updateNeighborsAtExceptFromFacing( blockpos, this, ad.getOpposite( ) ) ;
            } ;
        } ;
        worldIn.updateNeighborsAt( pos, this ) ;
    }

    // 获取信号
    @Override
    public int getDirectSignal( BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side ) {
        return this.getSignal( blockState, blockAccess, pos, side );
    }
    // 获取信号
    @Override
    public int getSignal( BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side ) {
        int ad = getAbsoluteDirection( directionMapping( state.getValue( FACING ) ), directionMapping( side ) +2 ) ; // 获得相对方向
        // 如果接口有能输出，且io是打开的（不用过滤，因为输出接口如果非预定输出但却有信号，多半是憨批同时打开输出输入了）
        return this.comConfig.canOutput( ad ) && ( ( state.getValue( SIGNAL_IO ) >>> ad ) & 1 ) == 1
                ? comConfig.fastOutput
                    ? state.getValue( SIGNAL_LEVEL )
                    : this.onWorldGetSignal( ad, state.getValue( SIGNAL_IO ), state.getValue( SIGNAL_LEVEL ) )
                : 0
                ;
    }

    /** 当输入的信号已改变时触发的方法
     *
     * @param inputSignal   4向输入的信号值，高电平是15，低电平是0，如果启用了具体的输入，那么会变成从0~15的区间范围输入。
     * @param blockLevel
     * @return 4向布尔数组，返回长度不够4会补足，超过4会截断。
     * @apiNote 要是发现对应方向不输出，请检查下你是否打开了该方向的输出
     */
    public int onInputSignalChanged( int[] inputSignal, int blockIO, int blockLevel ) {
        return 0 ;
    } ;
    private int inputSignalChanged( int[] inputSignal, BlockState state ) {
        // 用来限定输出的
        return onInputSignalChanged( inputSignal, state.getValue( SIGNAL_IO ), state.getValue( SIGNAL_LEVEL ) ) & this.bitOut ;
    } ;
    /** 输出的信号强度
     *
     * @param inputSignal   4向输入的信号值，高电平是15，低电平是0，如果启用了具体的输入，那么会变成从0~15的区间范围输入。
     * @param blockIO  4向输入的信号电平
     * @param blockLevel
     * @return 设置当前方块的信号强度，0~15
     */
    public int getSignalLevel( int[] inputSignal, int blockIO, int blockLevel ) {
        return 0 ;
    } ;
    protected int getSignalLevel( int[] inputSignal, int calcOut, BlockState state ) {
        return getSignalLevel( inputSignal, calcOut | ( state.getValue( SIGNAL_IO ) & this.bitIn ), state.getValue( SIGNAL_LEVEL ) ) ;
    } ;
    /** 世界获取信号时会触发这个方法
     *
     * 输入与输出 传入的数组长度总是4，顺序请参考 directionMapping
     * 这个方法通常用来进行逻辑计算，如果需要改变输出强度，请在触发更新时返回一个输出强度
     * 你不必去检查对应方向是否开启了输出，因为若未启用输出接口，那么不会触发到该方法
     *
     * @param side   当前输入方向（绝对）
     * @param blockIO     四周的输入输出状态
     * @param blockLevel    当前方块的信号强度
     * @return   输出强度
     */
    public int onWorldGetSignal ( int side, int blockIO, int blockLevel ) {
        // 默认返回当前方块的信号强度
        // 改改，从世界层接管
        return blockLevel ;
    } ;

    protected int getInputSignalFromDirection( World world, BlockPos pos, Direction facing, Direction side ) {
        // 允许输入
        if ( comConfig.canInput( side ) ) {
            // 取得绝对方向
            Direction ad = getAbsoluteDirection( facing, side ) ;
            return world.getSignal( pos.relative( ad ), ad ) ; // 获得方向的方块，从哪儿获得的？
        } ;
        return 0 ;
    } ;
    /** 获取四周信号强度
     *
     * @param world   世界
     * @param pos     坐标
     * @return   信号强度
     */
    protected int[] getInputSignalFromAll( World world, BlockState state, BlockPos pos ) {

        int facing = directionMapping( state.getValue( FACING ) ) ;
        int[] ret = new int[4] ;
        // 获得世界方向上的开关
        for ( int i = 0 ; i < 4 ; i++ ) {
            int m = ( i +facing ) & 0b11 ;
            if ( this.comConfig.canInput( i ) ) {
                ret[i] = world.getSignal( pos.relative( directionMapping( m ) ), directionMapping( m ) ) ;
            } ;
        }
        return ret ;
    } ;

    @Override
    protected void createBlockStateDefinition( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( FACING ).add( SIGNAL_IO, SIGNAL_LEVEL) ;
        super.createBlockStateDefinition( builder ) ;
    }
    @Override
    protected int getDelay( BlockState state ) {
        return 2 ;
    }
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return this.defaultBlockState( ).setValue( FACING , context.getHorizontalDirection( ) ) ;
    }
    @Override
    public @Nonnull List<ItemStack> getDrops( BlockState state, LootContext.Builder builder ) {
        final List<ItemStack> drops = new ArrayList<>() ;
        drops.add( new ItemStack( this.asItem( ) , 1 ) ) ;
        return drops ;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void appendHoverText( ItemStack stack, @Nullable IBlockReader reader, List<ITextComponent> list, ITooltipFlag flag ) {
        list.add( new TranslationTextComponent( "info." + RedstoneGate.ID +"." +this.comConfig.getName( ) ).withStyle( TextFormatting.GRAY ) ) ;
        list.add( new StringTextComponent( "" ) ) ;
        list.add( new TranslationTextComponent( "io." +RedstoneGate.ID ).withStyle( TextFormatting.DARK_GRAY ) );
        list.add( new TranslationTextComponent( "io." +RedstoneGate.ID +".gray" ) );
        list.add( new TranslationTextComponent( "io." +RedstoneGate.ID +".blue" ) );
    }

}
