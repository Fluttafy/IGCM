package com.creativemd.igcm.utils;

import com.creativemd.creativecore.common.utils.stack.InfoStack;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SearchUtils {
	
	public static boolean canStackBeFound(ItemStack stack, String search)
	{
		if(stack.getItem() instanceof ItemBlock)
			if(Block.REGISTRY.getNameForObject(Block.getBlockFromItem(stack.getItem())).toString().toLowerCase().contains(search))
				return true;
		else
			if(Item.REGISTRY.getNameForObject(stack.getItem()).toString().toLowerCase().contains(search))
				return true;
		
		return stack.getDisplayName().toLowerCase().contains(search);
	}
	
	public static boolean canInfoBeFound(InfoStack info, String search)
	{
		return canStackBeFound(info.getItemStack(), search);
	}
	
}