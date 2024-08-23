package baguchan.chamber_bta.world.gen;

import baguchan.chamber_bta.ChamberBTA;
import baguchan.structure_lib.util.Direction;
import baguchan.structure_lib.util.RevampStructure;
import baguchan.structure_lib.util.Vec3i;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrialChamberGen extends AbstractTrialChamberGen {
	private static final int DUNGEON_SIZE = 15;
	int dungeonSize = 0;
	boolean trialGenerated = false;
	public ItemStack treasureItem;
	public WeightedRandomBag<WeightedRandomLootObject> chestLoot;
	public WeightedRandomBag<WeightedRandomLootObject> dispenserLoot;
	public List<AABB> list = new ArrayList<>();

	public TrialChamberGen() {
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		Biome biome = world.getBlockBiome(x, y, z);
		this.chestLoot = new WeightedRandomBag();
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.ingotIron.getDefaultStack(), 1, 6), 100.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.ingotGold.getDefaultStack(), 1, 4), 100.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.sulphur.getDefaultStack(), 3, 8), 100.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.diamond.getDefaultStack(), 1, 4), 2.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.foodAppleGold.getDefaultStack()), 1.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.dustRedstone.getDefaultStack(), 1, 4), 100.0);
		for (int i = 0; i < 9; ++i) {
			this.chestLoot.addEntry(new WeightedRandomLootObject(new ItemStack(Item.itemsList[Item.record13.id + i])), 1.0);
		}
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.foodApple.getDefaultStack()), 100.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Block.spongeDry.getDefaultStack(), 1, 4), 100.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.handcannonLoaded.getDefaultStack()), 0.5);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.handcannonUnloaded.getDefaultStack()), 4.5);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.armorHelmetChainmail.getDefaultStack()).setRandomMetadata(Item.armorHelmetChainmail.getMaxDamage() / 2, Item.armorHelmetChainmail.getMaxDamage()), 20.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.armorChestplateChainmail.getDefaultStack()).setRandomMetadata(Item.armorChestplateChainmail.getMaxDamage() / 2, Item.armorChestplateChainmail.getMaxDamage()), 20.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.armorLeggingsChainmail.getDefaultStack()).setRandomMetadata(Item.armorLeggingsChainmail.getMaxDamage() / 2, Item.armorLeggingsChainmail.getMaxDamage()), 20.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.armorBootsChainmail.getDefaultStack()).setRandomMetadata(Item.armorBootsChainmail.getMaxDamage() / 2, Item.armorBootsChainmail.getMaxDamage()), 20.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(Item.ingotSteelCrude.getDefaultStack()), 10.0);
		this.chestLoot.addEntry(new WeightedRandomLootObject(null), 892.0);
		this.dispenserLoot = new WeightedRandomBag();
		this.dispenserLoot.addEntry(new WeightedRandomLootObject(Item.ammoArrow.getDefaultStack(), 5, 7), 300.0);
		this.dispenserLoot.addEntry(new WeightedRandomLootObject(Item.ammoArrowGold.getDefaultStack()), 10.0);
		this.dispenserLoot.addEntry(new WeightedRandomLootObject(Item.ammoChargeExplosive.getDefaultStack()), 0.5);
		this.dispenserLoot.addEntry(new WeightedRandomLootObject(null), 289.5);
		this.treasureItem = Item.armorQuiverGold.getDefaultStack();

		this.generateStart(world, random, x, y, z);
		return true;

	}

	public void generateStart(World world, Random random, int blockX, int blockY, int blockZ) {
		RevampStructure revampStructure = new RevampStructure(ChamberBTA.MOD_ID, "chamber_stairs", "chamber_start", true, true);
		revampStructure.placeStructure(world, blockX, blockY - 9, blockZ);

		for (int i = 0; i < 3; i++) {
			Direction direction1 = Direction.getDirectionFromSide(2 + random.nextInt(Direction.values().length - 2));
			int xSize = 3;
			int zSize = 3;
			int trueXSize = 5;
			int trueZSize = 5;
			Vec3i vec3i = transform(new Vec3i(xSize, 0, zSize), direction1, new Vec3i(2, 0, 2));
			int directionX = direction1.getAxis() == Axis.X ? (trueXSize) * direction1.getVec().x : 0;
			int directionZ = direction1.getAxis() == Axis.Z ? (trueZSize) * direction1.getVec().z : 0;

			Vec3i offset = new Vec3i(-directionZ, 0, -directionX);
			Vec3i origin = new Vec3i(blockX, blockY - 8 - 5, blockZ);
			Vec3i vec3i1 = origin.add(vec3i).add(offset);
			generateStairsDown(world, random, vec3i1, origin, direction1);
		}
	}

	public static Vec3i transform(Vec3i pos, Direction direction, Vec3i pivot) {
		return pos.rotate(pivot, direction);
	}

	@Nullable
	protected void generateStructureTemplate(
		Direction direction, World world, String name, Vec3i generatePos, Vec3i origin
	) {
		net.minecraft.core.util.helper.Direction direction1 = direction.getSide().getDirection();
		if (direction1 != null) {
			RevampStructure revampStructure = new RevampStructure(ChamberBTA.MOD_ID, name, name, true, true);

			if (!isCollide(origin, generatePos)) {
				revampStructure.placeStructure(world,
					generatePos.x,
					generatePos.y,
					generatePos.z, direction.getName());

				list.add(new AABB(generatePos.x, generatePos.y, generatePos.z, generatePos.x, generatePos.y, generatePos.z));

			}
		}
	}


	public void generateStairsDown(World world, Random random, Vec3i generatePos, Vec3i originPos, Direction
		direction) {
		generateStructureTemplate(direction, world, "corridor_stair_down", generatePos, originPos);

		/*if(random.nextBoolean()){

			generateStairsDown();
		}*/
	}


	public boolean isCollide(Vec3i origin, Vec3i rotate) {
		if (list.stream().anyMatch(aabb -> {
			return aabb.isVecInside(Vec3d.createVector(origin.x, origin.y, origin.z)) || aabb.isVecInside(Vec3d.createVector(rotate.x, rotate.y, rotate.z));
		})) {
			return true;
		}
		return false;
	}

	private ItemStack pickDispenserLootItem(Random random) {
		return this.dispenserLoot.getRandom(random).getItemStack(random);
	}

	private ItemStack pickCheckLootItem(Random random) {
		if (!this.trialGenerated && this.dungeonSize > 7) {
			this.trialGenerated = true;
			return this.treasureItem.copy();
		}
		return this.chestLoot.getRandom(random).getItemStack(random);
	}
}

