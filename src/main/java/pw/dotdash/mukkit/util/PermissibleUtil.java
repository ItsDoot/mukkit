package pw.dotdash.mukkit.util;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.ServerOperator;

public final class PermissibleUtil {

    private static final PermissibleBase DEFAULT_ENTITY = new PermissibleBase(new ServerOperator() {
        @Override
        public boolean isOp() {
            return false;
        }

        @Override
        public void setOp(boolean value) {

        }
    });

    public static Permissible getDefaultEntity() {
        return DEFAULT_ENTITY;
    }

    private PermissibleUtil() {
    }
}