package com.kzics.vupgrader.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class UpgraderHolder implements InventoryHolder {
    public final Inventory inventory;

    public UpgraderHolder(String name, int size){
        this.inventory = Bukkit.createInventory(this,size, name);
    }

    public abstract void open(Player player);

    public abstract void handle(InventoryClickEvent event);

}
