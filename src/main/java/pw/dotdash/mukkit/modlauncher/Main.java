package pw.dotdash.mukkit.modlauncher;

import cpw.mods.modlauncher.Launcher;
import pw.dotdash.mukkit.modlauncher.util.ArgumentList;

public class Main {

    public static void main(String[] args) {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

        Launcher.main(ArgumentList.from(args).getArguments());
    }
}