package info.u_team.useful_backpacks.item;

import info.u_team.u_team_core.item.UItem;
import info.u_team.useful_backpacks.container.BackpackContainer;
import info.u_team.useful_backpacks.init.UsefulBackpacksItemGroups;
import info.u_team.useful_backpacks.inventory.BackpackInventory;
import info.u_team.useful_backpacks.type.Backpack;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BackpackItem extends UItem implements IDyeableArmorItem {
	
	private final Backpack backpack;
	
	public BackpackItem(Backpack backpack) {
		super("backpack_" + backpack.getName(), UsefulBackpacksItemGroups.group, new Properties().maxStackSize(1).rarity(backpack.getRarity()));
		this.backpack = backpack;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		final ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote && player instanceof ServerPlayerEntity) {
			NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
				
				@Override
				public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
					return new BackpackContainer(id, playerInventory, new BackpackInventory(stack, backpack.getInventorySize()), backpack);
				}
				
				@Override
				public ITextComponent getDisplayName() {
					return stack.getDisplayName();
				}
			}, buffer -> buffer.writeEnumValue(backpack));
		}
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return !ItemStack.areItemsEqual(oldStack, newStack);
	}
	
	// Item group
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (!isInGroup(group)) {
			return;
		}
		items.add(new ItemStack(this));
		for (final DyeColor color : DyeColor.values()) {
			final ItemStack dyedStack = new ItemStack(this, 1);
			setColor(dyedStack, color.getMapColor().colorValue);
			items.add(dyedStack);
		}
	}
	
}
