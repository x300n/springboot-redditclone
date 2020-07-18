package org.ahmedgaber.Redditclone.repository;

import org.ahmedgaber.Redditclone.model.Post;
import org.ahmedgaber.Redditclone.model.User;
import org.ahmedgaber.Redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
