package com.kzics.vupgrader.menu.impl;

import com.kzics.vupgrader.VUpgrader;
import com.kzics.vupgrader.menu.UpgraderHolder;
import com.kzics.vupgrader.services.ItemUpgradeService;
import com.kzics.vupgrader.upgrades.IUpgrade;
import com.kzics.vupgrader.upgrades.ResourceRequirements;
import com.kzics.vupgrader.upgrades.Upgrade;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class UpgraderMenu extends UpgraderHolder {

    private final int[] UPGRADE_REQ_SLOT = {29, 30, 31, 32, 33, 38, 39, 40, 41, 42};
    private final IUpgrade upgrade;
    private final VUpgrader vUpgrader;
    private final ItemStack upgradedItem;

    public UpgraderMenu(IUpgrade upgrade, ItemStack upgradedItem) {
        super("Upgrade Menu", 54);
        this.upgrade = upgrade;
        this.vUpgrader = VUpgrader.getInstance();
        this.upgradedItem = upgradedItem;
    }

    @Override
    public void open(Player player) {
        inventory.clear();

        int level = getItemLevel(upgradedItem);
        System.out.println(level);

        // Récupère l'upgrade pour le niveau suivant, ou null si elle n'existe pas
        IUpgrade nextUpgrade = vUpgrader.getUpgradePathManager()
                .getUpgradePath("helmet")
                .getUpgradeForLevel(level + 1);

        inventory.setItem(12, upgradedItem);

        inventory.setItem(13, createUpgradeButton());

        if (nextUpgrade != null) {
            ItemStack newItem = nextUpgrade.getItem();
            inventory.setItem(14, newItem);

            ResourceRequirements requiredMaterials = nextUpgrade.getRequirements();
            displayRequiredMaterials(requiredMaterials.getRequiredItems());
        } else {
            inventory.setItem(14, new ItemStack(Material.BARRIER));  // Par exemple, une barrière pour indiquer le max level
        }

        player.openInventory(inventory);
    }


    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (slot == 13) {
          IUpgrade newUpgrade = vUpgrader.getItemUpgradeService().upgradeItem(player, upgradedItem, "helmet");
          if (newUpgrade != null) {
            //replaceItemInInventory(player, upgradedItem, newUpgrade.getItem());

            player.sendMessage("§aUpgrade réussi !");
            new UpgraderMenu(newUpgrade, upgradedItem).open(player);
          } else {
              player.sendMessage("§cPas assez de matériaux requis pour l'upgrade !");
          }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private void displayRequiredMaterials(List<ItemStack> requiredMaterials) {
        int index = 0;

        for (ItemStack material : requiredMaterials) {
            if (index >= UPGRADE_REQ_SLOT.length) {
                break;
            }
            inventory.setItem(UPGRADE_REQ_SLOT[index], material);
            index++;
        }

        for (int i = index; i < UPGRADE_REQ_SLOT.length; i++) {
            inventory.setItem(UPGRADE_REQ_SLOT[i], new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
    }

    private ItemStack createUpgradeButton() {
        ItemStack button = new ItemStack(Material.EMERALD);
        ItemMeta meta = button.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§aUpgrade");
            button.setItemMeta(meta);
        }
        return button;
    }



    private boolean canUpgrade(Player player) {
        return true;
    }


    public int getItemLevel(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().get(ItemUpgradeService.key, PersistentDataType.INTEGER);
    }
}
