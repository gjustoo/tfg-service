package com.gabriel.tfg.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.entity.Post;
import com.gabriel.tfg.entity.constants.MEDIA_TYPE;
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
            rawPost.remove("all_awardings");
            rawPost.remove("thumbnail");


            Pattern videoPattern = Pattern.compile("(http(s?):).*(mp4)\"");
            Matcher videoMatch = videoPattern.matcher(rawPost.toString());
            String videoUrl = (videoMatch.matches())?  videoMatch.group():"";

            Pattern gifPattern = Pattern.compile("(http(s?):).*(gif)\"");
            Matcher gifMatch = gifPattern.matcher(rawPost.toString());
            String gifUrl = (gifMatch.matches())? gifMatch.group():"";

            Pattern imgPattern = Pattern.compile("(http(s?):).*(jpg|png)\"");
            Matcher imgMatch = imgPattern.matcher(rawPost.toString());
            String imgURL = (imgMatch.matches())? imgMatch.group():"";

            if (!videoUrl.isEmpty()) {
                post.setMediaUrl(videoUrl);
                post.setType(MEDIA_TYPE.VIDEO);
            } else if (!gifUrl.isEmpty()) {
                post.setMediaUrl(gifUrl);
                post.setType(MEDIA_TYPE.GIF);
            } else {

                post.setMediaUrl(imgURL);
                post.setType(MEDIA_TYPE.IMAGE);
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
