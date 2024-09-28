package com.kzics.vupgrader.listeners;

import com.kzics.vupgrader.menu.UpgraderHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListeners implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event){
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getInventory();

        if(inventory.getHolder() instanceof UpgraderHolder){
            ((UpgraderHolder)inventory.getHolder()).handle(event);
        }
    }
}
