package com.worksofbarry.sq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Quary {

	public static Map<String, Quary> List = new HashMap<String, Quary>();

	public static String LocationString(Location BlockLocation) {
		String result = BlockLocation.getWorld().getName() + "," + 
	                    String.valueOf(BlockLocation.getBlockX()) + "," +
	                    String.valueOf(BlockLocation.getBlockY()) + "," +
	                    String.valueOf(BlockLocation.getBlockZ());
		
		return result;
	}

	public static void createQuary(Location loc) {
		List.put(LocationString(loc), new Quary(loc));
	}
	
	public static boolean isQuaryBlock(Location loc) {
		return List.containsKey(LocationString(loc));
	}
	
	public static void deleteQuary(Location loc) {
		String key = LocationString(loc);
		if (List.containsKey(key)) {
			List.remove(key);
		}
	}
	
	public static ItemStack GetQuaryBlock() {
		ItemStack item = new ItemStack(Material.OBSIDIAN); 
        ItemMeta im = item.getItemMeta(); 
        im.setDisplayName("Quary Block"); 
        List<String> lore = new ArrayList<String>(); 
        lore.add("When powered, this block"); 
        lore.add("will dig downwards when powered.");
        im.setLore(lore); 
        item.setItemMeta(im); 
        return item;
	}
	
	public static boolean isActive(Location loc) {
		double y = List.get(LocationString(loc)).getDrillY();
		return (y > 0);
	}
	
	public static Inventory GetBoxInv(Location loc) {
		return List.get(LocationString(loc)).GetBox();
	}
	
	public static boolean isDrillPart(Location loc) {
		int fenceCount = 0;
		while (true) {
			switch (loc.getBlock().getType()) {
			case BIRCH_FENCE:
				loc.setY(loc.getY() + 1);
				fenceCount++;
				
				if (fenceCount >= 10)
					return true; //Stop counting after 6 fences to improve performance
				break;
			case OBSIDIAN:
				return (isQuaryBlock(loc));
			default:
				return false;
			}
		}
	}
	
	private Location BlockLocation;
	private double DrillY; //Relative to BlockLocation.Y
	private Inventory box;

	public Quary(Location key) {
		this.BlockLocation = key;
		this.DrillY = 0;
		box = Bukkit.createInventory(null, 18, "Quary Block Inventory");
	}
	
	public Quary(Location key, double drillY) {
		this.BlockLocation = key;
		this.DrillY = drillY;
		box = Bukkit.createInventory(null, 18, "Quary Block Inventory");
	}

	public Location getBlockLocation() {
		return BlockLocation;
	}
	public double getDrillY() {
		return DrillY;
	}
	public Inventory GetBox() {
		return box;
	}
	
	public void DoWork() {
		Block quaryBlock = BlockLocation.getBlock(), drillBlock;
		Location drillLocation;

		drillLocation = BlockLocation.clone();
		
		if (quaryBlock.isBlockIndirectlyPowered()) {
			drillLocation.setY(BlockLocation.getY() - (DrillY+1));
			
			drillBlock = drillLocation.getBlock();
			
			if (!drillBlock.getType().equals(Material.BEDROCK)) {
				for (ItemStack item : drillBlock.getDrops())
					box.addItem(item);
				
				drillBlock.setType(Material.BIRCH_FENCE);
				DrillY++;
			}
			
		} else {
			if (DrillY >= 1) {
				drillLocation.setY(BlockLocation.getY() - DrillY);
				drillLocation.getBlock().setType(Material.AIR);
				this.DrillY--;
			}
		}
	}
}
