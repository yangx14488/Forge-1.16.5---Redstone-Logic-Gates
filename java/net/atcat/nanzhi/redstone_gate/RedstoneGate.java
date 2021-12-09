package net.atcat.nanzhi.redstone_gate;

import net.atcat.nanzhi.redstone_gate.com.ConfigFactory;
import net.atcat.nanzhi.redstone_gate.com.Registry;
import net.atcat.nanzhi.redstone_gate.pack.Packs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;


@Mod( RedstoneGate.ID )
@Mod.EventBusSubscriber( modid = RedstoneGate.ID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class RedstoneGate {

    // 根ID
    public static final String ID = "redstone_gate" ;
    // 日志
    public static final Logger logger = NZCore.logger ;
    // 配置
    // public static final ConfigFactory configFactory = new ConfigFactory( "ImBoxMC", RedstoneGate.ID ) ;
    // 根注册器
    public static final Registry registry = new Registry( RedstoneGate.ID ) ;

    // 总线
    protected final IEventBus bus ;

    public RedstoneGate( ) {

        // 注册配置文件
        // ModLoadingContext.get( ).registerConfig( ModConfig.Type.COMMON, configFactory.build( ) ) ;
        // 取得总线
        this.bus = FMLJavaModLoadingContext.get( ).getModEventBus( ) ;

        // 触发静态类加载
        Im3Sounds.load( ) ;
        /*
                Im3Sounds.load( ) ; // 声音

        Disease.loaded( bus ) ; // 疾病

        // 注册配置
        ModLoadingContext.getDirectionSwitch( ).registerConfig( ModConfig.Type.COMMON, configFactory.build( ) ) ;
        // 注册可视化配置
        /*
        ModLoadingContext.getDirectionSwitch( ).registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY,
                ( ) -> ( mc, screen ) -> configFactory.GUI( mc, screen )
        ) ;
         */

        if ( Packs.load( bus ) ) {
            logger.info( "[nz core] Packs is loaded" ) ;
        } else {
            logger.fatal( "[nz core] Packs is locked", new LoaderException( "加载包的数据锁是生效的状态。请检查是否在其他地方调用过方法 <java>.pack.Packs.load( IEventBus bus ) " ) ) ;
        } ;

    } ;


}
