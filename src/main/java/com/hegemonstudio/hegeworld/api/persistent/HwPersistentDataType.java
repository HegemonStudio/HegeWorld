package com.hegemonstudio.hegeworld.api.persistent;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class HwPersistentDataType {

  public static final PersistentDataType<byte[], java.util.UUID> UUID = new UUID();

  private HwPersistentDataType() {

  }

  private static class UUID implements PersistentDataType<byte[], java.util.UUID> {

    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
      return byte[].class;
    }

    @Override
    public @NotNull Class<java.util.UUID> getComplexType() {
      return java.util.UUID.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(java.util.UUID complex, @NotNull PersistentDataAdapterContext context) {
      ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
      buffer.putLong(complex.getMostSignificantBits());
      buffer.putLong(complex.getLeastSignificantBits());
      return buffer.array();
    }

    @Override
    public java.util.@NotNull UUID fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
      ByteBuffer bb = ByteBuffer.wrap(primitive);
      long firstLong = bb.getLong();
      long secondLong = bb.getLong();
      return new java.util.UUID(firstLong, secondLong);
    }

  }

}
