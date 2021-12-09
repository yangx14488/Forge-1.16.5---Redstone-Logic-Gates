package net.atcat.nanzhi.redstone_gate.function;

import net.atcat.nanzhi.redstone_gate.RedstoneGate;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber( modid = RedstoneGate.ID, bus = Mod.EventBusSubscriber.Bus.FORGE )
public class ItemEntityRecipe {

    private static int OFFSET = 0 ;
    // 受记录的掉落表
    private static final List<List<ItemEntity>> DROPITEM_LIST = new ArrayList<>( ) ;
    // 受记录的不可破坏的表
    private static final List<List<InvulnerableObj>> INVULNERABLE_LIST = new ArrayList<>( ) ;
    private static final int TICK = 10 ; // 每秒处理20个数组( 这mc有什么大病，server tick是40tick/s？ )
    public static final Map<Item,Item> LAVA_RECIPE = new HashMap<>( ) ;
    public static final Map<Item,Item> WATER_RECIPE = new HashMap<>( ) ;
    public static final Map<Item,Item> CONCRETE_MAPPING = new HashMap<>( ) ;
    private static final RecipeCalc RC_EMPTY = ie -> { } ;
    private static final RecipeCalc RC_LAVA = ie -> {
        List<InvulnerableObj> list = getMinList( INVULNERABLE_LIST ) ;
        list.add( new InvulnerableObj( ie, 60 *2 ) ) ; // 记录到主表
        ie.setInvulnerable( true ) ; // 确认无敌
    } ;

    static {
        int i = TICK ;
        while ( i --> 0 ) {
            DROPITEM_LIST.add( new ArrayList<>( ) ) ; // 添加数组
            INVULNERABLE_LIST.add( new ArrayList<>( ) ) ; // 添加存储容器
        } ;

        CONCRETE_MAPPING.put( Items.WHITE_CONCRETE_POWDER, Items.WHITE_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.ORANGE_CONCRETE_POWDER, Items.ORANGE_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.MAGENTA_CONCRETE_POWDER, Items.MAGENTA_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.LIGHT_BLUE_CONCRETE_POWDER, Items.LIGHT_BLUE_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.YELLOW_CONCRETE_POWDER, Items.YELLOW_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.LIME_CONCRETE_POWDER, Items.LIME_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.PINK_CONCRETE_POWDER, Items.PINK_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.GRAY_CONCRETE_POWDER, Items.GRAY_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.LIGHT_GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.CYAN_CONCRETE_POWDER, Items.CYAN_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.PURPLE_CONCRETE_POWDER, Items.PURPLE_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.BLUE_CONCRETE_POWDER, Items.BLUE_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.BROWN_CONCRETE_POWDER, Items.BROWN_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.GREEN_CONCRETE_POWDER, Items.GREEN_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.RED_CONCRETE_POWDER, Items.RED_CONCRETE ) ;
        CONCRETE_MAPPING.put( Items.BLACK_CONCRETE_POWDER, Items.BLACK_CONCRETE ) ;

        WATER_RECIPE.put( Items.BUCKET, Items.WATER_BUCKET ) ;

        LAVA_RECIPE.put( Items.SAND, Items.GLASS ) ;
        LAVA_RECIPE.put( Items.RED_SAND, Items.GLASS ) ;
        LAVA_RECIPE.put( Items.GRAVEL, Items.FLINT ) ;

    } ;

    private static class InvulnerableObj {
        public final ItemEntity ie ;
        public int time ;
        public InvulnerableObj ( ItemEntity ie, int time ) {
            this.ie = ie ;
            this.time = time ;
        }
    } ;

    protected static <T> List<T> getMinList( List<List<T>> sto ) { // 总是会把元素往目前存储最少的里添加
        List<T> list = sto.get( 0 ) ;
        for ( int i = 1; i < sto.size( ) ; i++ ) {
            if ( sto.get( i ).size( ) < list.size( ) ) {
                list = sto.get( i ) ;
            } ;
        } ;
        return list ;
    } ;

    public static boolean isConcretePowderBlock ( Item item ) {
        return item instanceof BlockItem && ( (BlockItem) item ).getBlock( ) instanceof ConcretePowderBlock ;
    } ;

    public static boolean Matched ( Item item ) { // 不处理不需要记录的，例如岩浆
        return isConcretePowderBlock( item ) || WATER_RECIPE.containsKey( item ) ;
    } ;

    interface RecipeCalc {
        void apply ( ItemEntity entity ) ;
    } ;


    public static boolean SpawnRecipeItem ( Map<Item,Item> map, ItemEntity entity ) {
        return SpawnRecipeItem( map, entity, RC_EMPTY ) ;
    }

    public static boolean SpawnRecipeItem ( Map<Item,Item> map, ItemEntity entity, RecipeCalc calc ) {
        ItemStack stackIn = entity.getItem( ) ;
        Item itemOut = map.get( stackIn.getItem( ) ) ;
        if ( itemOut != null ) { // 从地图中获取到了配方
            int mss = itemOut.getMaxStackSize( ) ;
            int success = 0 ;
            // 分组添加
            for ( int count = stackIn.getCount( ) ; count > 0 ; count -= mss ) {
                // 复制数据
                ItemStack stackOut = new ItemStack( itemOut, Math.min( count, mss ) ) ;
                if ( stackIn.hasTag( ) )
                    stackOut.setTag( stackIn.getTag( ) ) ;
                ItemEntity ie = new ItemEntity( entity.level, entity.getX( ), entity.getY( ), entity.getZ( ), stackOut ) ;
                // 实例添加与检测
                if ( entity.level.addFreshEntity( ie ) ) {
                    calc.apply( ie ) ;
                    success += mss ;
                } ;
            }
            // 物品保护
            if ( success >= stackIn.getCount( ) ) {
                entity.remove( ) ; // 销毁
            } else {
                ItemStack stackOut = new ItemStack( itemOut, stackIn.getCount( ) - success ) ;
                if ( stackIn.hasTag( ) ) stackOut.setTag( stackIn.getTag( ) ) ;
                entity.setItem( stackOut ) ; // 追加
                calc.apply( entity ) ;
            } ;
            return true ;
        } ;
        return false ;
    } ;

    /*
    掉落处理
    */
    @SubscribeEvent
    public static void dropItem ( EntityJoinWorldEvent event ) {
        Entity entity = event.getEntity( ) ;
        // 只处理服务器
        if ( !event.getWorld( ).isClientSide( ) && entity instanceof ItemEntity ) {
            ItemEntity ie = (ItemEntity) entity ;
            // 匹配检测
            if ( Matched( ie.getItem( ).getItem( ) ) ) {
                getMinList( DROPITEM_LIST ).add( ie ) ;
            } ;
        } ;
    } ;

    // 清除物品的不可破坏状态
    protected static void shrinkInvulnerable ( int offset ) {
        List<InvulnerableObj> map = INVULNERABLE_LIST.get( offset ) ;
        for ( int i = 0 ; i < map.size( ) ; i++ ) {
            InvulnerableObj ioe = map.get( i ) ;
            ioe.time -= TICK ;
            if ( ioe.time <= 0 || !ioe.ie.isAlive( ) ) {
                if ( ioe.ie.isAlive( ) ) ioe.ie.setInvulnerable( false ) ;
                map.remove( i-- ) ; // 移除
            } ;
        } ;
    } ;
    // 计算匹配的掉落物
    protected static void calcDropItem ( int offset ) {
        List<ItemEntity> list = DROPITEM_LIST.get( offset ) ;
        for ( int index = 0 ; index < list.size( ) ; index++ ) {
            boolean remove = false ;
            ItemEntity entity = list.get( index ) ;
            Item item = entity.getItem( ).getItem( ) ;
            // 只应该处理还存在的元素
            if ( entity.isAlive( ) ) {
                // 在水里
                if ( entity.isInWater( ) ) {
                    if ( isConcretePowderBlock( item ) ) { // 检测是否为混凝土（节省算力）
                        if ( SpawnRecipeItem( CONCRETE_MAPPING, entity ) ) {
                            remove = true ;
                        } ;
                    } else { // 查询湿润配方
                        if ( SpawnRecipeItem( WATER_RECIPE, entity ) ) { // 查询集合A与集合B的并集，应考虑集合C与集合B的元素不一定重复的情况，所以未查询到时不应该移除
                            remove = true ;
                        } ;
                    } ;
                } ;
            } else {
                remove = true ;
            } ;
            if ( remove ) {
                list.remove( index-- ) ;
            } ;
        } ;
    } ;

    /*
    流体处理：水
    */
    @SubscribeEvent
    public static void ServerTick( TickEvent.ServerTickEvent event ) {
        // 应该加个遍历
        // 只处理服务器INVULNERABLE_LIST的代码，结构模拟上面的配方，每秒遍历指定数量的内容，被遍历3次的(2,3]的物品会被删除
        if ( event.side.isServer( ) ) {
            // 这里的代码用于清除物品的不可破坏状态
            shrinkInvulnerable( OFFSET ) ;
            calcDropItem( OFFSET ) ;
            if ( ++OFFSET >= TICK ) OFFSET = 0 ;
        } ;
    } ;

    // 处理熔岩
    @SubscribeEvent
    public static void ItemEntityDamage ( EntityLeaveWorldEvent event ) {
        Entity entity = event.getEntity( ) ;
        // 非客户端且是物品
        if ( !event.getWorld( ).isClientSide( ) && entity instanceof ItemEntity ) {
            // 不在表里，物品是不是活着无所谓
            ItemEntity ie = (ItemEntity) entity ;
            if ( ie.isOnFire( ) ) { // 想办法改改逻辑，依旧检测岩浆，看看有没有添加火的事件
                SpawnRecipeItem( LAVA_RECIPE, ie, RC_LAVA ) ;
            } ;
        } ;
    } ;
}
