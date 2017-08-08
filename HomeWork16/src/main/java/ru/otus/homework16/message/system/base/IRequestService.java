package ru.otus.homework16.message.system.base;

import ru.otus.homework16.message.system.Address;

public interface IRequestService {
    void addInnerReciever(IMessageReceiver innerReceiver);
    void removeInnerReceiver(Address innerReceiverAddress);

    void sendCacheParamsRequest(Address requesterAddress);
    void processCacheParamsRequestResponse(Address innerRecieverAddress, String response);

    void sendCacheParamsChangeRequest(Address requesterAddress, long maximalLifeTime, long maximalIdleTime, int maximalSize);

}
