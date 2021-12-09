package net.atcat.nanzhi.redstone_gate.com;

import net.atcat.nanzhi.redstone_gate.NZCore;
import net.atcat.nanzhi.redstone_gate.com.client.gui.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;

public class ConfigFactory {

  // 配置
  public static class OptionConf<T> {
    protected T step ;
    protected T max ;
    protected T min ;
    protected T def;
    protected String path ;
    protected String comment ;
    protected String i18n ;
    protected boolean isHalf = false ;
    public OptionConf<T> min ( T val ) {
      this.min = val ;
      return this ;
    }
    public OptionConf<T> max ( T val ) {
      this.max = val ;
      return this ;
    }
    public OptionConf<T> defaultVale ( T val ) {
      this.def = val ;
      return this ;
    }
    public OptionConf<T> range ( T min, T max ) {
      this.min = min ;
      this.max = max ;
      return this ;
    }
    public OptionConf<T> path ( String val ) {
      this.path = val ;
      return this ;
    }
    public OptionConf<T> comment ( String val ) {
      this.comment = val ;
      return this ;
    }
    public OptionConf<T> i18n ( String val ) {
      this.i18n = val ;
      return this ;
    }
    public OptionConf<T> step (T val ) {
      this.step = val ;
      return this ;
    }
    public OptionConf<T> isHalf ( ) {
      this.isHalf = true ;
      return this ;
    }

  }

  // 配置取出
  public static class OptionConfRO<T> {
    public final T step ;
    public final T max ;
    public final T min ;
    public final boolean isHalf ;
    public String path ;
    public String comment ;
    public String i18n ;
    public OptionConfRO ( OptionConf<T> opt ) {
      this.max = opt.max ;
      this.min = opt.min ;
      this.step = opt.step ;
      this.isHalf = opt.isHalf ;
      this.path = opt.path == null ? "null" : opt.path ;
      this.comment = opt.comment ;
      this.i18n = opt.i18n ;
    }
  }

  // 布尔值设置
  public static class Bool extends OptionBuilder<Boolean> {
    public Bool( ForgeConfigSpec.Builder builder, OptionConf<Boolean> opt ) { super( builder, opt ) ; }
    @Override
    protected Boolean defVal( ) {
      return false ;
    }
    @Override
    protected boolean mmsNotEmpty( ) {
      return false;
    }
    @Override
    protected ForgeConfigSpec.ConfigValue<Boolean> getSetter( ForgeConfigSpec.Builder builder, Boolean defVal ) {
      return builder
              .comment( this.conf.comment )
              .define( this.conf.path, defVal ) ;
    }
  }
  // 数字设置
  public static class Int extends OptionBuilder<Integer> {
    public Int(ForgeConfigSpec.Builder builder, OptionConf<Integer> opt ) { super( builder, opt ) ; }
    @Override
    protected Integer defVal( ) {
      return 0;
    }
    @Override
    protected boolean mmsNotEmpty() {
      return true;
    }
    @Override
    protected ForgeConfigSpec.ConfigValue<Integer> getSetter( ForgeConfigSpec.Builder builder, Integer defVal ) {
      return builder
              .comment( this.conf.comment )
              .defineInRange( this.conf.path, defVal, this.conf.min, this.conf.max ) ;
    }
  }
  // 小数设置
  public static class Double extends OptionBuilder<java.lang.Double> {
    public Double(ForgeConfigSpec.Builder builder, OptionConf<java.lang.Double> opt ) { super( builder, opt ) ; }
    @Override
    protected java.lang.Double defVal( ) {
      return 0d;
    }
    @Override
    protected boolean mmsNotEmpty() {
      return true;
    }
    @Override
    protected ForgeConfigSpec.ConfigValue<java.lang.Double> getSetter( ForgeConfigSpec.Builder builder, java.lang.Double defVal ) {
      return builder
              .comment( this.conf.comment )
              .defineInRange( this.conf.path, defVal, this.conf.min, this.conf.max ) ;
    }
  }
  // 百分比设置
  public static class Percentage extends OptionBuilder<Integer> {
    public Percentage( ForgeConfigSpec.Builder builder, OptionConf<Integer> opt ) {
      super( builder, opt.min( 0 ).max( 100 ).step( Math.max( 0, Math.min( opt.step == null ? 1 : opt.step, 100 ) ) ) ) ;
    }
    @Override
    protected Integer defVal( ) {
      return 0 ;
    }
    @Override
    protected boolean mmsNotEmpty() {
      return false;
    }
    @Override
    protected ForgeConfigSpec.ConfigValue<Integer> getSetter( ForgeConfigSpec.Builder builder, Integer defVal ) {

      return builder
              .comment( this.conf.comment )
              .defineInRange( this.conf.path, defVal, 0, 100 ) ;
    }
  }
  // 用于构建设置的类
  public abstract static class OptionBuilder<T> {

    protected ForgeConfigSpec.ConfigValue<T> setter ;
    protected T val ;

    public final OptionConfRO<T> conf ;

    protected OptionBuilder ( ForgeConfigSpec.Builder builder, OptionConf<T> opt ) {
      if ( this.mmsNotEmpty( ) && ( opt.max == null || opt.min == null || opt.step == null ) ) {
        throw new ExceptionInInitializerError( "need to setDirectionSwitch parameters for some of the option: min, max, step" ) ;
      } ;
      if ( opt.def == null ) {
        NZCore.logger.warn( "[nanzhi_core] No default value is setDirectionSwitch for this option", new Exception( "> > > It's just a warn. It could be an oversight by one of the dev. You can ignore it. < < <" ) ) ;
        this.val = this.defVal( ) ;
      } else {
        this.val = opt.def ;
      } ;
      this.conf = new OptionConfRO<>( opt ) ;
      this.setter = this.getSetter( builder, this.val ) ;
    }

    public OptionBuilder<T> set ( T val ) {
      this.val = val ;
      return this ;
    }

    public T get ( ) {
      return this.val ;
    }

    public OptionBuilder<T> sync ( boolean toFile ) { // true : 从内存同步到文件，false：从文件同步到内存
      if ( toFile ) {
        this.setter.set( this.val ) ;
      } else {
        this.val = this.setter.get( ) ;
      }
      return this ;
    }

    protected abstract T defVal ( ) ;
    protected abstract boolean mmsNotEmpty  ( ) ;
    protected abstract ForgeConfigSpec.ConfigValue<T> getSetter ( ForgeConfigSpec.Builder builder, T defVal ) ;

  }
  /*
   * 实例代码
   */

  public final String modID ;
  public final ArrayList<OptionBuilder<?>> optList = new ArrayList<>() ;

  private final ForgeConfigSpec.Builder specBuilder;
  private ForgeConfigSpec spec ;

  public ConfigFactory( String comment, String modID ) {
    this.modID = modID ;
    this.specBuilder = new ForgeConfigSpec.Builder( ) ;
    this.specBuilder.comment( comment ).push( modID ) ;
  }

  public ConfigFactory( String modID ) {
    this( "null", modID ) ;
  }

  public ForgeConfigSpec build ( ) { // 构建
    if ( this.spec == null ) {
      this.specBuilder.pop( ) ;
      this.spec = this.specBuilder.build( ) ;
      this.sync( false ) ;
    }
    return this.spec ;
  }

  public Bool addBool ( OptionConf<Boolean> conf ) {
    Bool opt = new Bool( this.specBuilder, conf ) ;
    optList.add( opt ) ;
    return opt ;
  }
  public Bool addBool ( String path, boolean defVal ) {
    return addBool( new OptionConf<Boolean>().path( path ) ) ;
  }

  public Int addInt ( OptionConf<Integer> conf ) {
    Int opt = new Int( this.specBuilder, conf ) ;
    optList.add( opt ) ;
    return opt ;
  }
  public Int addInt ( String path, int defVal, int min, int max, int step ) {
    return addInt( new OptionConf<Integer>().path( path ).defaultVale( defVal ).range( min, max ).step( step ) ) ;
  }

  public Double addDouble ( OptionConf<java.lang.Double> conf ) {
    Double opt = new Double( this.specBuilder, conf ) ;
    optList.add( opt ) ;
    return opt ;
  }
  public Double addDouble ( String path, double defVal, double min, double max, double step ) {
    return addDouble( new OptionConf<java.lang.Double>().path( path ).defaultVale( defVal ).range( min, max ).step( step ) ) ;
  }

  public Percentage addPercentage ( OptionConf<Integer> conf ) {
    Percentage opt = new Percentage( this.specBuilder, conf ) ;
    optList.add( opt ) ;
    return opt ;
  }
  public Percentage addPercentage ( String path, int defVal ) {
    return addPercentage( new OptionConf<Integer>( ).path( path ).defaultVale( defVal ) ) ;
  }

  public ConfigFactory sync( boolean toFile ) { // 同步配置
    for ( OptionBuilder<?> opt : optList ) opt.sync( toFile ) ;
    return this ;
  }

  public ConfigFactory save ( ) { // 存储配置
    this.spec.save( ) ;
    return this ;
  }


  /**
   * 用法：
   * 在你的mod的主类里写这么一句话
   *         ModLoadingContext.getDirectionSwitch( ).registerExtensionPoint(
   *                 ExtensionPoint.CONFIGGUIFACTORY,
   *                 ( ) -> ( mc, screen ) -> config.factory.GUI( mc, screen )
   *         ) ;
   *
   *
   * */
  @OnlyIn( Dist.CLIENT )
  public ConfigScreen GUI( Minecraft mc, Screen screen ) {
    return new ConfigScreen( mc, screen, this ) ;
  } ;


}