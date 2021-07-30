package me.ashenguard.agmcore;

import me.ashenguard.lib.events.DayCycleEvent;
import me.ashenguard.lib.events.equipment.ArmorListener;
import me.ashenguard.lib.events.equipment.DispenserListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

@SuppressWarnings("unused")
public class EventCaller implements Listener {
    private static final AGMCore core = AGMCore.getInstance();

    private static final int INTERVAL = 20;
    private static class DayCycleTask implements Runnable {
        @Override public void run() {
            for (World world: Bukkit.getServer().getWorlds()) {
                long delay = DayCycleEvent.Cycle.getCycle(world).getPassedTime(world);
                if (delay < INTERVAL) Bukkit.getServer().getPluginManager().callEvent(new DayCycleEvent(world));
            }
        }
    }
    public static BukkitTask DayCycleCaller = null;

    public static void activateArmorEquipEvent() {
        core.getServer().getPluginManager().registerEvents(new ArmorListener(core), core);
        try {
            core.getServer().getPluginManager().registerEvents(new DispenserListener(), core);
        } catch (Throwable ignored) {}
    }
    public static void activateDayCycleEvent() {
        if (DayCycleCaller != null) return;
        DayCycleCaller = Bukkit.getScheduler().runTaskTimer(AGMCore.getInstance(), new DayCycleTask(), 0, INTERVAL);
    }
    public static void deactivateDayCycleEvent(boolean confirm) {
        if (!confirm || DayCycleCaller == null) return;
        DayCycleCaller.cancel();
    }
}
