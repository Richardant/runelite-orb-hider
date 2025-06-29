package com.orbhider;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
	name = "Orb Hider",
	description = "Hides minimap orbs",
	tags = {"orbs", "orb", "minimap", "spec", "hide"}
)
public class OrbHiderPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private OrbHiderConfig config;

    @Override
    protected void startUp() throws Exception
    {
        updateAllOrbs();
    }

    @Override
    protected void shutDown() throws Exception
    {
        showAllOrbs();
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event)
    {
        if (event.getGroupId() == InterfaceID.ORBS || event.getGroupId() == InterfaceID.TOPLEVEL_OSRS_STRETCH || event.getGroupId() == InterfaceID.TOPLEVEL_PRE_EOC)
        {
            updateAllOrbs();
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        updateAllOrbs();
    }

    @Provides
    OrbHiderConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(OrbHiderConfig.class);
    }

    public void updateAllOrbs()
    {
        updateOrb(client.getWidget(InterfaceID.Orbs.XP_DROPS), config.hideXpOrb());
        updateOrb(client.getWidget(InterfaceID.Orbs.ORB_HEALTH), config.hideHealthOrb());
        updateOrb(client.getWidget(InterfaceID.Orbs.ORB_PRAYER), config.hidePrayerOrb());
        updateOrb(client.getWidget(InterfaceID.Orbs.ORB_RUNENERGY), config.hideRunOrb());
        updateOrb(client.getWidget(InterfaceID.Orbs.ORB_SPECENERGY), config.hideSpecOrb());
        updateOrb(client.getWidget(InterfaceID.Orbs.ORB_WORLDMAP), config.hideWorldMapOrb());

        updateOrb(client.getWidget(InterfaceID.TOPLEVEL_OSRS_STRETCH, 22), config.hideWorldMap());

        for (int i = 23; i <= 32; i++)
        {
            updateOrb(client.getWidget(InterfaceID.TOPLEVEL_PRE_EOC, i), config.hideWorldMap());
        }
    }

    public void showAllOrbs()
    {
        updateOrb(client.getWidget(InterfaceID.Orbs.XP_DROPS), false);
        updateOrb(client.getWidget(InterfaceID.Orbs.ORB_HEALTH),false);
        updateOrb(client.getWidget(InterfaceID.Orbs.ORB_PRAYER), false);
        updateOrb(client.getWidget(InterfaceID.Orbs.ORB_RUNENERGY), false);
        updateOrb(client.getWidget(InterfaceID.Orbs.ORB_SPECENERGY), false);
        updateOrb(client.getWidget(InterfaceID.Orbs.ORB_WORLDMAP), false);

        updateOrb(client.getWidget(InterfaceID.TOPLEVEL_OSRS_STRETCH,22), false);

        for (int i = 23; i <= 32; i++)
        {
            updateOrb(client.getWidget(InterfaceID.TOPLEVEL_PRE_EOC, i), false);
        }
    }

    public void updateOrb(Widget orb, boolean hidden)
    {
        if (orb != null)
        {
            orb.setHidden(hidden);
        }
    }
}
