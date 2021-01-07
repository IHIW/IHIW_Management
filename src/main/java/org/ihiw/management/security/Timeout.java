package org.ihiw.management.security;

public class Timeout {

    private long timeout;
    private int retries;

    public Timeout(long timeout, int retries) {
        this.timeout = timeout;
        this.retries = retries;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public void incrementRetries() {
        this.retries++;
    }

    public void decrementRetries() {
        this.retries--;
    }
}
