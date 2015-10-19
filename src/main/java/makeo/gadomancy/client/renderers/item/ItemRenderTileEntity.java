package makeo.gadomancy.client.renderers.item;

import makeo.gadomancy.client.util.FakeWorld;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 29.09.2015 18:29
 */
public class ItemRenderTileEntity<T extends TileEntity> implements IItemRenderer {
    private static final World FAKE_WORLD = new FakeWorld();

    private final TileEntitySpecialRenderer render;
    protected final T tile;

    public ItemRenderTileEntity(TileEntitySpecialRenderer render, T tile) {
        this.render = render;
        this.tile = tile;

        if(tile.getWorldObj() == null) {
            tile.setWorldObj(FAKE_WORLD);
        }
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if(type == IItemRenderer.ItemRenderType.ENTITY)
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);

        if(type == ItemRenderType.INVENTORY) {
            GL11.glTranslatef(0, -0.1f, 0);
        }

        this.render.renderTileEntityAt(tile, 0.0D, 0.0D, 0.0D, 0.0F);
    }
}
