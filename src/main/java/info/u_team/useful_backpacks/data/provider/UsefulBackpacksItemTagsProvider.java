package info.u_team.useful_backpacks.data.provider;

import static info.u_team.useful_backpacks.init.UsefulBackpacksTags.Items.BACKPACK;

import info.u_team.u_team_core.data.CommonItemTagsProvider;
import static info.u_team.useful_backpacks.init.UsefulBackpacksItems.*;
import net.minecraft.data.DataGenerator;

public class UsefulBackpacksItemTagsProvider extends CommonItemTagsProvider {
	
	public UsefulBackpacksItemTagsProvider(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void registerTags() {
		getBuilder(BACKPACK).add(SMALL_BACKPACK, MEDIUM_BACKPACK, LARGE_BACKPACK, ENDERCHEST_BACKPACK);
	}
	
}