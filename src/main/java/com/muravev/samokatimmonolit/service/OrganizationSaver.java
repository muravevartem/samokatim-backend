package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.model.in.command.organization.*;

public interface OrganizationSaver {
    OrganizationEntity create(OrganizationCreateCommand command);

    OrganizationEntity addTariff(TariffAddCommand command);

    OrganizationEntity changeTariff(long id, TariffChangeCommand command);

    OrganizationEntity deleteTariff(TariffDeleteCommand command);

    OrganizationEntity changeLogo(OrganizationChangeLogoCommand command);
}
