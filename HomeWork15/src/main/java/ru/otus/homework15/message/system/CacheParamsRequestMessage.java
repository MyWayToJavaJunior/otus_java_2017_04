package ru.otus.homework15.message.system;

public class CacheParamsRequestMessage extends Message{


    public CacheParamsRequestMessage(Address receiver, Address sender) {
        super(receiver, sender);
    }
}
