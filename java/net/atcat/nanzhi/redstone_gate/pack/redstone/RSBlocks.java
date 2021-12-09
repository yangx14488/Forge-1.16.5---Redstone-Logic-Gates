package net.atcat.nanzhi.redstone_gate.pack.redstone;

import net.atcat.nanzhi.redstone_gate.pack.redstone.block.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;

public class RSBlocks {

    public static void load( ) { } ;

    public static Block and_gate = Redstone.REG.register( "and", new ANDgate( ), ItemGroup.TAB_REDSTONE ) ;
    public static Block xor_gate = Redstone.REG.register( "xor", new XORgate( ), ItemGroup.TAB_REDSTONE ) ;
    public static Block not_gate = Redstone.REG.register( "not", new NOTgate( ), ItemGroup.TAB_REDSTONE ) ;
    public static Block or_gate = Redstone.REG.register( "or", new ORgate( ), ItemGroup.TAB_REDSTONE ) ;

    public static Block rs_trigger = Redstone.REG.register( "rs_trigger", new RStrigger( ), ItemGroup.TAB_REDSTONE ) ;
    public static Block d_flipflop = Redstone.REG.register( "d_flipflop", new Dflipflop( ), ItemGroup.TAB_REDSTONE ) ;
    public static Block t_trigger = Redstone.REG.register( "t_trigger", new Ttrigger( ), ItemGroup.TAB_REDSTONE ) ;

    public static Block counter = Redstone.REG.register( "counter", new Counter( ), ItemGroup.TAB_REDSTONE ) ;
    public static Block slide_rheostat = Redstone.REG.register( "slide_rheostat", new SlideRheostat( ), ItemGroup.TAB_REDSTONE ) ;

}
