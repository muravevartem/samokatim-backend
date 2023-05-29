package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.OfficeEntity;
import com.muravev.samokatimmonolit.model.in.command.office.OfficeCreateCommand;
import com.muravev.samokatimmonolit.model.in.command.office.OfficeScheduleDayModifyCommand;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface OfficeWriter {
    OfficeEntity create(@RequestBody @Valid OfficeCreateCommand command);

    OfficeEntity modifyScheduleDay(long officeId, OfficeScheduleDayModifyCommand command);
}
