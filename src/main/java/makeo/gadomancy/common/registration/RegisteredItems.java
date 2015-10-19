package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.registry.GameRegistry;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.items.ItemFakeGolemPlacer;
import makeo.gadomancy.common.items.ItemFakeGolemShield;
import makeo.gadomancy.common.items.ItemGolemCoreBreak;
import makeo.gadomancy.common.items.ItemTransformationFocus;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 03.06.2015 03:37
 */
public class RegisteredItems {
    private RegisteredItems() {}

    public static CreativeTabs creativeTab;

    public static Item itemFakeGolemShield;
    public static Item itemFakeGolemPlacer;
    public static Item itemTransformationFocus;
    public static Item itemGolemCoreBreak;

    public static void preInit() {
        creativeTab = new CreativeTabs(Gadomancy.MODID) {
            @Override
            public Item getTabIconItem() {
                return Items.boat;
            }
        };
    }

    public static void init() {
        registerItems();

        registerDefaultStickyJars();
    }

    //Items
    private static void registerItems() {
        itemFakeGolemPlacer = registerItem(new ItemFakeGolemPlacer());
        itemFakeGolemShield = registerItem(new ItemFakeGolemShield());
        itemTransformationFocus = registerItem(new ItemTransformationFocus());
        itemGolemCoreBreak = registerItem(new ItemGolemCoreBreak());
    }

    private static <T extends Item> T registerItem(T item, String name) {
        GameRegistry.registerItem(item, name);
        return item;
    }

    private static <T extends Item> T registerItem(T item) {
        return registerItem(item, item.getClass().getSimpleName());
    }


    //Sticky jars
    private static void registerDefaultStickyJars() {
        registerStickyJar(ConfigItems.itemJarFilled, 0);
        registerStickyJar(ConfigItems.itemJarFilled, 3);

        Item itemBlockJar = Item.getItemFromBlock(ConfigBlocks.blockJar);
        registerStickyJar(itemBlockJar, 0, new ItemStack(itemBlockJar, 1, 0));
        registerStickyJar(itemBlockJar, 3, new ItemStack(itemBlockJar, 1, 3));
    }

    private static List<StickyJarItemInfo> stickyJarItems = new ArrayList<StickyJarItemInfo>();

    public static void registerStickyJar(Item item, int damage, ItemStack recipeStack) {
        StickyJarItemInfo info = new StickyJarItemInfo();
        info.item = item;
        info.damage = damage;
        info.recipeStack = recipeStack;
        stickyJarItems.add(info);
    }

    public static void registerStickyJar(Item item, int damage) {
        registerStickyJar(item, damage, null);
    }

    public static void registerStickyJar(Item item) {
        registerStickyJar(item, Short.MAX_VALUE);
    }

    public static boolean isStickyableJar(Item item, int damage) {
        for(StickyJarItemInfo info : stickyJarItems) {
            if(info.item == item && (info.damage == damage || info.damage == Short.MAX_VALUE)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStickyableJar(ItemStack stack) {
        return stack != null && isStickyableJar(stack.getItem(), stack.getItemDamage());
    }

    public static List<ItemStack> getStickyJarStacks() {
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for(StickyJarItemInfo info : stickyJarItems) {
            if(info.recipeStack != null) {
                stacks.add(info.recipeStack);
            }
        }
        return stacks;
    }

    private static class StickyJarItemInfo {
        public Item item;
        public int damage;
        public ItemStack recipeStack;
    }
}
