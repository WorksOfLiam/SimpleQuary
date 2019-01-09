package com.worksofbarry.sq.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.worksofbarry.sq.Quary;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
    	
    	Player player = event.getPlayer();
        Location location;
        Block block;
        
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
        	player = event.getPlayer();
            block = event.getClickedBlock();
            location = block.getLocation();
            
        	if (!block.equals(null)) {            
	        	if (block.getType().equals(Material.OBSIDIAN)) {
	        		if (player.isSneaking()) {
		        		if (Quary.isQuaryBlock(location)) {
		        			player.openInventory(Quary.GetBoxInv(location));
		        		}
	        		}
	        	}
        	}
        }
    }
}

