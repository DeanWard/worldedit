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

package com.sk89q.bukkit.migration;

import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.util.config.Configuration;

public class PermissionsResolverManager implements PermissionsResolver {
    private Configuration config;
    private Server server;
    private PermissionsResolver perms;
    private String name;
    private Logger logger;
    
    public PermissionsResolverManager(Configuration config, Server server, String name, Logger logger) {
        this.config = config;
        this.server = server;
        this.name = name;
        this.logger = logger;
        
        findResolver();
    }
    
    public void findResolver() {
        if (tryGroupsUsers()) return;
        if (tryNijiPermissions()) return;
        if (tryFlatFilePermissions()) return;
        
        perms = new ConfigurationPermissionsResolver(config);
        logger.info(name + ": No known permissions plugin detected. Using configuration file for permissions.");
    }
    
    private boolean tryGroupsUsers() {
        try {
            perms = new GroupUsersPemissionsResolver(server);
            logger.info(name + ": GroupUsers detected! Using GroupUsers for permissions.");
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    
    private boolean tryNijiPermissions() {
        try {
            perms = new NijiPermissionsResolver(server);
            logger.info(name + ": Permissions plugin detected! Using Permissions plugin for permissions.");
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    
    private boolean tryFlatFilePermissions() {
        if (FlatFilePermissionsResolver.filesExists()) {
            perms = new FlatFilePermissionsResolver();
            logger.info(name + ": perms_groups.txt and perms_users.txt detected! Using flat file permissions.");
            return true;
        }
        
        return false;
    }

    public void load() {
        perms.load();
    }

    public boolean hasPermission(String name, String permission) {
        return perms.hasPermission(name, permission);
    }

    public boolean inGroup(String player, String group) {
        return perms.inGroup(player, group);
    }

    public String[] getGroups(String player) {
        return perms.getGroups(player);
    }

}
