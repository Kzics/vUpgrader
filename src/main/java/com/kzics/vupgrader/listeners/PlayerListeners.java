package com.kzics.vupgrader.listeners;

import com.kzics.vupgrader.VUpgrader;
import com.kzics.vupgrader.menu.impl.ItemSelectionMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListeners implements Listener {

    private final VUpgrader vUpgrader;

    public PlayerListeners(VUpgrader vUpgrader){
        this.vUpgrader = vUpgrader;
    }


    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        final Player player = event.getPlayer();
        final ItemStack handItem = player.getInventory().getItemInMainHand();

        if(!event.isSneaking())
        vUpgrader.getItemUpgradeService().upgradeItem(player, handItem,"helmet");
        new ItemSelectionMenu().open(player);
    }
}
