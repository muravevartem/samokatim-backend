package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.model.in.command.organization.OrganizationCreateCommand;
import com.muravev.samokatimmonolit.model.in.command.organization.TariffAddCommand;
import com.muravev.samokatimmonolit.model.in.command.organization.TariffDeleteCommand;

public interface OrganizationSaver {
    OrganizationEntity create(OrganizationCreateCommand command);

    OrganizationEntity addTariff(TariffAddCommand command);

    OrganizationEntity deleteTariff(TariffDeleteCommand command);
}
