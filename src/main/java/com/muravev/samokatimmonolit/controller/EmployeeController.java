package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.mapper.UserMapper;
import com.muravev.samokatimmonolit.model.in.command.employee.EmployeeInviteCommand;
import com.muravev.samokatimmonolit.model.out.EmployeeOut;
import com.muravev.samokatimmonolit.service.EmployeeReader;
import com.muravev.samokatimmonolit.service.EmployeeSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeSaver employeeSaver;
    private final EmployeeReader employeeReader;
    private final UserMapper userMapper;

    @GetMapping(params = {"page", "size"})
    @Secured("ROLE_LOCAL_ADMIN")
    public Page<EmployeeOut> findAll(@RequestParam String keyword,
                                     @RequestParam(name = "show_retired", defaultValue = "false") boolean showRetired,
                                     Pageable pageable) {
        return employeeReader.findAllColleagues(keyword, showRetired, pageable)
                .map(userMapper::toDto);
    }

    @GetMapping(value = "/{id}", params = {"colleague"})
    @Secured("ROLE_LOCAL_ADMIN")
    public EmployeeOut findColleagueById(@PathVariable long id) {
        EmployeeEntity employee = employeeReader.findByIdAsEmployee(id);
        return userMapper.toDto(employee);
    }

    @GetMapping(value = "/{id}")
    @Secured("ROLE_GLOBAL_ADMIN")
    public EmployeeOut findById(@PathVariable long id) {
        EmployeeEntity employee = employeeReader.findById(id);
        return userMapper.toDto(employee);
    }


    @PostMapping
    @Secured("ROLE_LOCAL_ADMIN")
    public EmployeeOut invite(@RequestBody @Valid EmployeeInviteCommand command) {
        EmployeeEntity invitedColleague = employeeSaver.inviteColleague(command);
        return userMapper.toDto(invitedColleague);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_LOCAL_ADMIN")
    public void deleleById(@PathVariable long id) {
        employeeSaver.delete(id);
    }

}
