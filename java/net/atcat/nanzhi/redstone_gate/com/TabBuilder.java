package net.atcat.nanzhi.redstone_gate.com;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class TabBuilder extends ItemGroup {
    private final Supplier<Item> icon ;
    /** 构建创造模式物品栏
     * @param label   表名
     * @param icon    lambda方法，要求返回一个物品作为图标
     *
     * 要是把传入的lambda替换为方法引用，你就知道 null 这四个字符是怎么写的了
     * */
    public TabBuilder( String label, Supplier<Item> icon ) {
        super( label ) ;
        this.icon = icon ;
    }

    @Override
    public ItemStack makeIcon ( ) {
        return new ItemStack( this.icon.get( ) ) ;
    }
}
