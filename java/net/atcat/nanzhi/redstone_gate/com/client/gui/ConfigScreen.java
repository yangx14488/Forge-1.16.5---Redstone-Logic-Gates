package net.atcat.nanzhi.redstone_gate.com.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.atcat.nanzhi.redstone_gate.NZCore;
import net.atcat.nanzhi.redstone_gate.com.ConfigFactory;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class ConfigScreen extends Screen {

  private static final int OPTIONS_LIST_TOP_HEIGHT = 24 ;
  private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32 ;
  private static final int OPTIONS_LIST_ITEM_HEIGHT = 25 ;

  private static final int BUTTON_WIDTH = 200 ;
  private static final int BUTTON_HEIGHT = 20 ;
  private static final int DONE_BUTTON_TOP_OFFSET = 26 ;

  private OptionsRowList optionsRowList ;
  private final Screen parentScreen ;
  private final Minecraft mc ;
  private final ConfigFactory factory ;


  public ConfigScreen ( Minecraft mc, Screen parentScreen, ConfigFactory factory) {
    this( mc, parentScreen, factory, "config." + factory.modID + ".title"  ) ;
  }
  public ConfigScreen ( Minecraft mc, Screen parentScreen, ConfigFactory factory, String titleI18n ) {
    super( new TranslationTextComponent( titleI18n ) );
    this.parentScreen = parentScreen ;
    this.mc = mc ;
    this.factory = factory ;
  }

  @Override
  protected void init( ) {

    // 同步配置
    factory.sync( false ) ;
    // 准备配置列表
    this.optionsRowList = new OptionsRowList( this.mc, this.width, this.height, OPTIONS_LIST_TOP_HEIGHT, this.height - OPTIONS_LIST_BOTTOM_OFFSET, OPTIONS_LIST_ITEM_HEIGHT ) ;
    // 将列表写入到配置
    writeOptions( this.optionsRowList, factory.optList ) ;
    
    this.children.add( this.optionsRowList ) ;
    
    this.addButton( new Button(
        ( this.width - BUTTON_WIDTH ) / 2,
        this.height - DONE_BUTTON_TOP_OFFSET,
        BUTTON_WIDTH, BUTTON_HEIGHT,
        new TranslationTextComponent("gui.done" ),
        button -> this.onClose( )
    ) ) ;
    
    super.init( ) ;
    
  }
  
  @Override
  public void render ( MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {

    this.renderBackground( matrixStack ) ;

    this.optionsRowList.render( matrixStack, mouseX, mouseY, partialTicks ) ;

    drawCenteredString( matrixStack, this.font, this.title.getString( ), this.width / 2, 8, 0xFFFFFF ) ;
    super.render( matrixStack, mouseX, mouseY, partialTicks ) ;
  }

  @Override
  public void removed( ) {
    factory.sync( true ).save( ) ;
  }

  @Override
  public void onClose( ) {
    this.mc.setScreen( this.parentScreen ) ;
  }


  private static String getI18n ( String i18n, String def ) {
    return i18n != null ? I18n.get( i18n ) : def ;
  }


  private static AbstractOption UnitBoolOpt( ConfigFactory.OptionBuilder<Boolean> opt ) {
    return new BooleanOption(
            getI18n( opt.conf.i18n, opt.conf.path ) ,
            a -> opt.get( ) ,
            ( a, b ) -> opt.set( b )
    ) ;
  }

  private static AbstractOption UnitIntOpt( ConfigFactory.OptionBuilder<Integer> opt ) {
    return new SliderPercentageOption(
            getI18n( opt.conf.i18n, opt.conf.path ) ,
            (double) opt.conf.min ,
            (double) opt.conf.max ,
            (float) opt.conf.step ,
            gameSettings -> (double) opt.get( ) ,
            ( gs, val ) -> opt.set( val.intValue( ) ) ,
            ( gs, spo ) -> new StringTextComponent( getI18n( opt.conf.i18n, opt.conf.path ) + ": " + spo.get( gs ) )
    ) ;
  }

  private static AbstractOption UnitPreOpt( ConfigFactory.OptionBuilder<Integer> opt ) {
    return new SliderPercentageOption(
            getI18n( opt.conf.i18n, opt.conf.path ) ,
            (double) opt.conf.min ,
            (double) opt.conf.max ,
            (float) opt.conf.step ,
            gameSettings -> (double) opt.get( ) ,
            ( gs, val ) -> opt.set( val.intValue( ) ) ,
            ( gs, spo ) -> new StringTextComponent( getI18n( opt.conf.i18n, opt.conf.path ) + ": " + spo.get( gs ) + "%" )
    ) ;
  }

  private static AbstractOption UnitDoubleOpt( ConfigFactory.OptionBuilder<Double> opt ) {
    return new SliderPercentageOption(
            getI18n( opt.conf.i18n, opt.conf.path ) ,
            opt.conf.min ,
            opt.conf.max ,
            opt.conf.step.floatValue( ) ,
            gameSettings -> opt.get( ) ,
            ( gs, val ) -> opt.set( val ) ,
            ( gs, spo ) -> new StringTextComponent( getI18n( opt.conf.i18n, opt.conf.path ) + ": " + spo.get( gs ) )
    ) ;
  }

  public static AbstractOption putToRow ( ConfigFactory.OptionBuilder<?> opt ) {
    AbstractOption ret =
            opt instanceof ConfigFactory.Bool
            ? UnitBoolOpt( (ConfigFactory.Bool) opt )
            : opt instanceof ConfigFactory.Int
              ? UnitIntOpt( (ConfigFactory.Int) opt )
              : opt instanceof ConfigFactory.Double
                ? UnitDoubleOpt( (ConfigFactory.Double) opt )
                : opt instanceof ConfigFactory.Percentage
                  ? UnitPreOpt( (ConfigFactory.Percentage) opt )
                  : null ;
    if ( ret == null ) {
      NZCore.logger.error( "Nanzhi_core internal error: unknown configuration type", new Exception( opt.toString( ) ) ) ;
    }
    return ret ;
  }

  public static void writeOptions ( OptionsRowList optRow, List<ConfigFactory.OptionBuilder<?>> optList ) { // 用于将配置写入表
    for ( int i = 0 ; i < optList.size( ) ; i++ ) {
      if ( i < optList.size( ) - 1 && optList.get( i ).conf.isHalf && optList.get( i +1 ).conf.isHalf ) {
        optRow.addSmall( putToRow( optList.get( i ) ), putToRow( optList.get( ++i ) ) );
      } else {
        optRow.addBig( putToRow( optList.get( i ) ) ) ;
      }
    }
  }

}