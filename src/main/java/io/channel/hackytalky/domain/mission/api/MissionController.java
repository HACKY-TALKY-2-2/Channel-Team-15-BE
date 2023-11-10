package io.channel.hackytalky.domain.mission.api;


import io.channel.hackytalky.domain.mission.dto.MissionAddDTO;
import io.channel.hackytalky.domain.mission.service.MissionService;
import io.channel.hackytalky.global.exception.BaseException;
import io.channel.hackytalky.global.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mission")
@Slf4j
public class MissionController {
    private final MissionService missionService;

    @PreAuthorize("#request.getremoteaddr().equals(#request.getlocaladdr())")
    @PostMapping("/add")
    public BaseResponse<String> addMission(@Valid @RequestBody MissionAddDTO missionAddDTO, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            return new BaseResponse<>(false, 400, message);
        }

        try {
            missionService.addMission(missionAddDTO);

            return new BaseResponse<>("SUCCESSFULLY ADDED MISSION");
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
