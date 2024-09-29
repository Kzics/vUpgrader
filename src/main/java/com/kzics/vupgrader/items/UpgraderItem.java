package com.kzics.vupgrader.items;

import com.kzics.vupgrader.services.ItemUpgradeService;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class UpgraderItem extends ItemStack {


    public UpgraderItem(Material material, int level){
        super(material);

        ItemMeta meta = getItemMeta();
        meta.getPersistentDataContainer().set(ItemUpgradeService.key, PersistentDataType.INTEGER, 0);
        meta.getPersistentDataContainer().set(ItemUpgradeService.nameKey, PersistentDataType.STRING,"chestplate");
        setItemMeta(meta);
    }
}
