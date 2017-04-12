package ru.otus.homework02;

import java.util.function.Supplier;

public class ObjectSizeMeter {
    private int passCnt = 3;
    private int objArrLen = 100;

    public int getPassCnt() {
        return passCnt;
    }

    public void setPassCnt(int passCnt) {
        this.passCnt = passCnt;
    }

    public int getObjArrLen() {
        return objArrLen;
    }

    public void setObjArrLen(int objArrLen) {
        this.objArrLen = objArrLen;
    }

    private void gc() {
        try {
            System.gc();
            Thread.sleep(100);

            System.runFinalization();
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    private long memoryUsage(){
        gc();
        gc();
        long totalMemory = Runtime.getRuntime().totalMemory();
        gc();
        gc();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return (totalMemory - freeMemory);
    }

    private long onePass(Supplier<Object> so) throws Exception{
        long memBefore, memAfter;
        Object[] arr = new Object[objArrLen];

        memBefore = memoryUsage();
        for (int j = 0; j < objArrLen; j++) {
            arr[j] = so.get();
        }
        memAfter = memoryUsage();

        long tmpres = Math.round(1f * (memAfter - memBefore) / objArrLen);
        return tmpres;
    }

    public long measure(Supplier<Object> so) throws Exception {
        if (passCnt == 0 || objArrLen == 0) {
            throw new Exception("Wrong properties values");
        }

        long avgTmp = 0;
        for (int i = 0; i < passCnt; i++) {
            avgTmp += onePass(so);
        }
        return avgTmp / passCnt;
    }

}
