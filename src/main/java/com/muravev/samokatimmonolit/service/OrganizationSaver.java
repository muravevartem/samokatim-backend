package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.model.in.command.organization.OrganizationCreateCommand;

public interface OrganizationSaver {
    OrganizationEntity create(OrganizationCreateCommand command);
}
