package org.ahmedgaber.Redditclone.service;


import lombok.AllArgsConstructor;
import org.ahmedgaber.Redditclone.dto.VoteDto;
import org.ahmedgaber.Redditclone.exceptions.PostNotFoundException;
import org.ahmedgaber.Redditclone.exceptions.SpringRedditException;
import org.ahmedgaber.Redditclone.model.Post;
import org.ahmedgaber.Redditclone.model.Vote;
import org.ahmedgaber.Redditclone.repository.PostRepository;
import org.ahmedgaber.Redditclone.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.ahmedgaber.Redditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {


    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) throws SpringRedditException {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
