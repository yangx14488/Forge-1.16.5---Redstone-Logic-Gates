package net.atcat.nanzhi.redstone_gate.pack;

import net.atcat.nanzhi.redstone_gate.RedstoneGate;
import net.atcat.nanzhi.redstone_gate.NZCore;
import net.atcat.nanzhi.redstone_gate.com.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Pack {

    public static Logger logger = NZCore.logger ;

    @Nonnull
    String getID ( ) ;

    @Nullable
    Registry getRegistry ( ) ;

    // 如果getRegistry有返回值，那么请不要重复注册
    // 该方法会在主类完成加载，准备加载包时调用

    /**
     * 模组加载完自身并准备加载包时，会访问该方法
     * @param event   事件
     * @return boolean   返回 false 时，会取消事件：阻止当前包在后续的一系列加载，例如注册资源( getRegistry( ).regBus( IEventBus ) )等。
     */
    default void onload ( Pack.Event event ) {  } ;

    public static Registry getRegistry( String packID ) {
        return new Registry( RedstoneGate.ID, packID ) ;
    } ;

    public static class Event {
        public final IEventBus bus ;
        public boolean cancelEvent = false ;
        public Event ( IEventBus busIn ) {
            this.bus = busIn ;
        } ;
    } ;


}