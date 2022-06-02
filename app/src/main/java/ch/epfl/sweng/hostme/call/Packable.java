package ch.epfl.sweng.hostme.call;

/**
 * Created by Li on 10/1/2016.
 */
public interface Packable {
    void marshal(ByteBuf out);
}
