package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import thaumcraft.common.tiles.TileJarFillable;

import java.util.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.10.2015 15:06
 */
public class TileRemoteJar extends TileJarFillable {
    public UUID networkId = null;

    private int count = 0;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (count % 3 == 0 && !getWorldObj().isRemote && networkId != null && amount < maxAmount) {
            count = 0;

            JarNetwork network = getNetwork(networkId);

            if(!network.jars.contains(this)) {
                network.jars.add(this);
            }

            network.update();
        }
        count++;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        networkId = NBTHelper.getUUID(compound, "networkId");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        if(networkId != null) {
            NBTHelper.setUUID(compound, "networkId", networkId);
        }
    }

    private static Map<UUID, JarNetwork> networks = new HashMap<UUID, JarNetwork>();

    private static class JarNetwork {
        private long lastTime = 0;
        private List<TileRemoteJar> jars = new ArrayList<TileRemoteJar>();

        private void update() {
            long time = MinecraftServer.getServer().getEntityWorld().getTotalWorldTime();
            if(time > lastTime) {
                if(jars.size() > 1) {
                    Collections.sort(jars, new Comparator<TileRemoteJar>() {
                        @Override
                        public int compare(TileRemoteJar o1, TileRemoteJar o2) {
                            return o2.amount - o1.amount;
                        }
                    });

                    TileRemoteJar jar1 = jars.get(0);
                    if(!isValid(jar1)) {
                        jars.remove(0);
                        return;
                    }

                    TileRemoteJar jar2 = jars.get(jars.size() - 1);
                    if(!isValid(jar2)) {
                        jars.remove(jars.size() - 1);
                        return;
                    }

                    if((jar2.amount+1) < jar1.amount && jar2.addToContainer(jar1.aspect, 1) == 0) {
                        jar1.takeFromContainer(jar1.aspect, 1);
                    }
                }
                lastTime = time + 3;
            }
        }

        private static boolean isValid(TileRemoteJar jar) {
            return jar != null && jar.getWorldObj() != null && !jar.isInvalid()
                    && jar.getWorldObj().blockExists(jar.xCoord, jar.yCoord, jar.zCoord);
        }
    }

    private static JarNetwork getNetwork(UUID id) {
        JarNetwork network = networks.get(id);

        if(network == null) {
            network = new JarNetwork();
            networks.put(id, network);
        }
        return network;
    }

    public void markForUpdate() {
        markDirty();
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
