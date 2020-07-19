package org.ahmedgaber.Redditclone.repository;

import org.ahmedgaber.Redditclone.model.Post;
import org.ahmedgaber.Redditclone.model.Subreddit;
import org.ahmedgaber.Redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
}
