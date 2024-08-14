package com.example.locavel.service;

import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.exception.handler.FollowHandler;
import com.example.locavel.apiPayload.exception.handler.UserHandler;
import com.example.locavel.converter.FollowConverter;
import com.example.locavel.domain.Follow;
import com.example.locavel.domain.User;
import com.example.locavel.repository.FollowRepository;
import com.example.locavel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    public void createFollow(User user, User followUser) {
        if(user.getId().equals(followUser.getId())) {throw new UserHandler(ErrorStatus.USER_SAME);}
        if(isFollowing(user, followUser.getId())) {throw new FollowHandler(ErrorStatus.ALREADY_FOLLOWING);}

        Follow follow = FollowConverter.toFollow(user, followUser.getId());
        followRepository.save(follow);
        user.setFollowingCountPlus();
        followUser.setFollowerCountPlus();
        userRepository.save(user);
        userRepository.save(followUser);
    }
    public void deleteFollow(User user, User followUser) { // user : 팔로우하는 사람, followUser : 팔로우 당하는 유저
        if(user.getId().equals(followUser.getId())) {throw new UserHandler(ErrorStatus.USER_SAME);}
        if(!isFollowing(user, followUser.getId())) {throw new FollowHandler(ErrorStatus.FOLLOWING_NOT_FOUND);}
        else{
            Follow follow = followRepository.findByUserIdAndFollowUserId(user.getId(), followUser.getId());
            user.setFollowingCountMinus();
            followUser.setFollowerCountMinus();
            followRepository.delete(follow);
        }
    }
    public Page<User> getFollowingList(User user, Integer page) {
        List<Long> followingList = followRepository.findAllFollowUserIdByUserId(user.getId());
        Page<User> followingUserList = userRepository.findAllByIdIn(followingList, PageRequest.of(page,10));
        return followingUserList;
    }

    public Page<User> getFollowerList(User user, Integer page) {
        List<Long> followerList = followRepository.findAllUserIdByFollowUserId(user.getId());
        Page<User> followerUserList = userRepository.findAllByIdIn(followerList, PageRequest.of(page,10));
        return followerUserList;
    }

    public boolean isFollowing(User user, Long followUserId) {
        Follow follow = followRepository.findByUserIdAndFollowUserId(user.getId(), followUserId);
        if(follow == null) {return false;}
        else {return true;}
    }
}
