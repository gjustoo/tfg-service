package com.gabriel.tfg.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.entity.Post;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONObject;

public class TwitterUtils {

    // @Scheduled(fixedRate = 365 * 24 * 60 * 60 * 1000)
    // public void main() {

    // Platform twitter = platformService.getByName("twitter");
    // List<FeedNode> feeds = feedNodeService.getAllByPlatform(twitter);
    // for (FeedNode feedNode : feeds) {
    // getFeedFromUser(feedNode);
    // }
    // }

    public static List<Post> getFeedFromUser(FeedNode feedNode) {

        System.out.println(
                "Getting feed from : " + feedNode.getUid() + " platform : " + feedNode.getPlatform().getName());
        String userId = feedNode.getUid();

        LocalDateTime endTime = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime startTime = endTime.minusHours(500);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String startTimeFormatted = startTime.format(format);
        String endTimeFormatted = endTime.format(format);
        List<Post> posts = new ArrayList();
        HttpResponse<JsonNode> response;
        Unirest.setTimeouts(0, 0);
        try {
            String url = feedNode.getPlatform().getUrl()
                    + "/users/" + userId + "/tweets?start_time=" + startTimeFormatted
                    + "&end_time="
                    + endTimeFormatted
                    + "&expansions=attachments.media_keys&tweet.fields=attachments,author_id,created_at&media.fields=url,type&max_results=100";
            response = Unirest.get(url)
                    .header("Authorization",
                            "Bearer " + feedNode.getPlatform().getBearerToken())
                    .asJson();
            posts = parsePost(response.getBody(), feedNode);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        System.out.println(posts);
        return posts;
    }

    public static List<Post> parsePost(JsonNode json, FeedNode feedNode) {
        List<Post> posts = new ArrayList<>();

        JSONArray data = json.getObject().getJSONArray("data");
        JSONObject includes = (JSONObject) json.getObject().get("includes");
        Map<String, JSONObject> urlsByMediaKeys = getMediaKeys(includes);

        if (urlsByMediaKeys.isEmpty()) {
            System.out.println("No media tweets found...");
            return posts;
        }

        for (int i = 0; i < data.length(); i++) {
            JSONObject tweet = (JSONObject) data.get(i);

            if (tweet.has("attachments")) {

                JSONArray keys = tweet.getJSONObject("attachments").getJSONArray("media_keys");

                String author = tweet.getString("author_id");
                String tweetId = tweet.getString("id");
                String src = "https://twitter.com/" + author + "/status/" + tweetId;
                String title = tweet.getString("text");
                String stringDate = tweet.getString("created_at");
                // "2021-11-22T11:27:07.000Z"

                LocalDateTime createdTime = LocalDateTime.parse(stringDate.substring(0, stringDate.length() - 2));
                for (int j = 0; j < keys.length(); j++) {
                    if (urlsByMediaKeys.containsKey(keys.get(j))) {

                        String mediaUrl = urlsByMediaKeys.get(keys.get(j)).getString("url");

                        Post post = new Post();
                        post.setFeedNode(feedNode);
                        post.setSource(src);
                        post.setSource(src);
                        post.setMediaUrl(mediaUrl);
                        post.setTitle(title);
                        post.setLikes(0);
                        post.setPostedDate(createdTime);
                        posts.add(post);
                    }
                }

            }
        }

        return posts;
    }

    public static Map<String, JSONObject> getMediaKeys(JSONObject includes) {

        JSONArray array = includes.getJSONArray("media");

        Map<String, JSONObject> mediaByKeys = new HashMap<>();

        for (int i = 0; i < array.length(); i++) {

            JSONObject mediaObject = (JSONObject) array.get(i);
            if (mediaObject.getString("type").equals("photo")) {
                mediaByKeys.put(mediaObject.getString("media_key"), mediaObject);
            }

        }
        return mediaByKeys;
    }

    public static FeedNode getTwitterFeedByUserName(String userName, Platform twitter) {

        FeedNode userFeed = new FeedNode();

        Unirest.setTimeouts(0, 0);
        HttpResponse<JsonNode> response;
        try {

            response = Unirest.get(twitter.getUrl() + "/users/by/username/" + userName)
                    .header("Authorization",
                            "Bearer " + twitter.getBearerToken())
                    .asJson();

            userFeed = parseUser(response.getBody().getObject());
            userFeed.setPlatform(twitter);

        } catch (UnirestException e) {
            System.out.println("ERROR GETTING USER");
            e.printStackTrace();
        }

        return userFeed;

    }

    private static FeedNode parseUser(JSONObject data) {

        JSONObject user = data.getJSONObject("data");

        FeedNode userFeed = new FeedNode();

        userFeed.setUid(user.getString("id"));
        userFeed.setName(user.getString("username"));
        return userFeed;

    }

}
