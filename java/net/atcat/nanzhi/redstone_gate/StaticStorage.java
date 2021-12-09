package net.atcat.nanzhi.redstone_gate;

import net.atcat.nanzhi.redstone_gate.com.Registry;

import java.util.ArrayList;
import java.util.List;

/**
 * RegArr : Registry类使用该存储器，用于某些类获取Registry所注册的内容
 * 这个核心最好打包做成lib库，在模组有需要的时候释放这个库文件到模组文件夹然后重启游戏。。？
 *
 * */
public class StaticStorage {
    public static final List<Registry> RegArr = new ArrayList<>() ;
}
