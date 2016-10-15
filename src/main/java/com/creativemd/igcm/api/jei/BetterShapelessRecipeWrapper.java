package com.creativemd.igcm.api.jei;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapelessRecipe;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.plugins.vanilla.VanillaPlugin;
import mezz.jei.plugins.vanilla.crafting.AbstractShapelessRecipeWrapper;
import net.minecraft.item.ItemStack;

public class BetterShapelessRecipeWrapper extends AbstractShapelessRecipeWrapper  {
	
	@Nonnull
	private final BetterShapelessRecipe recipe;

	public BetterShapelessRecipeWrapper(@Nonnull IGuiHelper guiHelper, @Nonnull BetterShapelessRecipe recipe) {
		super(guiHelper);
		this.recipe = recipe;
	}

	@Nonnull
	@Override
	public List<ItemStack> getInputs() {
		return Arrays.asList(recipe.getInput());
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		return Collections.singletonList(recipe.getRecipeOutput());
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		IStackHelper stackHelper = VanillaPlugin.jeiHelpers.getStackHelper();

		List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(getInputs());
		ingredients.setInputLists(ItemStack.class, inputs);

		ItemStack recipeOutput = recipe.getRecipeOutput();
		if (recipeOutput != null) {
			ingredients.setOutput(ItemStack.class, recipeOutput);
		}
	}

	
}
