package ru.otus.homework15.message.system.base;

import ru.otus.homework15.message.system.Address;

public interface IRequestService {
    void addInnerReciever(IMessageReceiver innerReceiver);
    void removeInnerReceiver(Address innerReceiverAddress);

    void sendRequest(Address requesterAddress);
    void processResponse(Address innerRecieverAddress, String response);

}
