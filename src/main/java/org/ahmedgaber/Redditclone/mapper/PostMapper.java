package org.ahmedgaber.Redditclone.mapper;


import org.ahmedgaber.Redditclone.dto.PostRequest;
import org.ahmedgaber.Redditclone.dto.PostResponse;
import org.ahmedgaber.Redditclone.model.Post;
import org.ahmedgaber.Redditclone.model.Subreddit;
import org.ahmedgaber.Redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);
}
