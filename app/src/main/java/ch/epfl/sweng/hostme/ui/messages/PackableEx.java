package ch.epfl.sweng.hostme.ui.messages;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
