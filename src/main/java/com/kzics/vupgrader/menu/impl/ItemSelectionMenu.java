package com.kzics.vupgrader.menu.impl;

import com.kzics.vupgrader.VUpgrader;
import com.kzics.vupgrader.menu.UpgraderHolder;
import com.kzics.vupgrader.services.ItemUpgradeService;
import com.kzics.vupgrader.upgrades.IUpgrade;
import com.kzics.vupgrader.upgrades.Upgrade;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemSelectionMenu extends UpgraderHolder {

    private final int[] UPGRADE_SLOTS = {19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
    private final int MAX_ITEMS_PER_PAGE = UPGRADE_SLOTS.length;
    private int currentPage = 0;

    public ItemSelectionMenu() {
        super("Item Selection", 54);
    }

    @Override
    public void open(Player player) {
        List<ItemStack> upgradeableItems = getUpgradeableItems(player);

        displayItems(player, upgradeableItems, currentPage);
        player.openInventory(inventory);
    }

    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        final ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getItemMeta() == null) return;
        if (!clickedItem.getItemMeta().getPersistentDataContainer().has(ItemUpgradeService.key, PersistentDataType.INTEGER)) return;

        int upgradeLevel = clickedItem.getItemMeta().getPersistentDataContainer().get(ItemUpgradeService.key, PersistentDataType.INTEGER);
        IUpgrade upgrade = VUpgrader.getInstance().getUpgradePathManager().getUpgradePath("helmet").getUpgradeForLevel(upgradeLevel);

        int clickedSlot = event.getSlot();
        ItemStack inventoryItem = null;

        for (ItemStack stack : player.getInventory().getContents()){
            if(clickedItem.isSimilar(stack)){
                inventoryItem = stack;
            }
        }
        new UpgraderMenu(upgrade, inventoryItem).open(player);

        List<ItemStack> upgradeableItems = getUpgradeableItems(player);
        int totalPages = (int) Math.ceil((double) upgradeableItems.size() / MAX_ITEMS_PER_PAGE);

        if (clickedSlot == 45 && currentPage > 0) {
            currentPage--;
            open(player);
        } else if (clickedSlot == 53 && currentPage < totalPages - 1) {
            currentPage++;
            open(player);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private List<ItemStack> getUpgradeableItems(Player player) {
        List<ItemStack> itemStacks = new ArrayList<>();

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getItemMeta() == null) continue;
            if (!itemStack.getItemMeta().getPersistentDataContainer().has(ItemUpgradeService.key, PersistentDataType.INTEGER)) continue;

            itemStacks.add(itemStack);
        }
        return itemStacks;
    }

    private void displayItems(Player player, List<ItemStack> upgradeableItems, int page) {
        inventory.clear();

        int startIndex = page * MAX_ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + MAX_ITEMS_PER_PAGE, upgradeableItems.size()); // Calcul de l'index de fin

        for (int i = startIndex; i < endIndex; i++) {
            int slot = UPGRADE_SLOTS[i - startIndex];

            inventory.setItem(slot, upgradeableItems.get(i));
        }

        for (int i = endIndex - startIndex; i < MAX_ITEMS_PER_PAGE; i++) {
            inventory.setItem(UPGRADE_SLOTS[i], new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }

        if (page > 0) {
            inventory.setItem(45, createNavigationButton("§aPrécédent"));
        }

        if (endIndex < upgradeableItems.size()) {
            inventory.setItem(53, createNavigationButton("§aSuivant"));
        }
    }

    private ItemStack createNavigationButton(String name) {
        ItemStack button = new ItemStack(Material.ARROW);
        ItemMeta meta = button.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            button.setItemMeta(meta);
        }
        return button;
    }
}
