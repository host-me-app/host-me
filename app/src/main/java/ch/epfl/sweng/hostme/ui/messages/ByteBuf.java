package ch.epfl.sweng.hostme.ui.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Li on 10/1/2016.
 */
public class ByteBuf {
    ByteBuffer buffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);

    public ByteBuf() {
    }

    public byte[] asBytes() {
        byte[] out = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(out, 0, out.length);
        return out;
    }

    public ByteBuf put(short v) {
        buffer.putShort(v);
        return this;
    }

    public ByteBuf put(byte[] v) {
        put((short) v.length);
        buffer.put(v);
        return this;
    }

    public ByteBuf put(int v) {
        buffer.putInt(v);
        return this;
    }

    public void putIntMap(TreeMap<Short, Integer> extra) {
        put((short) extra.size());

        for (Map.Entry<Short, Integer> pair : extra.entrySet()) {
            put(pair.getKey());
            put(pair.getValue());
        }

    }
}
