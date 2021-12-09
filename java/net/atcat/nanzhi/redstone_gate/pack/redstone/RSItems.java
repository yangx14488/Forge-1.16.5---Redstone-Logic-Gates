package net.atcat.nanzhi.redstone_gate.pack.redstone;

import net.atcat.nanzhi.redstone_gate.pack.redstone.item.ComBoard;
import net.minecraft.item.Item;

public class RSItems {

    public static void load( ) { } ;

    public static Item com_board = Redstone.REG.register( "com_board", new ComBoard( ) ) ;
}
