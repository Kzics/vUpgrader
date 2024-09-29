package com.kzics.vupgrader.menu.impl;

import com.kzics.vupgrader.VUpgrader;
import com.kzics.vupgrader.menu.UpgraderHolder;
import com.kzics.vupgrader.upgrades.ResourceRequirements;
import com.kzics.vupgrader.upgrades.Upgrade;
import com.kzics.vupgrader.upgrades.UpgradePath;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AddUpgradeMenu extends UpgraderHolder {
    private final int[] UPGRADE_REQ_SLOT = {29, 30, 31, 32, 33, 38, 39, 40, 41, 42};
    private final UpgradePath path;
    public AddUpgradeMenu(UpgradePath path) {
        super("Add Upgrade", 54);
        this.path = path;
    }

    @Override
    public void open(Player player) {
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < UPGRADE_REQ_SLOT.length; i++) {
            inventory.setItem(UPGRADE_REQ_SLOT[i], glass);
        }

        inventory.setItem(13, glass);

        // Ajouter le bouton "Appliquer"
        ItemStack applyButton = new ItemStack(Material.LIME_DYE);
        ItemMeta meta = applyButton.getItemMeta();
        meta.setDisplayName("§aAppliquer");
        applyButton.setItemMeta(meta);

        inventory.setItem(53, applyButton); // Dernier slot (54-1 = 53)
        player.openInventory(inventory);
    }

    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory != null && clickedInventory.equals(inventory)) {
            int slot = event.getSlot();
            ItemStack currentItem = event.getCurrentItem();

            if (currentItem == null || currentItem.getType() == Material.AIR) {
                return;
            }

            if (slot == 53 && currentItem.getType() == Material.LIME_DYE) {
                // Récupérer l'item principal dans le slot 13
                ItemStack mainItem = inventory.getItem(13);
                if (mainItem == null || mainItem.getType() == Material.BLACK_STAINED_GLASS_PANE) {
                    player.sendMessage("§cAucun item à upgrader !");
                    return;
                }

                List<ItemStack> requirementsList = new ArrayList<>();
                for (int upgradeSlot : UPGRADE_REQ_SLOT) {
                    ItemStack requirementItem = inventory.getItem(upgradeSlot);
                    if (requirementItem != null && requirementItem.getType() != Material.BLACK_STAINED_GLASS_PANE) {
                        requirementsList.add(requirementItem);
                    }
                }

                if (requirementsList.isEmpty()) {
                    player.sendMessage("§cAucune ressource pour l'upgrade n'a été ajoutée !");
                    return;
                }

                ResourceRequirements requirements = new ResourceRequirements(requirementsList);



                int upgradeLevel = 1;

                Upgrade upgrade = new Upgrade(mainItem, mainItem.getEnchantments(), requirements, upgradeLevel);

                path.addUpgrade(upgrade);

                player.closeInventory();
                player.sendMessage("§aUpgrade appliqué avec succès !");
            }

            if (slot == 13) {
                ItemStack slot13Item = inventory.getItem(13);

                if (slot13Item != null && slot13Item.getType() != Material.BLACK_STAINED_GLASS_PANE) {
                    inventory.setItem(13, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                    player.getInventory().addItem(slot13Item);
                }
                return;
            }

            for (int upgradeSlot : UPGRADE_REQ_SLOT) {
                ItemStack upgradeSlotItem = inventory.getItem(upgradeSlot);

                if (slot == upgradeSlot) {
                    if (upgradeSlotItem != null && upgradeSlotItem.getType() != Material.BLACK_STAINED_GLASS_PANE) {
                        inventory.setItem(upgradeSlot, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                        player.getInventory().addItem(upgradeSlotItem);
                    }
                    return;
                }
            }

            player.sendMessage("§cTous les slots d'upgrade sont remplis !");
            return;
        }

        if (clickedInventory != null && clickedInventory.equals(player.getInventory())) {
            int slot = event.getSlot();
            ItemStack currentItem = event.getCurrentItem();

            if (currentItem == null || currentItem.getType() == Material.AIR) {
                return;
            }

            if (inventory.getItem(13).getType() == Material.BLACK_STAINED_GLASS_PANE) {
                inventory.setItem(13, currentItem);
                return;
            }

            for (int upgradeSlot : UPGRADE_REQ_SLOT) {
                ItemStack upgradeSlotItem = inventory.getItem(upgradeSlot);

                if (upgradeSlotItem != null && upgradeSlotItem.getType() == Material.BLACK_STAINED_GLASS_PANE) {
                    inventory.setItem(upgradeSlot, currentItem);
                    return;
                }
            }

            player.sendMessage("§cTous les slots d'upgrade sont remplis !");
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
