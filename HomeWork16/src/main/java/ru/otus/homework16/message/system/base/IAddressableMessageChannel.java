package ru.otus.homework16.message.system.base;

import ru.otus.homework16.message.system.Address;

public interface IAddressableMessageChannel extends IMessageChannel {
    Address getAddress();
    void setAddress(Address address);
}
