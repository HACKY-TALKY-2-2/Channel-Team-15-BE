package io.channel.hackytalky.domain.user.api;

import io.channel.hackytalky.domain.doongdoong.dto.ClearMissionResponseDTO;
import io.channel.hackytalky.domain.user.dto.LoginRequestDTO;
import io.channel.hackytalky.domain.user.dto.SignupRequestDTO;
import io.channel.hackytalky.domain.user.service.UserService;
import io.channel.hackytalky.global.exception.BaseException;
import io.channel.hackytalky.global.response.BaseResponse;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public BaseResponse<String> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            return new BaseResponse<>(false, 400, message);
        }

        try {
            userService.signup(signupRequestDTO);
            return new BaseResponse<>("SUCCESSFULLY SIGNED UP");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PostMapping("/login")
    public BaseResponse<String> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            return new BaseResponse<>(false, 400, message);
        }

        try {
            userService.login(loginRequestDTO, response);
            return new BaseResponse<>("SUCCESSFULLY LOGGED IN");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request) {
        try {
            userService.logout(request);
            return new BaseResponse<>("SUCCESSFULLY LOGGED OUT");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/withdraw")
    public BaseResponse<String> withdraw(@Valid @RequestBody String password, HttpServletRequest request) {
        try {
            userService.withDraw(request, password);
            return new BaseResponse<>("SUCCESSFULLY WITHDREW");
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/doongdoongInfo")
    public BaseResponse<?> getDoongdoongInformation(HttpServletRequest request) {
        try {
            return new BaseResponse<>(userService.getDoongdoongInformation(request));
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping(value = "/clearMission/{missionId}")
    public BaseResponse<?> gainExperiment(@PathVariable Long missionId,
                                          HttpServletRequest request) {
        try {
            ClearMissionResponseDTO resultDTO = userService.clearMission(request, missionId, null);

            return new BaseResponse<>(resultDTO);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping(value = "/clearImageMission/{missionId}")
    public BaseResponse<?> gainExperimentWithImage(@PathVariable Long missionId,
                                                   @RequestParam("imageFile") MultipartFile imageFile,
                                                   HttpServletRequest request) {
        try {
            ClearMissionResponseDTO resultDTO = userService.clearMission(request, missionId, imageFile);

            return new BaseResponse<>(resultDTO);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}