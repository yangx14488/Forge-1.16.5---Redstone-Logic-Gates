package net.atcat.nanzhi.redstone_gate.com;

import net.atcat.nanzhi.redstone_gate.StaticStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.function.Supplier;

/** 方块注册类
 * 用法：
 * getRegistry = new Registry( <模组ID> )  // 创建注册器的实例
 * getRegistry.register( ... )  // 注册方块、物品等
 * getRegistry.register( getBus ) // 注册到总线
 *
 * */
public class Registry {

    private String _dn( String name ) {
        return this.domain == null ? name : this.domain + "/" + name ;
    }

    private final String id ;
    private final String domain ;
    private final ArrayList<DeferredRegister<?>> defArr = new ArrayList<>() ;
    private boolean REG = false ;

    public final DeferredRegister<Item> itemReg ;
    public final DeferredRegister<Block> blockReg ;
    public final DeferredRegister<Effect> effectReg ;
    public final DeferredRegister<Attribute> attributeReg ;
    public final DeferredRegister<SoundEvent> soundReg ;
    public final DeferredRegister<TileEntityType<?>> tileEntityReg ;
    /**
     * @param modID 模组ID
     * */
    public Registry( String modID ) {
        this( modID, null ) ;
    }
    public Registry( String modID, String domain ) {
        this.id = modID ;
        this.domain = domain ;
        this.itemReg = createDef( ForgeRegistries.ITEMS ) ;
        this.blockReg = createDef( ForgeRegistries.BLOCKS ) ;
        this.effectReg = createDef( ForgeRegistries.POTIONS ) ;
        this.soundReg = createDef( ForgeRegistries.SOUND_EVENTS ) ;
        this.attributeReg = createDef( ForgeRegistries.ATTRIBUTES ) ;
        this.tileEntityReg = createDef( ForgeRegistries.TILE_ENTITIES ) ;
    }

    /** 用于创建注册器，注册器会添加到defArr里以供注册到总线
     * @param reg 注册类型
     * */
    private <T extends IForgeRegistryEntry<T>> DeferredRegister<T> createDef( IForgeRegistry<T> reg ) {
        DeferredRegister<T> r = DeferredRegister.create( reg, id ) ;
        this.defArr.add( r ) ;
        return r ;
    }

    /** 物品注册
     * @param name 物品ID
     * @param item 物品实例
     * @return 物品
     * */
    public Item register( String name, Item item) {
        itemReg.register( _dn( name ), ( ) -> item );
        return item ;
    }
    /** 方块注册
     * @param name 物品ID
     * @param block 方块实例
     * @param properties 物品属性
     * @return 方块
     * */
    public Block register( String name, Block block, Item.Properties properties ) {
        blockReg.register( _dn( name ), ( ) -> block ) ;
        itemReg.register( _dn( name ), ( ) -> new BlockItem( block, properties ) );
        return block ;
    }
    /** @param BlockItemIn 方块物品 */
    public Block register( String name, BlockItem BlockItemIn ) {
        blockReg.register( _dn( name ), BlockItemIn::getBlock ) ;
        itemReg.register( _dn( name ), ( ) -> BlockItemIn );
        return BlockItemIn.getBlock( ) ;
    }
    /** @param group 方块被分配的组 */
    public Block register( String name, Block block, ItemGroup group ) {
        blockReg.register( _dn( name ), ( ) -> block ) ;
        itemReg.register( _dn( name ), ( ) -> new BlockItem( block, new Item.Properties( ).tab( group ) ) );
        return block ;
    }
    /** 效果注册
     * @param name 物品ID
     * @param effectIn 效果
     * @return 效果
     * */
    public Effect register( String name, Effect effectIn ) {
        effectReg.register( _dn( name ), ( ) -> effectIn ) ;
        return effectIn ;
    }
    /** 属性注册
     * @param name 物品ID
     * @param attr 属性
     * @return 属性
     * */
    public Attribute register( String name, Attribute attr ) {
        attributeReg.register( _dn( name ), ( ) -> attr ) ;
        return attr ;
    }
    /** 声音注册
     * @param name 声音id
     * @param sound 声音
     * @return 属性
     * */
    public SoundEvent register( String name, SoundEvent sound ) {
        soundReg.register( _dn( name ), ( ) -> sound ) ;
        return sound ;
    }
    public SoundEvent soundRegister( String name ) {
        return register( name, new SoundEvent( new ResourceLocation( this.id, name ) ) ) ;
    }
    /** 方块实体
     * @param name 物品ID
     * @param ts lambda表达式，要求返回一个方块实体的实例
     * @param blockIn 方块
     * @return 方块实体类型
     * */
    public <T extends TileEntity> TileEntityType<T> register ( String name, Supplier<T> ts, Block blockIn ) {
        TileEntityType<T> te = TileEntityType.Builder.of( ts, blockIn ).build( Util.fetchChoiceType( TypeReferences.BLOCK_ENTITY, _dn( name ) ) ) ;
        tileEntityReg.register( _dn( name ), ( ) -> te ) ;
        return te ;
    }

    /** 注册
     * @param bus MC总线
     * */
    public void regBus( IEventBus bus ) {
        if ( !REG ) {
            REG = true ;
            StaticStorage.RegArr.add( this ) ;
            for ( DeferredRegister<?> def : defArr ) {
                def.register( bus ) ;
            }
        }
    }

}
