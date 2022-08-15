package com.c203.api.service;

import com.c203.api.dto.Feed.FeedRegistDto;
import com.c203.api.dto.Feed.FeedShowDto;

import java.util.Map;

public interface FeedService {
    Map showPicture(String decodeEmail, String roomIdx) throws Exception;
    FeedShowDto registFeed(FeedRegistDto feedRegistDto) throws Exception;
    boolean deleteFeed(String email,String roomIdx) throws Exception;
}
