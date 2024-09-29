package com.kzics.vupgrader.menu.impl;

import com.kzics.vupgrader.VUpgrader;
import com.kzics.vupgrader.manager.UpgradePathManager;
import com.kzics.vupgrader.menu.UpgraderHolder;
import com.kzics.vupgrader.upgrades.UpgradePath;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class AdminUpgradesMenu extends UpgraderHolder {
    private final UpgradePathManager upgradePathManager;
    private final NamespacedKey key = new NamespacedKey(VUpgrader.getInstance(), "admin-item");

    public AdminUpgradesMenu(UpgradePathManager upgradePathManager) {
        super("Admin Upgrade Menu", 54);

        this.upgradePathManager = upgradePathManager;
    }

    @Override
    public void open(Player player) {
        for (Map.Entry<String, UpgradePath> pathEntry : upgradePathManager.getUpgradePaths().entrySet()){
            ItemStack itemStack = new ItemStack(pathEntry.getValue().getDisplayMaterial());
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName("§a" + pathEntry.getKey());
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, pathEntry.getKey());
            itemStack.setItemMeta(meta);
            inventory.addItem(itemStack);
        }
    }

    @Override
    public void handle(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null || clickedItem.getItemMeta() == null ||
                !clickedItem.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;

        String pathName = clickedItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        UpgradePath path = upgradePathManager.getUpgradePath(pathName);

        new AdminUpgradePathMenu(path).open();
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
