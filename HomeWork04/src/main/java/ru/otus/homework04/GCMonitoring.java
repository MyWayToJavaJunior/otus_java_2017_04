package ru.otus.homework04;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GCMonitoring {
    private List<NotificationListener> listeners = new ArrayList<>();
    private List<NotificationEmitter> emitters = new ArrayList<>();
    private Map<String, GCStat> gcStatMap = new HashMap<>();
    private long startTimeMills;
    private long monitoringDuration;

    public void startMonitoring() {
        stopMonitoring();
        gcStatMap.clear();
        startTimeMills = System.currentTimeMillis();

        List<GarbageCollectorMXBean> gcList = ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean gc : gcList) {
            NotificationEmitter emitter = (NotificationEmitter) gc;

            if (!gcStatMap.containsKey(gc.getName())) {
                gcStatMap.put(gc.getName(), new GCStat());
            }

            NotificationListener listener = new GCMonitoringNotificationListener();
            emitter.addNotificationListener(listener, null, null);
            listeners.add(listener);
            emitters.add(emitter);
        }
    }

    public void stopMonitoring() {
        monitoringDuration = System.currentTimeMillis() - startTimeMills;

        for (int i = 0; i < emitters.size(); i++) {
            try {
                emitters.get(i).removeNotificationListener(listeners.get(i));
            } catch (ListenerNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }

        listeners.clear();
        emitters.clear();
    }

    public void saveSatToFile(String fileName) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String line = String.format("Total monitoring duration: %s min \n", String.format("%.2f", monitoringDuration / 60_000f));
                    writer.write(line);
            for (Map.Entry<String, GCStat> e : gcStatMap.entrySet()) {
                line = String.format("GCName = %s, %s \n", e.getKey(), e.getValue().toString());
                writer.write(line);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private class GCMonitoringNotificationListener implements NotificationListener {
        private void logNotification(String gcName, float totalDuration, float timeElapsedMins, GCStat stat) {
            StringBuilder msg = new StringBuilder();
            msg.append("GCName: ").append(gcName).append("\n")
                    .append("   , Total monitoring duration: ")
                    .append(String.format("%.2f", totalDuration)).append(" min").append("\n")

                    .append("   , Total number of trips: ")
                    .append(stat.getTotalNumberOfTrips()).append("\n")

                    .append("   , Total GC duration: ")
                    .append(stat.getTotalDuration()).append(" ms").append("\n")

                    .append("   , Avg GC duration: ")
                    .append(stat.getAvgDuration()).append(" ms").append("\n")

                    .append("   , Min GC duration: ")
                    .append(stat.getMinDuration()).append(" ms").append("\n")

                    .append("   , Max GC duration: ")
                    .append(stat.getMaxDuration()).append(" ms").append("\n")

                    .append("   , Number of trips per per last ")
                    .append(String.format("%.2f", timeElapsedMins)).append(" min: ").append(stat.getSumNumberOfTripsPerMinute()).append("\n")

                    .append("   , GC duration per last ")
                    .append(String.format("%.2f", timeElapsedMins)).append(" min: ").append(stat.getSumDurationPerMinute()).append(" ms");

            System.out.println(msg.toString());

        }

        @Override
        public void handleNotification(Notification notification, Object handback) {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                String gcName = info.getGcName();
                long duration = info.getGcInfo().getDuration();
                GCStat stat = gcStatMap.get(gcName);
                stat.add(duration);

                long curTimeMills = System.currentTimeMillis();
                long timeElapsedMills = curTimeMills - stat.getPrevNotificationTimeMills();

                monitoringDuration = curTimeMills - startTimeMills;
                float totalDuration = (monitoringDuration) / 60_000f;
                float timeElapsedMins = timeElapsedMills / 60_000f;
                if (timeElapsedMills > 60_000) {
                    logNotification(gcName, totalDuration, timeElapsedMins, stat);
                    stat.setPrevNotificationTimeMills(curTimeMills);
                    stat.setSumDurationPerMinute(0);
                    stat.setSumNumberOfTripsPerMinute(0);
                }

                stat.incSumDurationPerMinute(duration);
                stat.incSumNumberOfTripsPerMinute();

            }
        }
    }

    private class GCStat {
        private int totalNumberOfTrips;
        private long totalDuration;
        private long minDuration;
        private long maxDuration;
        private long avgDuration;

        private long prevNotificationTimeMills = System.currentTimeMillis();
        private long sumDurationPerMinute;
        private int sumNumberOfTripsPerMinute;

        public void add(long duration){
            totalNumberOfTrips++;
            totalDuration += duration;
            minDuration = (minDuration == 0) ? duration : Math.min(duration, minDuration);
            maxDuration = Math.max(duration, maxDuration);
            avgDuration = totalDuration / totalNumberOfTrips;
        }

        public long getPrevNotificationTimeMills() {
            return prevNotificationTimeMills;
        }

        public void setPrevNotificationTimeMills(long prevNotificationTimeMills) {
            this.prevNotificationTimeMills = prevNotificationTimeMills;
        }

        public int getTotalNumberOfTrips() {
            return totalNumberOfTrips;
        }

        public long getTotalDuration() {
            return totalDuration;
        }

        public long getMinDuration() {
            return minDuration;
        }

        public long getMaxDuration() {
            return maxDuration;
        }

        public long getAvgDuration() {
            return avgDuration;
        }

        public long getSumDurationPerMinute() {
            return sumDurationPerMinute;
        }

        public void incSumDurationPerMinute(long duration) {
            this.sumDurationPerMinute += duration;
        }

        public int getSumNumberOfTripsPerMinute() {
            return sumNumberOfTripsPerMinute;
        }

        public void incSumNumberOfTripsPerMinute() {
            this.sumNumberOfTripsPerMinute ++;
        }

        public void setSumDurationPerMinute(long sumDurationPerMinute) {
            this.sumDurationPerMinute = sumDurationPerMinute;
        }

        public void setSumNumberOfTripsPerMinute(int sumNumberOfTripsPerMinute) {
            this.sumNumberOfTripsPerMinute = sumNumberOfTripsPerMinute;
        }

        @Override
        public String toString() {
            return "GCStat{" +
                    "totalNumberOfTrips=" + totalNumberOfTrips +
                    ", totalDuration=" + totalDuration +
                    ", minDuration=" + minDuration +
                    ", maxDuration=" + maxDuration +
                    ", avgDuration=" + avgDuration +
                    '}';
        }
    }
}
