package com.gabriel.tfg.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.entity.Post;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;

import org.json.JSONArray;
import org.json.JSONObject;

public class RedditUtils {

    private RedditUtils() {

    }

    public static String getAuthToken(Platform reddit) {

        if (!reddit.getName().equalsIgnoreCase("reddit")) {
            System.out.println("Invalid platform");
        }

        String bearer_token = "";
        String url = "https://www.reddit.com/api/v1/access_token";
        try {

            Unirest.setTimeouts(0, 0);
            MultipartBody request = Unirest.post(url)
                    .header("user-agent", "api/v1.0")
                    .header("Authorization", "Basic VTliOUVRQnVVUDdBeWc6SlhHd3g0Yk1KdnJjYnVYMUJiRVBncV8xTlNB")
                    .field("grant_type", "password")
                    .field("username", "ByJusto_ow")
                    .field("password", "gabrifabi23");

            HttpResponse<JsonNode> response = request.asJson();

            bearer_token = response.getBody().getObject().getString("access_token");
            reddit.setBearerToken(bearer_token);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return bearer_token;

    }

    public static List<Post> getPosts(FeedNode node, FILTER filter) {

        Platform reddit = node.getPlatform();
        String bearer = getAuthToken(reddit);
        List<Post> posts = new ArrayList();
        String url = reddit.getUrl() + "r/" + node.getUid() + filter.value;

        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("user-agent", "api/v1.0")
                    .header("Authorization", "Bearer " + bearer)
                    .asJson();

            posts = parsePosts(response.getBody().getObject(), node);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public static List<Post> parsePosts(JSONObject raw, FeedNode reddit) {

        JSONArray rawPosts = raw.getJSONObject("data").getJSONArray("children");

        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < rawPosts.length(); i++) {
            JSONObject rawPost = rawPosts.getJSONObject(i).getJSONObject("data");
            Post post = new Post();
            post.setBody(rawPost.getString("title"));
            post.setTitle(rawPost.getString("title"));
            post.setFeedNode(reddit);
            post.setSource("https://www.reddit.com" + rawPost.getString("permalink"));
            if (rawPost.has("media") && !rawPost.isNull("media")) {
                String mediaUrl = "";
                if (rawPost.getJSONObject("media").has("reddit_video")) {
                    mediaUrl = rawPost.getJSONObject("media").getJSONObject("reddit_video")
                            .getString("fallback_url");
                } else {
                    mediaUrl = rawPost.getJSONObject("secure_media_embed").getString("media_domain_url");
                }
                post.setMediaUrl(mediaUrl);
            } else {
                post.setMediaUrl(rawPost.getString("url"));
            }

            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(rawPost.getLong("created_utc")),
                    ZoneId.of("UTC"));
            post.setPostedDate(date);

            posts.add(post);
        }

        return posts;
    }

    public enum FILTER {

        NONE(""),
        HOUR("/top/?t=hour"),
        DAY("/top/?t=day"),
        WEEK("/top/?t=week"),
        MONTH("/top/?t=month"),
        YEAR("/top/?t=year"),
        ALL_TIME("/top/?t=all");

        private String value;

        private FILTER(String value) {
            this.value = value;
        }

    }
}
