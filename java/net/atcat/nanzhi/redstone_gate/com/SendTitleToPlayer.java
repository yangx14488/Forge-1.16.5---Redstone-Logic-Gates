package net.atcat.nanzhi.redstone_gate.com;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SendTitleToPlayer {

    private static void sendTitlePacket ( STitlePacket.Type type, PlayerEntity player, ITextComponent string ) {
        if ( player instanceof ServerPlayerEntity ) {
            ( (ServerPlayerEntity) player ).connection.send( new STitlePacket( type, string ) ) ;
        } ;
    } ;

    public static void actionbar ( PlayerEntity player, ITextComponent string ) {
        sendTitlePacket( STitlePacket.Type.ACTIONBAR, player, string ) ;
    } ;

    public static void actionbar ( PlayerEntity player, String i18n, Object... args ) {
        sendTitlePacket( STitlePacket.Type.ACTIONBAR, player, new TranslationTextComponent( i18n, args ) ) ;
    } ;

    public static void title ( PlayerEntity player, ITextComponent string ) {
        sendTitlePacket( STitlePacket.Type.TITLE, player, string ) ;
    } ;

    public static void title ( PlayerEntity player, String i18n, Object... args ) {
        sendTitlePacket( STitlePacket.Type.TITLE, player, new TranslationTextComponent( i18n, args ) ) ;
    } ;

    public static void subtitle ( PlayerEntity player, ITextComponent string ) {
        sendTitlePacket( STitlePacket.Type.SUBTITLE, player, string ) ;
    } ;

    public static void subtitle ( PlayerEntity player, String i18n, Object... args ) {
        sendTitlePacket( STitlePacket.Type.SUBTITLE, player, new TranslationTextComponent( i18n, args ) ) ;
    } ;

    public static void time ( PlayerEntity player, int fadeInTime, int stayTimem, int fadeOutTime ) {
        if ( player instanceof ServerPlayerEntity ) {
            ( (ServerPlayerEntity) player ).connection.send( new STitlePacket( fadeInTime, stayTimem, fadeOutTime ) ) ;
        } ;
    } ;

}
