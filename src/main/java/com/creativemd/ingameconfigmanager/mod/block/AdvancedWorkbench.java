package com.creativemd.ingameconfigmanager.mod.block;

import java.util.ArrayList;

import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.controls.gui.GuiTextfield;
import com.creativemd.ingameconfigmanager.api.common.machine.RecipeMachine;
import com.creativemd.ingameconfigmanager.api.common.segment.machine.AddRecipeSegment;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AdvancedWorkbench extends RecipeMachine<AdvancedGridRecipe>{

	public AdvancedWorkbench(ModTab tab, String name) {
		super(tab, name);
	}

	@Override
	public int getWidth() {
		return BlockAdvancedWorkbench.gridSize;
	}

	@Override
	public int getHeight() {
		return BlockAdvancedWorkbench.gridSize;
	}

	@Override
	public int getOutputCount() {
		return BlockAdvancedWorkbench.outputs;
	}
	
	@Override
	public boolean hasDisableBranch()
	{
		return false;
	}
	
	@Override
	public void addRecipeToList(AdvancedGridRecipe recipe) {
		BlockAdvancedWorkbench.recipes.add(recipe);
	}

	@Override
	public void clearRecipeList() {
		BlockAdvancedWorkbench.recipes.clear();
	}

	@Override
	public ItemStack[] getOutput(AdvancedGridRecipe recipe) {
		return recipe.output;
	}

	@Override
	public ArrayList<AdvancedGridRecipe> getAllExitingRecipes() {
		return new ArrayList<AdvancedGridRecipe>(BlockAdvancedWorkbench.recipes);
	}

	@Override
	public void fillGrid(ItemStack[] grid, AdvancedGridRecipe recipe) {
		
	}

	@Override
	public void fillGridInfo(StackInfo[] grid, AdvancedGridRecipe recipe) {
		for (int i = 0; i < recipe.input.length; i++) {
			int row = i/recipe.width;
			int index = row*getWidth()+(i-row*recipe.width);
			grid[index]= recipe.input[i];
		}
	}

	@Override
	public AdvancedGridRecipe parseRecipe(StackInfo[] input, ItemStack[] output, NBTTagCompound nbt, int width, int height) {
		if(input.length > 0 && output.length > 0)
			return new AdvancedGridRecipe(output, width, height, input, nbt.getInteger("duration"));
		return null;
	}
	
	@Override
	public void onBeforeSave(AdvancedGridRecipe recipe, NBTTagCompound nbt)
	{
		nbt.setInteger("duration", recipe.duration);
	}
	
	@Override
	public void parseExtraInfo(NBTTagCompound nbt, AddRecipeSegment segment, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls)
	{
		for (int i = 0; i < guiControls.size(); i++) {
			if(guiControls.get(i).is("duration"))
			{
				int duration = 0;
				try
				{
					duration = Integer.parseInt(((GuiTextfield)guiControls.get(i)).text);
				}catch(Exception e){
					duration = 0;
				}
				nbt.setInteger("duration", duration);
			}
		}
	}
	
	@Override
	public void onControlsCreated(AdvancedGridRecipe recipe, boolean isAdded, int x, int y, int maxWidth, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls)
	{
		if(isAdded)
		{
			guiControls.add(new GuiTextfield("duration", recipe != null ? "" + recipe.duration : "0", x+maxWidth-80, y, 70, 20).setNumbersOnly());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getAvatar() {
		return new ItemStack(Blocks.CRAFTING_TABLE);
	}

	@Override
	public boolean doesSupportStackSize() {
		return true;
	}

}
