package net.atcat.nanzhi.redstone_gate.pack.redstone;

import net.atcat.nanzhi.redstone_gate.com.Registry;
import net.atcat.nanzhi.redstone_gate.pack.Pack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Redstone implements Pack {

    public static final String ID = "redstone" ;
    public static final Registry REG = Pack.getRegistry( ID ) ;

    public Redstone ( ) {

        RSBlocks.load( ) ;
        RSItems.load( ) ;

    } ;


    @Nonnull
    public String getID( ) {
        return ID ;
    }

    @Nullable
    public Registry getRegistry( ) {
        return REG ;
    }

}
