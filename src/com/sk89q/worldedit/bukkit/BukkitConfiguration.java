// $Id$
/*
 * WorldEdit
 * Copyright (C) 2010 sk89q <http://www.sk89q.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.sk89q.worldedit.bukkit;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.util.config.Configuration;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LogFormat;
import com.sk89q.worldedit.snapshots.SnapshotRepository;

public class BukkitConfiguration extends LocalConfiguration {
    private Configuration config;
    private Logger logger;
    
    public BukkitConfiguration(Configuration config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    @Override
    public void load() {
        profile = config.getBoolean("debug", profile);
        wandItem = config.getInt("wand-item", wandItem);
        defaultChangeLimit = Math.max(-1, config.getInt(
                "limits.max-blocks-changed.default", defaultChangeLimit));
        maxChangeLimit = Math.max(-1,
                config.getInt("limits.max-blocks-changed.maximum", maxChangeLimit));
        maxRadius = Math.max(-1, config.getInt("limits.max-radius", maxRadius));
        maxSuperPickaxeSize = Math.max(1, config.getInt(
                "limits.max-super-pickaxe-size", maxSuperPickaxeSize));
        registerHelp = true;
        logComands = config.getBoolean("logging.log-commands", logComands);
        superPickaxeDrop = config.getBoolean("super-pickaxe.drop-items",
                superPickaxeDrop);
        superPickaxeManyDrop = config.getBoolean(
                "super-pickaxe.many-drop-items", superPickaxeManyDrop);
        noDoubleSlash = config.getBoolean("no-double-slash", noDoubleSlash);
        useInventory = config.getBoolean("use-inventory.enable", useInventory);
        useInventoryOverride = config.getBoolean("use-inventory.allow-override",
                useInventoryOverride);
        maxBrushRadius = config.getInt("limits.max-brush-radius", maxBrushRadius);
        
        navigationWand = config.getInt("navigation-wand.item", navigationWand);
        navigationWandMaxDistance = config.getInt("navigation-wand.max-distance", navigationWandMaxDistance);

        scriptTimeout = config.getInt("scripting.timeout", scriptTimeout);
        
        disallowedBlocks = new HashSet<Integer>(config.getIntList("limits.disallowed-blocks", null));

        String snapshotsDir = config.getString("snapshots.directory", "");
        if (!snapshotsDir.trim().equals("")) {
            snapshotRepo = new SnapshotRepository(snapshotsDir);
        } else {
            snapshotRepo = null;
        }

        String type = config.getString("shell-save-type", "").trim();
        shellSaveType = type.equals("") ? null : type;

        String logFile = config.getString("logging.file", "");
        if (!logFile.equals("")) {
            try {
                FileHandler handler = new FileHandler(logFile, true);
                handler.setFormatter(new LogFormat());
                logger.addHandler(handler);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not use log file " + logFile + ": "
                        + e.getMessage());
            }
        } else {
            for (Handler handler : logger.getHandlers()) {
                logger.removeHandler(handler);
            }
        }
    }
}
