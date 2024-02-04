package com.hegemonstudio.hegeworld.api.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemStackUtil {

    private static String toRoman(int n) {
        String[] romanNumerals = {"M", "CM", "D", "CD", "C", "XC", "L", "X", "IX", "V", "I"};
        int[] romanNumeralNums = {1000, 900, 500, 400, 100, 90, 50, 10, 9, 5, 1};
        StringBuilder finalRomanNum = new StringBuilder();

        for (int i = 0; i < romanNumeralNums.length; i++) {
            int currentNum = n / romanNumeralNums[i];
            if (currentNum == 0) {
                continue;
            }

            for (int j = 0; j < currentNum; j++) {
                finalRomanNum.append(romanNumerals[i]);
            }

            n = n % romanNumeralNums[i];
        }
        return finalRomanNum.toString();
    }

    public static void addItemStackToPlayer(Player player, ItemStack itemStack) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getLocation().getWorld().dropItem(player.getLocation(), itemStack);
        } else {
            player.getInventory().addItem(itemStack);
        }
    }

    public static ItemStack createGuiItem(Material material, int count, String name, String... lore) {
        ItemStack item = new ItemStack(material, count);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metaLore = new ArrayList<>(Arrays.asList(lore));
        meta.setLore(metaLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack GetUUIDHead(String display_name, int count, Player p, ArrayList<String> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, count);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(display_name);
        skull.setLore(lore);
        skull.setOwningPlayer(p);
        item.setItemMeta(skull);
        return item;
    }

    @Contract("_, _ -> param1")
    public static @NotNull ItemStack changeName(ItemStack item, String changedName) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ColorUtil.Color(changedName));
        item.setItemMeta(im);
        return item;
    }

    @Contract("_, _ -> param1")
    public static @NotNull ItemStack changeLore(@NotNull ItemStack item, String @NotNull ... lore) {
        ItemMeta im = item.getItemMeta();
        int var6 = lore.length;
        ArrayList<String> metaLore = new ArrayList<>(Arrays.asList(lore).subList(0, var6));
        im.setLore(metaLore);
        item.setItemMeta(im);
        return item;
    }

    public static ItemStack createItem(Material material, String name, String @NotNull ... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        int var7 = lore.length;
        ArrayList<String> metaLore = new ArrayList<>(Arrays.asList(lore).subList(0, var7));
        meta.setLore(metaLore);
        item.setItemMeta(meta);
        return item;
    }

    /*public static ItemStack getPlayerHead(Player p, int amount, String display_name, String... lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
        new BukkitRunnable() {
            @Override
            public void run() {
                SkullMeta skull = (SkullMeta) item.getItemMeta();
                skull.setDisplayName(display_name);
                int var8 = lore.length;
                ArrayList<String> metaLore = new ArrayList<>(Arrays.asList(lore).subList(0, var8));
                skull.setLore(metaLore);
                skull.setOwningPlayer(p);
                item.setItemMeta(skull);
            }

        }.runTaskAsynchronously(Main.);
        return item;
    }*/

    public static List<ItemStack> getSameItemsList(@NotNull Player player, ItemStack item) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (ItemStack invStack : player.getInventory().getContents()) {
            if (invStack != null) {
                if (invStack.getType() == item.getType()) {
                    if (invStack.getItemMeta().equals(item.getItemMeta())) {
                        items.add(invStack);
                    }
                }
            }
        }
        return items;
    }

    public static int getCountOfMaterial(@NotNull Player player, Material mat) {
        int found = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == mat) {
                    found = found + item.getAmount();
                }
            }
        }
        return found;
    }

    public static int getSameItems(@NotNull Player player, ItemStack item) {
        int found = 0;
        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null) continue;
            if (is.isSimilar(item)) {
                found += is.getAmount();
            }
        }
        return found;
    }

    public static boolean consumeItem(@NotNull Player player, int count, Material mat) {
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(mat);

        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

        }
        return true;
    }

    public static boolean consumeItem(@NotNull Player player, int count, ArrayList<ItemStack> items) {
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(items.get(0));

        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

        }
        return true;
    }

    public static boolean consumeItem(@NotNull Player player, int count, ItemStack item) {
        int found = getSameItems(player, item);
        if (count > found)
            return false;

        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null) continue;
            if (!is.isSimilar(item)) continue;
            int removed = Math.min(count, is.getAmount());
            count -= removed;
            is.setAmount(is.getAmount() - removed);
        }
//
//        for (Integer index : ammo.keySet()) {
//            ItemStack stack = ammo.get(index);
//
//            int removed = Math.min(count, stack.getAmount());
//            count -= removed;
//
//            if (stack.getAmount() == removed)
//                player.getInventory().setItem(index, null);
//            else
//                stack.setAmount(stack.getAmount() - removed);
//
//        }
        return count == 0;
    }

    public static boolean getBooleanOfMaterial(@NotNull Player player, Material mat, int count) {
        int found = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == mat) {
                    found = found + item.getAmount();
                }
            }
        }
        return count <= found;
    }

    public static boolean getBooleanOfMaterial(@NotNull Player player, ItemStack itemToScan, int count) {
        int found = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == itemToScan.getType()) {
                    for (Enchantment ignored : item.getEnchantments().keySet()) {
                        itemToScan.getEnchantments();
                    }
                    if (itemToScan.getItemMeta().hasDisplayName()) {
                        if (item.getItemMeta().hasDisplayName()) {
                            itemToScan.getItemMeta().getDisplayName();
                            item.getItemMeta().getDisplayName();
                        }
                    }
                    found = found + item.getAmount();
                }
            }
        }
        return count <= found;
    }

    @Contract("_, _, _ -> param1")
    public static @NotNull ItemStack addEnchantment(@NotNull ItemStack item, Enchantment chosen, int maxLevel) {
        item.addEnchantment(chosen, maxLevel);
        return item;
    }

    public static @NotNull ItemStack generateEnchantmentBook(@NotNull Map<Enchantment, Integer> enchantments) {
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
        for (Enchantment enchantment : enchantments.keySet()) {
            meta.addStoredEnchant(enchantment, enchantments.get(enchantment), true);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static @NotNull String toBase64(ItemStack is) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeObject(is);

            // Serialize that array
            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }

    public static ItemStack fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack is = (ItemStack) dataInput.readObject();
            dataInput.close();
            return is;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}