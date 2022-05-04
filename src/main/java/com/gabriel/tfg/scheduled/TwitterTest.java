package com.gabriel.tfg.scheduled;

import java.util.ArrayList;
import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.entity.Post;
import com.gabriel.tfg.service.FeedNodeService;
import com.gabriel.tfg.service.PlatformService;
import com.gabriel.tfg.service.PostService;
import com.gabriel.tfg.utils.TwitterUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class TwitterTest {

    @Autowired
    private FeedNodeService feedNodeService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private PostService postService;

    // @Scheduled(fixedRate = 365 * 24 * 60 * 60 * 1000)
    public void main() {

        Platform twitter = platformService.getByName("twitter");
        // FeedNode newUser = TwitterUtils.getTwitterFeedByUserName("adria2208_",
        // twitter);

        // feedNodeService.save(newUser);

        List<FeedNode> feeds = feedNodeService.getAllByPlatform(twitter);

        List<Post> posts = new ArrayList();
        for (FeedNode feedNode : feeds) {
            posts = TwitterUtils.getFeedFromUser(feedNode);
        }

        postService.insertOrUpdateAll(posts);

    }

}
