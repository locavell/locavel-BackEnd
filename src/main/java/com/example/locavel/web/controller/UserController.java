package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.apiPayload.code.status.SuccessStatus;
import com.example.locavel.converter.TermConverter;
import com.example.locavel.converter.UserConverter;
import com.example.locavel.domain.User;
import com.example.locavel.domain.mapping.TermAgreement;
import com.example.locavel.service.PlaceService;
import com.example.locavel.service.ReviewService;
import com.example.locavel.service.termService.TermService;
import com.example.locavel.service.userService.UserCommandService;
import com.example.locavel.web.dto.PlaceDTO.PlaceResponseDTO;
import com.example.locavel.web.dto.TermDTO.TermRequestDTO;
import com.example.locavel.web.dto.TermDTO.TermResponseDTO;
import com.example.locavel.web.dto.UserDTO.UserRequestDto;
import com.example.locavel.web.dto.UserDTO.UserResponseDto;
import com.example.locavel.web.dto.UserDTO.UserSignUpDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;
    private final TermService termService;
    private final UserConverter userConverter;
    private final PlaceService placeService;
    private final ReviewService reviewService;

    @PostMapping("/api/auth/sign-up")
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception{
        userCommandService.signUp(userSignUpDto);
        return "sign-up Success";
    }

    @GetMapping("/jwt-test")//테스트용
    public String jwtTest(){
        return "jwt-Test Success";
    }

    @PostMapping("/api/auth/profile")
    public ApiResponse<UserResponseDto.UpdateUserProfileResultDTO> updateUserProfile(HttpServletRequest httpServletRequest, @RequestBody UserRequestDto.UpdateUserProfileDto request){
        User user = userCommandService.updateUserProfile(httpServletRequest, request);
        return ApiResponse.of(SuccessStatus.USER_PROFILE_UPDATED, UserConverter.updateUserProfileResultDTO(user));
    }

    @GetMapping("/api/auth")
    public ApiResponse<UserResponseDto.getUserDTO> getUser(HttpServletRequest httpServletRequest){
        User user = userCommandService.getUser(httpServletRequest);
        return ApiResponse.of(SuccessStatus.USER_FOUND, userConverter.toGetUserResultDTO(user));
    }

    @DeleteMapping("/api/auth")
    public ApiResponse<UserResponseDto.DeleteUserResultDTO> deleteUser(HttpServletRequest httpServletRequest){
        User user = userCommandService.deleteUser(httpServletRequest);
        return ApiResponse.of(SuccessStatus.USER_DELETED, userConverter.toDeleteResultDTO(user));
    }

    @PatchMapping("/api/auth")
    public ApiResponse<UserResponseDto.UpdateUserDTO> updateUser(HttpServletRequest httpServletRequest, @RequestBody UserRequestDto.UpdateUserDTO updateUserDTO){
        User user = userCommandService.updateUser(httpServletRequest, updateUserDTO);
        return ApiResponse.of(SuccessStatus.USER_UPDATED, userConverter.toUpdateUserDTO(user));
    }

    //약관동의
    @PostMapping("/api/auth/terms")
    public ApiResponse<TermResponseDTO.TermAgreeResultDTO> termAgree(@RequestBody TermRequestDTO.TermAgreeDTO termAgreeDTO){
        List<TermAgreement> agree = termService.agree(termAgreeDTO);
        return ApiResponse.of(SuccessStatus.USER_TERM_AGREED, TermConverter.toTermAgreeResultDTO(agree));
    }

    //유저 등급 조회
    @Operation(summary = "유저 등급 조회", description = "유저의 등급을 조회합니다.")
    @GetMapping("/api/users/{userId}/grade")
    public ApiResponse<UserResponseDto.GradeResponseDto> getUserGrade(@PathVariable Long userId ){
        User user = userCommandService.getUserGrade(userId);
        UserResponseDto.GradeResponseDto response = userConverter.toGetUserGradeDTO(user);
        return ApiResponse.of(SuccessStatus.GRADE_GET_OK, response);
    }
    @Operation(summary = "유저 프로필 조회", description = "마이페이지에서 유저의 프로필을 조회합니다.")
    @GetMapping("/api/users/mypage/profile")
    public ApiResponse<UserResponseDto.UserProfileDTO> getUserProfile(HttpServletRequest httpServletRequest) {
        User user = userCommandService.getUser(httpServletRequest);
        UserResponseDto.UserProfileDTO response = userConverter.toUserProfileDTO(user);
        return ApiResponse.of(SuccessStatus.MYPAGE_PROFILE_GET_OK,response);
    }

    @Operation(summary = "유저 방문한 곳 조회", description = "마이페이지에서 유저가 방문한 장소 목록을 조회합니다.")
    @GetMapping("/api/users/mypage/places")
    public ApiResponse<PlaceResponseDTO.PlacePreviewListDTO> getUserVisit(
            HttpServletRequest httpServletRequest,
            @RequestParam(name="page")Integer page) {
        User user = userCommandService.getUser(httpServletRequest);
        PlaceResponseDTO.PlacePreviewListDTO response = placeService.getUserVisitPlaceList(user, page-1);
        return ApiResponse.of(SuccessStatus.MYPAGE_VISIT_GET_OK,response);
    }

    @Operation(summary = "유저 방문 기록 날짜별 조회", description = "마이페이지에서 유저가 방문한 장소가 있는 날짜를 달력으로 조회합니다.")
    @GetMapping("/api/users/mypage/calendar")
    @Parameters({
            @Parameter(name = "year", description = "조회할 연도, 입력하지 않으면 오늘날짜의 연도로 설정됩니다."),
            @Parameter(name = "month", description = "조회할 달, 입력하지 않으면 오늘날짜의 달로 설정됩니다."),
            @Parameter(name = "category", description = "food/spot/activity 중 입력, 입력하지 않으면 전체 조회입니다.")
    })
    public ApiResponse<UserResponseDto.VisitCalendarDTO> getUserVisitCalendar(
            HttpServletRequest httpServletRequest,
            @RequestParam(name="year", required = false)Integer year,
            @RequestParam(name="month", required = false)Integer month,
            @RequestParam(name="category", required = false)String category) {
        User user = userCommandService.getUser(httpServletRequest);
        UserResponseDto.VisitCalendarDTO response = reviewService.getVisitDayList(user, year, month, category);
        return ApiResponse.of(SuccessStatus.MYPATE_CALENDAR_GET_OK,response);
    }
}
