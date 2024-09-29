package com.kzics.vupgrader.listeners;

import com.kzics.vupgrader.VUpgrader;
import com.kzics.vupgrader.items.UpgraderItem;
import com.kzics.vupgrader.menu.impl.AdminUpgradesMenu;
import com.kzics.vupgrader.menu.impl.ItemSelectionMenu;
import com.kzics.vupgrader.services.ItemUpgradeService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListeners implements Listener {

    private final VUpgrader vUpgrader;

    public PlayerListeners(VUpgrader vUpgrader){
        this.vUpgrader = vUpgrader;
    }



    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        final Player player = event.getPlayer();
        final ItemStack handItem = player.getInventory().getItemInMainHand();

        if(!event.isSneaking()) {
            UpgraderItem upgraderItem = new UpgraderItem(Material.CHAINMAIL_CHESTPLATE, 0);
            player.getInventory().addItem(upgraderItem);
            String pathName = upgraderItem.getItemMeta().getPersistentDataContainer().get(ItemUpgradeService.nameKey, PersistentDataType.STRING);
            new ItemSelectionMenu().open(player);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        new AdminUpgradesMenu(vUpgrader.getUpgradePathManager()).open(event.getPlayer());
    }
}
