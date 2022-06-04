package ch.epfl.sweng.hostme.call;

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

    /**
     * get the buffer as bytes
     *
     * @return bytes buffer
     */
    public byte[] asBytes() {
        byte[] out = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(out, 0, out.length);
        return out;
    }

    /**
     * put short value to the buffer
     *
     * @param v value to be put
     * @return the buffer
     */
    public ByteBuf put(short v) {
        buffer.putShort(v);
        return this;
    }

    /**
     * put byte array to the buffer
     *
     * @param v value to be put
     * @return the buffer with array in put
     */
    public ByteBuf put(byte[] v) {
        put((short) v.length);
        buffer.put(v);
        return this;
    }

    /**
     * put int into buffer
     *
     * @param v int to be put
     * @return buffer updated
     */
    public ByteBuf put(int v) {
        buffer.putInt(v);
        return this;
    }

    /**
     * put map into the buffer
     *
     * @param extra map to be put
     */
    public void putIntMap(TreeMap<Short, Integer> extra) {
        put((short) extra.size());

        for (Map.Entry<Short, Integer> pair : extra.entrySet()) {
            put(pair.getKey());
            put(pair.getValue());
        }

    }
}
