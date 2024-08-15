package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.apiPayload.code.status.SuccessStatus;
import com.example.locavel.converter.TermConverter;
import com.example.locavel.converter.UserConverter;
import com.example.locavel.domain.Places;
import com.example.locavel.domain.User;
import com.example.locavel.domain.mapping.TermAgreement;
import com.example.locavel.service.NaverMapService;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;
    private final TermService termService;
    private final UserConverter userConverter;
    private final NaverMapService naverMapService;
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

    // 내 지역 관련
    @Operation(summary = "내 지역 등록", description = "내 지역을 등록합니다. 쿼리 스트링으로 위도, 경도 값을 주세요")
    @PostMapping("/api/users/my-area")
    public ApiResponse<UserResponseDto.MyAreaResponseDto> setUserArea(HttpServletRequest httpServletRequest, @RequestParam double latitude, @RequestParam double longitude) {
        // 위도 경도 정보를 통해 속한 구 이름 가져오기
        String distinct = naverMapService.getRoadNameAddress(latitude, longitude);
        User user = userCommandService.setMyArea(httpServletRequest, distinct);
        UserResponseDto.MyAreaResponseDto responseDto = userConverter.myAreaResponseDto(user);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "내 지역 변경", description = "내 지역을 변경합니다. 쿼리 스트링으로 위도, 경도 값을 주세요")
    @PatchMapping("/api/users/my-area")
    public ApiResponse<UserResponseDto.MyAreaResponseDto> updateUserArea(HttpServletRequest httpServletRequest, @RequestParam double latitude, @RequestParam double longitude) {
        String distinct = naverMapService.getRoadNameAddress(latitude, longitude);
        User user = userCommandService.setMyArea(httpServletRequest, distinct);
        UserResponseDto.MyAreaResponseDto responseDto = userConverter.myAreaResponseDto(user);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "내 지역에 속한 장소 조회", description = "내 지역에 속한 장소를 조회합니다.")
    @GetMapping("/api/users/my-area/places")
    public ApiResponse<List<PlaceResponseDTO.PlaceDetailDTO>> getPlaces(HttpServletRequest httpServletRequest) {
        User user = userCommandService.getUser(httpServletRequest);
        List<Places> placesList = user.getMy_area().getPlacesList();
        List<PlaceResponseDTO.PlaceDetailDTO> collect = placesList.stream().map(places ->
                PlaceResponseDTO.PlaceDetailDTO.builder()
                        .name(places.getName())
                        .placeId(places.getId())
                        .address(places.getAddress())   // 수정 필요
                        .generalRating(places.getRating())
                        .longitude(places.getLongitude())
                        .latitude(places.getLatitude())
                        //              .travelerRating()
                        .build()).collect(Collectors.toList());
        return ApiResponse.onSuccess(collect);
    }

    @Operation(summary = "유저 방문한 곳 조회", description = "마이페이지에서 유저가 방문한 장소 목록을 조회합니다.")
    @GetMapping("/api/users/mypage/places")
    public ApiResponse<PlaceResponseDTO.PlacePreviewListDTO> getUserVisit(HttpServletRequest httpServletRequest, @RequestParam(name="page")Integer page) {
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