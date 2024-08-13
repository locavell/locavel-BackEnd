package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.code.status.SuccessStatus;
import com.example.locavel.apiPayload.exception.handler.UserHandler;
import com.example.locavel.converter.FollowConverter;
import com.example.locavel.domain.User;
import com.example.locavel.repository.UserRepository;
import com.example.locavel.service.FollowService;
import com.example.locavel.service.userService.UserCommandService;
import com.example.locavel.web.dto.FollowDTO.FollowRequestDTO;
import com.example.locavel.web.dto.FollowDTO.FollowResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowRestController {
    private final UserCommandService userCommandService;
    private final FollowService followService;
    private final UserRepository userRepository;
    @Operation(summary = "팔로잉", description = "유저를 팔로우합니다.")
    @PostMapping
    public ApiResponse createFollow(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody FollowRequestDTO.FollowDTO request) {
        User user = userCommandService.getUser(httpServletRequest);
        User followUser = userRepository.findById(request.getFollowUserId())
                        .orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));
        followService.createFollow(user, followUser);
        return ApiResponse.of(SuccessStatus.FOLLOW_OK,null);
    }
    @Operation(summary = "팔로잉 삭제", description = "본인이 팔로우 중인 유저를 삭제합니다.")
    @DeleteMapping("/followings")
    public ApiResponse deleteFollowing(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody FollowRequestDTO.FollowDTO request) {
        User user = userCommandService.getUser(httpServletRequest);
        User followUser = userRepository.findById(request.getFollowUserId())
                .orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));
        followService.deleteFollow(user, followUser);
        return ApiResponse.of(SuccessStatus.FOLLOW_DELETE_OK,null);
    }
    @Operation(summary = "팔로워 삭제", description = "본인을 팔로우하는 유저를 삭제합니다.")
    @DeleteMapping("/followers")
    public ApiResponse deleteFollower(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody FollowRequestDTO.FollowDTO request) {
        User user = userCommandService.getUser(httpServletRequest);
        User followUser = userRepository.findById(request.getFollowUserId())
                        .orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));
        followService.deleteFollow(followUser,user);
        return ApiResponse.of(SuccessStatus.FOLLOW_DELETE_OK,null);
    }

    @Operation(summary = "팔로잉 목록 조회", description = "유저가 팔로우 중인 유저 목록을 조회합니다.")
    @GetMapping("/followings")
    public ApiResponse<FollowResponseDTO.FollowPreviewListDTO> getFollowings(
            HttpServletRequest httpServletRequest,
            @RequestParam(name = "page") Integer page) {
        User user = userCommandService.getUser(httpServletRequest);
        Page<User> followingList = followService.getFollowingList(user, page-1);
        FollowResponseDTO.FollowPreviewListDTO response = FollowConverter.toFollowPreviewListDTO(followingList);
        return ApiResponse.of(SuccessStatus.FOLLOW_GET_OK,response);
    }
    @Operation(summary = "팔로워 목록 조회", description = "유저를 팔로우 중인 유저 목록을 조회합니다.")
    @GetMapping("/followers")
    public ApiResponse<FollowResponseDTO.FollowPreviewListDTO> getFollowers(
            HttpServletRequest httpServletRequest,
            @RequestParam(name = "page") Integer page) {
        User user = userCommandService.getUser(httpServletRequest);
        Page<User> followerList = followService.getFollowerList(user, page-1);
        FollowResponseDTO.FollowPreviewListDTO response = FollowConverter.toFollowPreviewListDTO(followerList);
        return ApiResponse.of(SuccessStatus.FOLLOW_GET_OK,response);
    }
}
