package org.irmc.pigeonlib.pdc.types;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@SuppressWarnings("deprecation")
public class ItemStackDataType implements PersistentDataType<byte[], ItemStack> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<ItemStack> getComplexType() {
        return ItemStack.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull ItemStack itemStack, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (BukkitObjectOutputStream output = new BukkitObjectOutputStream(bytes)) {
            output.writeObject(itemStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes.toByteArray();
    }

    @Override
    public @NotNull ItemStack fromPrimitive(byte @NotNull [] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        ByteArrayInputStream bytesStream = new ByteArrayInputStream(bytes);
        try (BukkitObjectInputStream input = new BukkitObjectInputStream(bytesStream)) {
            return (ItemStack) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
