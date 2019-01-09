package com.worksofbarry.sq.Listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.worksofbarry.sq.Quary;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockBuild(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        Location blockLoc = event.getBlock().getLocation();

        if (item.getItemMeta().hasDisplayName()) {
        	if (item.getItemMeta().getDisplayName().equals("Quary Block")) {
        		Quary.createQuary(blockLoc);
        	}
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	Block block = event.getBlock();
    	
    	switch (block.getType()) {
    	case OBSIDIAN:
    		if (Quary.isQuaryBlock(block.getLocation())) {
    			if (Quary.isActive(block.getLocation())) {
    				event.setCancelled(true);
    			} else {
	    			event.setDropItems(false);
	    			block.getLocation().getWorld().dropItemNaturally(block.getLocation(), Quary.GetQuaryBlock());
	    			Quary.deleteQuary(block.getLocation());
    			}
    		}
    		break;
    		
    	case BIRCH_FENCE:
    		if (Quary.isDrillPart(block.getLocation())) {
    			event.setCancelled(true);
    		}
    		break;
		default:
			break;
    	}
    }
}
