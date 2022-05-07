package com.gabriel.tfg.scheduled;

import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.service.FeedNodeService;
import com.gabriel.tfg.service.PlatformService;
import com.gabriel.tfg.service.PostService;
import com.gabriel.tfg.utils.RedditUtils;
import com.gabriel.tfg.utils.RedditUtils.FILTER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class RedditTest {

    @Autowired
    private FeedNodeService feedNodeService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private PostService postService;

    // @Scheduled(fixedRate = 365 * 24 * 60 * 60 * 1000)
    public void main() {

        Platform reddit = platformService.getByName("reddit");

        List<FeedNode> feeds = feedNodeService.getAllByPlatform(reddit);

        for (FeedNode feedNode : feeds) {

            postService.insertOrUpdateAll(RedditUtils.getPosts(feedNode, FILTER.MONTH));

        }

    }

}
