package org.ihiw.management.security;

import org.ihiw.management.web.rest.errors.RepeatedRequestException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BlacklistManager {

    Map<String, Timeout> blacklist;
    int maxRepeats;
    long timeout;

    public BlacklistManager(int maxRepeats, long timeout) {
        blacklist = new HashMap<>();
        this.maxRepeats = maxRepeats;
        this.timeout = timeout;
    }

    public void checkRequest(HttpServletRequest request) throws RepeatedRequestException {
        String ip = request.getRemoteAddr();
        long timestamp = System.currentTimeMillis();
        cleanBlacklist(timestamp);
        if (blacklist.containsKey(ip)) {
            if (blacklist.get(ip).getTimeout() > timestamp && blacklist.get(ip).getRetries() < maxRepeats) {
                blacklist.get(ip).incrementRetries();
                blacklist.get(ip).setTimeout(timestamp + timeout);
            }
            else {
                throw new RepeatedRequestException();
            }
        }
        else {
            blacklist.put(ip, new Timeout(timestamp + timeout, 1));
        }
    }

    private void cleanBlacklist(long timestamp) {
        for (String key : blacklist.keySet()) {
            if (blacklist.get(key).getTimeout() < timestamp) {
                blacklist.remove(key);
            }
        }
    }

    public void decreaseLimit(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (blacklist.containsKey(ip)) {
            blacklist.get(ip).decrementRetries();
        }
    }
}
