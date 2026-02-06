/*
 * Copyright 2021, OpenRemote Inc.
 *
 * See the CONTRIBUTORS.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openremote.manager.setup.custom;

import org.apache.commons.io.IOUtils;
import org.openremote.manager.rules.RulesService;
import org.openremote.manager.setup.ManagerSetup;
import org.openremote.model.Constants;
import org.openremote.model.Container;
import org.openremote.model.asset.impl.ThingAsset;
import org.openremote.model.rules.RealmRuleset;
import org.openremote.model.rules.Ruleset;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CustomManagerSetup extends ManagerSetup {

    private Container container;

    public CustomManagerSetup(Container container) {
        super(container);
        this.container = container;
    }

    @Override
    public void onStart() throws Exception {
        super.onStart();

        RulesService rules = container.getService(RulesService.class);

        try (InputStream inputStream = getClass().getResourceAsStream("/rules/austria-geofence.json")) {
            if (inputStream == null) {
                throw new IllegalStateException("Resource not found: /rules/austria-geofence.json");
            }
            String rule = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Ruleset ruleset = new RealmRuleset(
                    ReitingerKeycloakSetup.REALM_NAME, "Austria Geofence", Ruleset.Lang.JSON, rule
            ).setAccessPublicRead(true).setShowOnList(true);
            Long realmSmartCityRulesetId = rulesetStorageService.merge(ruleset).getId();
        }
    }
}
