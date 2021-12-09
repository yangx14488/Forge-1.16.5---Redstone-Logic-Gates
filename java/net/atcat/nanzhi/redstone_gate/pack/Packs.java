package net.atcat.nanzhi.redstone_gate.pack;

import net.atcat.nanzhi.redstone_gate.pack.redstone.Redstone;
import net.minecraftforge.eventbus.api.IEventBus;

public class Packs {

    // 包管理器
    public static final PackManage PM = new PackManage( ) ;

    public static final Pack Redstone = PM.instantiatePack( new Redstone( ) ) ;

    protected static boolean LOCKED = false ;

    public static boolean loaded ( ) {
        return LOCKED;
    } ;
    public static boolean load ( IEventBus bus ) {
        if ( !LOCKED ) {
            PM.loadPacks( bus ) ;
            return LOCKED = true ;
        } ;
        return false ;
    } ;

}
