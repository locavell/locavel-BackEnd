package com.example.locavel.service.userService;

import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.converter.UserConverter;
import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.Access;
import com.example.locavel.apiPayload.exception.handler.UserHandler;
import com.example.locavel.domain.enums.Grade;
import com.example.locavel.repository.PlaceRepository;
import com.example.locavel.repository.ReviewRepository;
import com.example.locavel.repository.UserRepository;
import com.example.locavel.service.jwtService.JwtService;
import com.example.locavel.web.controller.UserController;
import com.example.locavel.web.dto.PlaceDTO.PlaceRequestDTO;
import com.example.locavel.web.dto.ReviewDTO.ReviewRequestDTO;
import com.example.locavel.web.dto.UserDTO.UserRequestDto;
import com.example.locavel.web.dto.UserDTO.UserResponseDto;
import com.example.locavel.web.dto.UserDTO.UserSignUpDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;//PasswordEncoder 구현체 Spring Bean 등록으로 해결
    private final ReviewRepository reviewRepository;
    private final JwtService jwtService;
    private final PlaceRepository placeRepository;

    public void signUp(UserSignUpDto userSignUpDto) throws Exception{

        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일 입니다.");
        }

        if (userRepository.findByNickname(userSignUpDto.getNickname()).isPresent()){
            throw new Exception("이미 존재하는 닉내임 입니다.");
        }
        //추가적으로 전화번호로 찾는 로직 필요하면 추가할 예정

        User user = User.builder()
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .nickname(userSignUpDto.getNickname())
                .username(userSignUpDto.getUsername())
                .introduce(userSignUpDto.getIntroduce())
                .phone_num(userSignUpDto.getPhone_num())
                .access(Access.USER)
                .created_at(LocalDateTime.now())
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUserProfile(HttpServletRequest httpServletRequest, UserRequestDto.UpdateUserProfileDto updateUserProfileDto){
        String email = httpServletRequest.getUserPrincipal().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        if(updateUserProfileDto.getProfileImage() != null) user.setProfileImage(updateUserProfileDto.getProfileImage());

        return user;
    }

    @Override
    public User getUser(HttpServletRequest httpServletRequest){
        String email = httpServletRequest.getUserPrincipal().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    @Override
    public User findUser(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public User deleteUser(HttpServletRequest httpServletRequest){
        String email = httpServletRequest.getUserPrincipal().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        userRepository.delete(user);
        return user;
    }

    @Override
    @Transactional
    public User updateUser(HttpServletRequest httpServletRequest, UserRequestDto.UpdateUserDTO updateUserDTO){
        String email = httpServletRequest.getUserPrincipal().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        if(updateUserDTO.getName() != null) user.setUserName(updateUserDTO.getName());
        if(updateUserDTO.getNickname() != null) user.setNickname(updateUserDTO.getNickname());
        if(updateUserDTO.getIntroduce() != null) user.setIntroduce(updateUserDTO.getIntroduce());
        if(updateUserDTO.getPhone_num() != null) user.setPhone_num(updateUserDTO.getPhone_num());
        return user;
    }

    @Override
    public User getUserGrade(Long userId){
        User user = userRepository.findById(userId)

                .orElseThrow(() ->  new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return user;
    }

    @Override
    public User updateLocalGrade(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        int localScore = user.getLocalGradeScore(); //user의 로컬 점수를 가져옴
        localScore += 15; //15점 증가시킴
        user.setLocalGradeScore(localScore); //로컬 점수를 저장
        Grade localGrade = calculateLocalGrade(localScore); //로컬 점수를 통해 로컬 등급을 계산
        user.setLocalGrade(localGrade);//로컬 등급을 저장
        user = userRepository.save(user);//db에 저장하기

        return user;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Override
    public void updateMemberScoresDaily() {
        List<User> users = userRepository.findAll(); // 모든 회원 조회

        for (User user : users) {
            LocalDateTime certifiedAt = user.getCertified_at();
            if (certifiedAt != null) {
                // 인증된 날짜와 현재 날짜 사이의 차이를 계산
                long daysPassed = ChronoUnit.DAYS.between(certifiedAt, LocalDateTime.now());

                // 점수가 이미 계산된 마지막 날짜를 기준으로 경과한 30일 단위로 점수 부여
                long monthsPassed = daysPassed / 30; // 경과한 30일 단위 개수 계산
                if (monthsPassed > user.getLastCalculatedMonths()) {
                    int additionalScore = (int) ((monthsPassed - user.getLastCalculatedMonths()) * 10);
                    user.setLocalGradeScore(user.getLocalGradeScore() + additionalScore);
                    user.setLastCalculatedMonths((int) monthsPassed); // 마지막 계산된 개월 수 업데이트
                }

                // 업데이트 된 점수를 바탕으로 등급 계산
                Grade newGrade = calculateLocalGrade(user.getLocalGradeScore());
                user.setLocalGrade(newGrade); // 등급 업데이트

                // 변경 사항 저장
                userRepository.save(user);
            }
        }
    }

    @Override
    public User calculateTravelerGradeScore (Long userId, ReviewRequestDTO.ReviewDTO request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        int travelerScore = user.getTravelerGradeScore(); //user의 여행객 점수를 가져옴
        travelerScore += 10; //별점으로 인한 10점 증가

        if (!request.getComment().isEmpty()) {
            travelerScore += 15; // comment가 있을 경우 추가로 15점 더하기
        }
        user.setTravelerGradeScore(travelerScore); //로컬 점수를 저장
        Grade travelerGrade = calculateLocalGrade(travelerScore); //로컬 점수를 통해 로컬 등급을 계산
        user.setTravelerGrade(travelerGrade);//로컬 등급을 저장
        user = userRepository.save(user);//db에 저장하기

        return user;
        }

    @Override
    @Transactional
    public Grade calculateLocalGrade ( int score){
            if (score < 50) {
                return Grade.IRON;
            } else if (score < 100) {
                return Grade.BRONZE;
            } else if (score<300) {
                return Grade.SILVER;
            } else if (score<500) {
                return Grade.GOLD;
            } else if (score<1000){
                return Grade.DIAMOND;
            }
            else {
                return Grade.VIP;
            }
        }
    }


