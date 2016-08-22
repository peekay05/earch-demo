package com.sample.search.app.ratelimiter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ExpiringKey<K> implements Delayed{

	private long startTime = System.currentTimeMillis();
    private final long maxLifeTimeMillis;
    private final K key;

    public ExpiringKey(K key, long maxLifeTimeMillis) {
        this.maxLifeTimeMillis = maxLifeTimeMillis;
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExpiringKey<K> other = (ExpiringKey<K>) obj;
        if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 31;
        hash = 17 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(getDelayMillis(), TimeUnit.MILLISECONDS);
    }

    private long getDelayMillis() {
        return (startTime + maxLifeTimeMillis) - System.currentTimeMillis();
    }

    public void renew() {
        startTime = System.currentTimeMillis();
    }

    public void expire() {
        startTime =  System.currentTimeMillis() - maxLifeTimeMillis - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Delayed that) {
        return Long.compare(this.getDelayMillis(), ((ExpiringKey) that).getDelayMillis());
    }
}
