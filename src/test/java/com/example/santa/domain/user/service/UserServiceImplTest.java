package com.example.santa.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.dto.UserSignInRequestDto;
import com.example.santa.domain.user.dto.UserSignupRequestDto;
import com.example.santa.domain.user.dto.UserUpdateRequestDto;
import com.example.santa.domain.user.entity.Password;
import com.example.santa.domain.user.entity.Role;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.global.exception.ServiceLogicException;
import com.example.santa.global.security.jwt.JwtToken;
import com.example.santa.global.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

//    @Mock
//    private HttpServletRequest authenticationManager;



    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserSignupRequestDto signupRequest;
    private UserSignInRequestDto signInRequest;
    private JwtToken jwtToken;
    private UsernamePasswordAuthenticationToken authenticationToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Initialize commonly used objects and mock responses
        user = new User();
        user.setEmail("test@email.com");
        user.setName("t");
        user.setNickname("d");
        user.setId(1L);
        user.setPhoneNumber("01055555555");
        user.setPassword(new Password("password"));
        user.setRole(Role.USER);

        signupRequest = new UserSignupRequestDto();
        signupRequest.setEmail("test@email.com");
        signupRequest.setName("t");
        signupRequest.setNickname("d");
        signupRequest.setPhoneNumber("01055555555");
        signupRequest.setPassword("password");

        jwtToken = JwtToken.builder()
                .grantType("Bearer")
                .accessToken("rrrr")
                .refreshToken("rrrr")
                .role("USER")
                .build();

        signInRequest = new UserSignInRequestDto();
        signInRequest.setEmail("test@email.com");
        signInRequest.setPassword("password");

        authenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword(), user.getAuthorities());

//        lenient().when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));


        // Setting up the AuthenticationManager mock
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),
                signInRequest.getPassword(),
                user.getAuthorities()
        );

        // userRepository 모의 동작 설정
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // authenticationManager 모의 동작 설정
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authenticationToken);

        // jwtTokenProvider 모의 동작 설정
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn(jwtToken);

//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        signInRequest.getEmail(),
//                        signInRequest.getPassword(),
//                        user.getAuthorities()));

//        lenient().when(jwtTokenProvider.generateToken((authentication))).thenReturn(
//                        JwtToken.builder()
//                                .grantType("Bearer")
//                                .accessToken("rrrr")
//                                .refreshToken("rrrr")
//                                .role("USER")
//                .build());


    }

    @Test
    public void testSignupWithDuplicateEmail() {
        // Corrected email in stubbing to match the test case
        when(userRepository.existsByEmail("test11@email.com")).thenReturn(true);

        // When
        Exception exception = assertThrows(ServiceLogicException.class, () -> userService.signup(signupRequest));

        // Then
        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }

    @Test
    public void testSignupSuccess() {
        // Corrected stubs to match the email used in the signupRequest
//        when(userRepository.existsByEmail("test11@email.com")).thenReturn(false);
//        when(userRepository.existsByNickname("d")).thenReturn(false);
//        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        Long userId = userService.signup(signupRequest);

        // Then
        assertNotNull(userId);
    }

    @Test
    public void testSignInSuccess() {
        // Given
        // Mocks and user object set in @BeforeEach

        // When
        JwtToken jwtToken = userService.signIn(signInRequest);

        // Then
        assertNotNull(jwtToken);
        assertEquals("Bearer", jwtToken.getGrantType());
    }

    @Test
    void signIn_withValidUser_returnsJwtToken() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // 실행
        JwtToken returnedJwtToken = userService.signIn(signInRequest);

        System.out.println(user);
        // 검증
        assertNotNull(returnedJwtToken);
        assertEquals(jwtToken, returnedJwtToken);

        // verify()를 사용하여 실제로 예상되는 메서드 호출이 이루어졌는지 확인할 수 있습니다.
        verify(userRepository).findByEmail(anyString());
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtTokenProvider).generateToken(any(Authentication.class));
    }

    @Test
    public void testUpdateUserSuccess() {
        // Given
        UserUpdateRequestDto request = new UserUpdateRequestDto();
        request.setNickname("NewNick");
        request.setName("NewName");
        request.setPhoneNumber("9876543210");
        request.setImage("/images/newProfile.png");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserResponseDto updatedUser = userService.updateUser("test@example.com", request);

        // Then
        assertEquals("NewName", updatedUser.getName());
    }
}