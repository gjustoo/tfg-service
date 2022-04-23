package com.gabriel.tfg.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.gabriel.tfg.entity.ApiConnection;
import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.service.ApiConnectionService;
import com.gabriel.tfg.service.PlatformService;

import org.springframework.beans.factory.annotation.Autowired;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class twitterAPI {

    @Autowired
    private static ApiConnectionService apiConnectionService;

    @Autowired
    private static PlatformService platformService;

    public static void main(String args[]) throws Exception {
        // The factory instance is re-useable and thread safe.
        
        Twitter twitter = TwitterFactory.getSingleton();
        Platform platform = platformService.getByName("twitter");
        ApiConnection connection = apiConnectionService.getByPlatform(platform);
        
        twitter.setOAuthConsumer(connection.getClientIdToken(), connection.getClientSecretToken());
        
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            String pin = br.readLine();
            try {
                if (pin.length() > 0) {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                } else {
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if (401 == te.getStatusCode()) {
                    System.out.println("Unable to get the access token.");
                } else {
                    te.printStackTrace();
                }
            }
        }
        // persist to the accessToken for future reference.
        storeAccessToken(twitter.verifyCredentials().getId(), accessToken);
        Status status = twitter.updateStatus(args[0]);
        System.out.println("Successfully updated the status to [" + status.getText() + "].");
        System.exit(0);
    }

    private static void storeAccessToken(Long useId, AccessToken accessToken) {
        // store accessToken.getToken()s
        // store accessToken.getTokenSecret()
    }
}
