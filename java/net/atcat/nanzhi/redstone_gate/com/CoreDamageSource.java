package net.atcat.nanzhi.redstone_gate.com;

import net.minecraft.util.DamageSource;

public class CoreDamageSource {

    // 切割伤，会损坏盔甲
    public static final DamageSource CUTTING = ( new DamageSource("cutting" ) ) ;
    // 穿刺伤
    public static final DamageSource PENETRATING = ( new DamageSource("penetrating" ) ) ;
    // 撕裂伤
    public static final DamageSource LACERATION = ( new DamageSource("laceration" ) ).bypassArmor( ) ;
    // 神经损伤
    public static final DamageSource NERVE = ( new DamageSource("nerve" ) ).bypassArmor( ) ;
    // 感染
    public static final DamageSource INFECTION = ( new DamageSource("infection" ) ).bypassArmor( ) ;
    // 失血
    public static final DamageSource HEMORRHAGE = ( new DamageSource("hemorrhage" ) ).bypassArmor( ) ;

}
