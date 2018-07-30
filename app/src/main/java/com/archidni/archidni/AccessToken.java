package com.archidni.archidni;

public class AccessToken {
    private String token;
    private long expires_in;
    private long timeWhenAcquired;

    /**
     *
     * @param token token string
     * @param expires_in
     * @param timeWhenAcquired current time when token has been acquired in seconds
     */

    public AccessToken(String token, long expires_in, long timeWhenAcquired) {
        this.token = token;
        this.expires_in = expires_in;
        this.timeWhenAcquired = timeWhenAcquired;
    }

    public String getToken() {
        return token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public long getTimeWhenAcquired() {
        return timeWhenAcquired;
    }

    public boolean hasExpired ()
    {
        return ((System.currentTimeMillis()/1000)>(timeWhenAcquired+ expires_in));
    }
}
