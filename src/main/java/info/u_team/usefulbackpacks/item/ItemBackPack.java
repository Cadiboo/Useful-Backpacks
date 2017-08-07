package info.u_team.usefulbackpacks.item;

import java.util.List;

import info.u_team.usefulbackpacks.*;
import info.u_team.usefulbackpacks.container.ContainerBackPack;
import info.u_team.usefulbackpacks.enums.EnumBackPacks;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemBackPack extends Item implements IItemColor {
	
	public ItemBackPack() {
		super();
		setMaxStackSize(1);
		hasSubtypes = true;
		setCreativeTab(ModMain.getInstance().getCreativeTabs().usefullbackpacks);
		setUnlocalizedName("backpack");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		EnumBackPacks type = EnumBackPacks.byMetadata(itemstack.getMetadata());
		return "item.backpack." + type.getName();
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs creativetab, List<ItemStack> list) {
		for (int i = 0; i < EnumBackPacks.values().length; i++) {
			ItemStack normalstack = new ItemStack(item, 1, i);
			list.add(normalstack);
		}
		for (EnumDyeColor color : EnumDyeColor.values()) {
			for (int i = 0; i < EnumBackPacks.values().length; i++) {
				ItemStack dyedstack = new ItemStack(item, 1, i);
				setColor(dyedstack, color.getMapColor().colorValue);
				list.add(dyedstack);
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		player.openGui(Reference.modid, 0, world, 0, 0, 0);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int itemslot, boolean isSelected) {
		if (world.isRemote || !isSelected) {
			return;
		}
		
		if (!(entity instanceof EntityPlayer)) {
			return;
		}
		
		EntityPlayer player = (EntityPlayer) entity;
		Container opencontainer = player.openContainer;
		
		if (opencontainer == null || !(opencontainer instanceof ContainerBackPack)) {
			return;
		}
		ContainerBackPack container = (ContainerBackPack) opencontainer;
		if (container.updateNotification) {
			container.saveToNBT(itemstack);
			container.updateNotification = false;
		}
	}
	
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		return getColor(stack);
	}
	
	public int getColor(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			NBTTagCompound displaytag = tag.getCompoundTag("display");
			if (displaytag != null && displaytag.hasKey("color", 3)) {
				return displaytag.getInteger("color");
			}
		}
		return 0x816040;
	}
	
	public void setColor(ItemStack stack, int color) {
		NBTTagCompound tag = stack.getTagCompound();
		
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		
		if (!tag.hasKey("display", 10)) {
			tag.setTag("display", new NBTTagCompound());
		}
		NBTTagCompound displaytag = tag.getCompoundTag("display");
		
		displaytag.setInteger("color", color);
		
		stack.setTagCompound(tag);
	}
	
	public boolean hasColor(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		return tag != null && tag.hasKey("display", 10) ? tag.getCompoundTag("display").hasKey("color", 3) : false;
	}
	
}
