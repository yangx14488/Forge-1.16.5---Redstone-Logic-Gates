package net.atcat.nanzhi.redstone_gate.pack;

import net.atcat.nanzhi.redstone_gate.com.Registry;
import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PackManage {

    public final Map<String,Pack> packs = new HashMap<>( ) ;

    // 实例化
    public Pack instantiatePack( @Nonnull Pack pack ) {
        this.packs.put( pack.getID( ), pack ) ;
        return pack ;
    } ;

    // 获取
    @Nullable
    public Pack getPack ( String ID ) {
       return packs.get( ID ) ;
    } ;

    // 加载
    public void loadPacks ( IEventBus bus ) {
        for ( Pack p : this.packs.values( ) ) {
            loadPack( p, new Pack.Event( bus ) ) ;
        } ;
    } ;

    protected static void loadPack ( Pack pack, Pack.Event event ) {
        pack.onload( event ) ;
        if ( !event.cancelEvent ) {
            Registry reg = pack.getRegistry( ) ;
            if ( reg != null ) reg.regBus( event.bus ) ;
        } ;
    } ;

}
