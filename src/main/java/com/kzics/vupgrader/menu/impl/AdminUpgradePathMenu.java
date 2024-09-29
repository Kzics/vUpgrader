package com.kzics.vupgrader.menu.impl;

import com.kzics.vupgrader.VUpgrader;
import com.kzics.vupgrader.menu.UpgraderHolder;
import com.kzics.vupgrader.services.ItemUpgradeService;
import com.kzics.vupgrader.upgrades.IUpgrade;
import com.kzics.vupgrader.upgrades.UpgradePath;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class AdminUpgradePathMenu extends UpgraderHolder {
    private final UpgradePath upgradePath;
    private final NamespacedKey addItemKey = new NamespacedKey(VUpgrader.getInstance(), "add-item-path");
    public AdminUpgradePathMenu(UpgradePath upgradePath) {
        super("Upgrade Path", 54);
        this.upgradePath = upgradePath;
    }

    @Override
    public void open(Player player) {
        for (IUpgrade upgrade : upgradePath.getUpgrades()){
            inventory.addItem(upgrade.getItem());
        }

        ItemStack addItem = new ItemStack(Material.LIME_DYE);
        ItemMeta meta = addItem.getItemMeta();
        meta.getPersistentDataContainer().set(addItemKey, PersistentDataType.STRING,"");
        addItem.setItemMeta(meta);

        inventory.addItem(addItem);

        player.openInventory(inventory);

    }

    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);

        int slot = event.getSlot();
        final Player player = (Player) event.getWhoClicked();
        final ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null || itemStack.getItemMeta() == null) return;


        if(itemStack.getItemMeta().getPersistentDataContainer().has(addItemKey, PersistentDataType.STRING)){
            new AddUpgradeMenu(upgradePath).open(player);
            return;
        }

        IUpgrade upgrade = upgradePath.getUpgradeForLevel(slot);

        upgrade.getRequirements().getRequiredItems().forEach(it-> player.sendMessage(it.getType().name()));
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
