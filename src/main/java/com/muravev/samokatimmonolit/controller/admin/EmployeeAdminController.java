package com.muravev.samokatimmonolit.controller.admin;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.mapper.UserMapper;
import com.muravev.samokatimmonolit.model.in.command.employee.EmployeeInviteCommand;
import com.muravev.samokatimmonolit.model.out.EmployeeOut;
import com.muravev.samokatimmonolit.service.EmployeeReader;
import com.muravev.samokatimmonolit.service.EmployeeSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class EmployeeAdminController {
    private final EmployeeReader employeeReader;
    private final EmployeeSaver employeeSaver;

    private final UserMapper userMapper;


    @GetMapping("/orgs/{id}/employees")
    public Page<EmployeeOut> getAll(@PathVariable long id, Pageable pageable) {
        return employeeReader.findAll(id, pageable)
                .map(userMapper::toDto);
    }

    @PostMapping("/orgs/{id}/employees")
    public EmployeeOut invite(@PathVariable long id, @RequestBody @Valid EmployeeInviteCommand command) {
        EmployeeEntity employee = employeeSaver.invite(command, new OrganizationEntity().setId(id));
        return userMapper.toDto(employee);
    }
}
