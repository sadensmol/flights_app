package me.sadensmol.flights.model.cache;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private static final int TTL_SEC = 5;

    //use soft reference as it's a cache
    //fixme add more operation like size check etc.
    private final Map<String, SoftReference<CacheObject>> cache = new ConcurrentHashMap<>();

    public Cache () {
        Thread cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep( 10000); //fixme we remove expired items every 10 seconds
                    cache.entrySet().removeIf(entry -> Optional.ofNullable(entry.getValue()).map(SoftReference::get).map(CacheObject::isExpired).orElse(false));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public void add(String key, Object value) {
        if (key == null) {
            return;
        }
        if (value == null) {
            cache.remove(key);
        } else {
            long expiryTime = System.currentTimeMillis() + TTL_SEC * 1000;
            cache.put(key, new SoftReference<>(new CacheObject(value, expiryTime)));
        }
    }

    public Object get(String key) {
        return Optional.ofNullable(cache.get(key)).map(SoftReference::get).filter(cacheObject -> !cacheObject.isExpired()).map(CacheObject::getValue).orElse(null);
    }


    private static class CacheObject {
        private Object value;
        private long expiryTime;

        private CacheObject(Object value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }


        public Object getValue() {
            return value;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }

    }
}
