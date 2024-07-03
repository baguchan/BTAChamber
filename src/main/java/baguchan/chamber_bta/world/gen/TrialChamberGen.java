package baguchan.chamber_bta.world.gen;

import baguchan.chamber_bta.block.ModBlocks;
import baguchan.chamber_bta.tileentity.TileEntityTrialMobSpawner;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;

import java.util.Random;

public class TrialChamberGen extends AbstractTrialChamberGen {
	private static final int DUNGEON_SIZE = 15;
	int dungeonSize = 0;
	int dungeonLimit;
	int dungeonCount = 0;
	boolean treasureGenerated = false;
	boolean libraryGenerated = false;
	boolean isCold = false;
	int wallBlockA;
	int wallBlockB;
	int brickBlockA;
	int brickBlockB;
	int slabBlock;
	public ItemStack treasureItem;
	public WeightedRandomBag<WeightedRandomLootObject> chestLoot;
	public WeightedRandomBag<WeightedRandomLootObject> dispenserLoot;
	public WeightedRandomBag<String> spawnerMonsters;

	public TrialChamberGen() {
		this.wallBlockA = Block.brickStone.id;
		this.wallBlockB = Block.stonePolished.id;
		this.brickBlockA = Block.brickStonePolished.id;
		this.brickBlockB = Block.brickStonePolishedMossy.id;
		this.slabBlock = Block.slabStonePolished.id;
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
		this.spawnerMonsters = new WeightedRandomBag();
		this.spawnerMonsters.addEntry("Skeleton", 1.0);
		this.spawnerMonsters.addEntry("Zombie", 1.0);
		this.spawnerMonsters.addEntry("Breeze", 1.0);
		this.treasureItem = Item.armorQuiverGold.getDefaultStack();

		if (this.canReplace(world, x, y, z)) {
			this.dungeonLimit = 3;
			this.generateBranch(world, random, x, y, z);
			return true;
		}
		return false;
	}

	public void generateBranch(World world, Random random, int blockX, int blockY, int blockZ) {
		boolean generateTrapOnWall = false;
		int wallWidth = 3;
		int wallHeight = 4;
		for (int x = blockX - wallWidth; x <= blockX + wallWidth; ++x) {
			boolean xWallCheck = x == blockX - wallWidth || x == blockX + wallWidth;
			for (int y = blockY - wallHeight; y <= blockY + 1; ++y) {
				boolean yWallCheck = y == blockY - wallWidth;
				for (int z = blockZ - wallWidth; z <= blockZ + wallWidth; ++z) {
					boolean zWallCheck;
					boolean bl = zWallCheck = z == blockZ - wallWidth || z == blockZ + wallWidth;
					if (!this.canReplace(world, x, y, z)) continue;
					if (xWallCheck && zWallCheck) {
						world.setBlockWithNotify(x, y, z, random.nextInt(4) == 0 ? this.brickBlockB : this.brickBlockA);
					} else if (xWallCheck || zWallCheck) {
						world.setBlockWithNotify(x, y, z, this.wallBlockB);

					} else if (yWallCheck) {
						world.setBlockWithNotify(x, y, z, this.wallBlockB);
					} else if (blockX - 2 == x && blockZ - 2 == z || blockX + 2 == x && blockZ - 2 == z || blockX - 2 == x && blockZ + 2 == z || blockX + 2 == x && blockZ + 2 == z) {
						if (blockY - wallHeight + 1 == y) {
							world.setBlock(x, y, z, Block.lampActive.id);
						} else if (blockY - wallHeight == y) {
							world.setBlock(x, y, z, Block.blockRedstone.id);
						}
					} else {
						world.setBlockWithNotify(x, y, z, 0);
					}


					if (generateTrapOnWall || !xWallCheck && !zWallCheck || x != blockZ && z != blockZ || y != blockY)
						continue;

					if (blockX - wallWidth == x) {
						this.placeMotionDispenser(world, random, x, y, z, Direction.WEST);
					}
					if (blockX + wallWidth == x) {
						this.placeMotionDispenser(world, random, x, y, z, Direction.EAST);
					}
					if (blockZ - wallWidth == z) {
						this.placeMotionDispenser(world, random, x, y, z, Direction.SOUTH);
					}
					if (blockZ + wallWidth == z) {
						this.placeMotionDispenser(world, random, x, y, z, Direction.NORTH);
					}

					generateTrapOnWall = true;
				}
			}
		}
		if (this.dungeonSize < DUNGEON_SIZE) {
			++this.dungeonSize;
			int corridorsToSpawn = random.nextInt(4);
			for (int i = 0; i <= corridorsToSpawn; ++i) {
				this.createCorridor(world, random, blockX, blockY, blockZ, random.nextInt(4), 0);
			}
		}
	}

	public void generateDrop(World world, Random random, int blockX, int blockY, int blockZ) {
		int dropHeight = random.nextInt(5) + 10;
		for (int x = blockX - 2; x <= blockX + 2; ++x) {
			for (int y = blockY - dropHeight; y <= blockY + 1; ++y) {
				for (int z = blockZ - 2; z <= blockZ + 2; ++z) {
					boolean yWallCheck;
					boolean xWallCheck = x == blockX - 2 || x == blockX + 2;
					boolean zWallCheck = z == blockZ - 2 || z == blockZ + 2;
					boolean bl = yWallCheck = y == blockY - dropHeight;
					if (!this.canReplace(world, x, y, z)) continue;
					if (xWallCheck || zWallCheck) {
						world.setBlockWithNotify(x, y, z, this.wallBlockB);
						continue;
					}

					if (y == blockY) {
						world.setBlockWithNotify(x, y, z, Block.trapdoorPlanksOak.id);
						continue;
					}

					if (yWallCheck) {
						world.setBlockWithNotify(x, y, z, this.wallBlockB);
						if (this.dungeonSize < 10) continue;
						world.setBlockWithNotify(x, y + 1, z, Block.spikes.id);
						continue;
					}
					if (x != blockX && z != blockZ && random.nextInt(20) == 0 && world.getBlockId(x, y + 1, z) != this.slabBlock) {
						world.setBlockWithNotify(x, y, z, this.slabBlock);
						continue;
					}
					world.setBlockWithNotify(x, y, z, 0);
				}
			}
		}
		if (this.dungeonSize < DUNGEON_SIZE) {
			++this.dungeonSize;
			int corridorsToSpawn = random.nextInt(4) + 1;
			for (int i = 1; i <= corridorsToSpawn; ++i) {
				this.createCorridor(world, random, blockX, blockY - (dropHeight - 2), blockZ, random.nextInt(4), 0);
			}
		}
	}

	public void generateDropTrap(World world, Random random, int blockX, int blockY, int blockZ) {
		int dropHeight = random.nextInt(5) + 3;
		for (int x = blockX - 2; x <= blockX + 2; ++x) {
			for (int y = blockY - dropHeight; y <= blockY + 1; ++y) {
				for (int z = blockZ - 2; z <= blockZ + 2; ++z) {
					boolean yWallCheck;
					boolean xWallCheck = x == blockX - 2 || x == blockX + 2;
					boolean zWallCheck = z == blockZ - 2 || z == blockZ + 2;
					boolean bl = yWallCheck = y == blockY - dropHeight;
					if (!this.canReplace(world, x, y, z)) continue;
					if (xWallCheck || zWallCheck) {
						world.setBlockWithNotify(x, y, z, this.wallBlockB);
						continue;
					}

					if (y == blockY) {
						world.setBlockWithNotify(x, y, z, Block.trapdoorPlanksOak.id);
						continue;
					}

					if (yWallCheck) {
						world.setBlockWithNotify(x, y, z, this.wallBlockB);
						world.setBlockWithNotify(x, y + 1, z, Block.spikes.id);
						continue;
					}
					if (x != blockX && z != blockZ && random.nextInt(20) == 0 && world.getBlockId(x, y + 1, z) != this.slabBlock) {
						world.setBlockWithNotify(x, y, z, this.slabBlock);
						continue;
					}
					world.setBlockWithNotify(x, y, z, 0);
				}
			}
		}
	}

	private boolean canReplace(World world, int x, int y, int z) {
		if (y <= 11) {
			return false;
		}
		if (world.getBlockId(x, y, z) == this.brickBlockA || world.getBlockId(x, y, z) == Block.planksOak.id || world.getBlockId(x, y, z) == Block.cobweb.id || world.getBlockId(x, y, z) == Block.bookshelfPlanksOak.id || world.getBlockId(x, y, z) == ModBlocks.trial_spawner.id || world.getBlockId(x, y, z) == this.brickBlockB) {
			return false;
		}
		if (world.getBlockId(x, y, z) == Block.motionsensorIdle.id || world.getBlockId(x, y, z) == Block.dispenserCobbleStone.id || world.getBlockId(x, y, z) == Block.motionsensorActive.id) {
			world.removeBlockTileEntity(x, y, z);
			world.setBlockWithNotify(x, y, z, 0);
			return true;
		}
		return world.getBlockMaterial(x, y, z) == Material.grass || world.getBlockMaterial(x, y, z) == Material.dirt || world.getBlockMaterial(x, y, z) == Material.stone || world.getBlockMaterial(x, y, z) == Material.sand || world.getBlockMaterial(x, y, z) == Material.moss;
	}

	private void generateCorridor(World world, Random random, int blockX, int blockY, int blockZ, int rot, int corridorIteration) {
		int height = 2;
		int width = 2;
		int length = 2;
		for (int x = blockX - width; x <= blockX + width; ++x) {
			boolean xWallCheck = x == blockX - width || x == blockX + width;
			for (int y = blockY - height; y <= blockY + (height - 1); ++y) {
				boolean yWallCheck = y == blockY - height;
				for (int z = blockZ - length; z <= blockZ + length; ++z) {
					boolean zWallCheck;
					boolean bl = zWallCheck = z == blockZ - length || z == blockZ + length;
					if (!this.canReplace(world, x, y, z) || (xWallCheck || zWallCheck || yWallCheck) && world.getBlockId(x, y + 1, z) == 0 && random.nextInt(3) > 0)
						continue;
					if (rot == 0) {
						if (xWallCheck) {
							world.setBlockWithNotify(x, y, z, this.wallBlockA);
						} else if (z == blockZ + length) {
							world.setBlockWithNotify(x, y, z, this.wallBlockA);
						} else if (yWallCheck) {
							if (random.nextInt(3) == 0) {
								world.setBlockWithNotify(x, y, z, this.wallBlockB);
							} else {
								world.setBlockWithNotify(x, y, z, this.wallBlockA);
							}
						} else {
							world.setBlockWithNotify(x, y, z, 0);
						}
					} else if (rot == 1) {
						if (x == blockX - width) {
							world.setBlockWithNotify(x, y, z, this.wallBlockA);
						} else if (zWallCheck) {
							world.setBlockWithNotify(x, y, z, this.wallBlockA);
						} else if (yWallCheck) {
							if (random.nextInt(3) == 0) {
								world.setBlockWithNotify(x, y, z, this.wallBlockB);
							} else {
								world.setBlockWithNotify(x, y, z, this.wallBlockA);
							}
						} else {
							world.setBlockWithNotify(x, y, z, 0);
						}
					} else if (rot == 2) {
						if (xWallCheck) {
							world.setBlockWithNotify(x, y, z, this.wallBlockA);
						} else if (z == blockZ - length) {
							world.setBlockWithNotify(x, y, z, this.wallBlockA);
						} else if (yWallCheck) {
							if (random.nextInt(3) == 0) {
								world.setBlockWithNotify(x, y, z, this.wallBlockB);
							} else {
								world.setBlockWithNotify(x, y, z, this.wallBlockA);
							}
						} else {
							world.setBlockWithNotify(x, y, z, 0);
						}
					} else if (x == blockX + width) {
						world.setBlockWithNotify(x, y, z, this.wallBlockA);
					} else if (zWallCheck) {
						world.setBlockWithNotify(x, y, z, this.wallBlockA);
					} else if (yWallCheck) {
						if (random.nextInt(3) == 0) {
							world.setBlockWithNotify(x, y, z, this.wallBlockB);
						} else {
							world.setBlockWithNotify(x, y, z, this.wallBlockA);
						}
					} else {
						world.setBlockWithNotify(x, y, z, 0);
					}
				}
			}
		}
		if (random.nextInt(2) == 0 && corridorIteration > 1) {
			if (random.nextInt(2) == 0) {
				this.generateDrop(world, random, blockX, blockY, blockZ);
			} else {
				this.generateBranch(world, random, blockX, blockY, blockZ);
			}
		} else if (random.nextInt(3) == 0 && corridorIteration > 1 && this.dungeonSize > 3 || this.dungeonSize >= DUNGEON_SIZE && this.dungeonCount < this.dungeonLimit) {
			this.createDungeon(world, random, blockX, blockY, blockZ, rot);
			++this.dungeonCount;
		} else {
			if (random.nextInt(10) == 0 && corridorIteration > 1 && this.dungeonSize > 5) {
				return;
			}
			this.createCorridor(world, random, blockX, blockY, blockZ, rot, corridorIteration + 1);
		}
	}

	private void createCorridor(World world, Random random, int blockX, int blockY, int blockZ, int rot, int size) {
		if (rot == 0) {
			this.generateCorridor(world, random, blockX, blockY, blockZ + 4, 0, size);
		}
		if (rot == 1) {
			this.generateCorridor(world, random, blockX - 4, blockY, blockZ, 1, size);
		}
		if (rot == 2) {
			this.generateCorridor(world, random, blockX, blockY, blockZ - 4, 2, size);
		}
		if (rot == 3) {
			this.generateCorridor(world, random, blockX + 4, blockY, blockZ, 3, size);
		}
	}

	private void generateDungeon(World world, Random random, int blockX, int blockY, int blockZ, boolean doSpawner) {
		int chestZ;
		int size = 6;
		for (int x = blockX - size; x <= blockX + size; ++x) {
			for (int y = blockY - 2; y <= blockY + 5; ++y) {
				for (int z = blockZ - size; z <= blockZ + size; ++z) {
					boolean yWallCheck;
					boolean xWallCheck = x == blockX - size || x == blockX + size;
					boolean zWallCheck = z == blockZ - size || z == blockZ + size;
					boolean bl = yWallCheck = y == blockY - 2;
					if (!this.canReplace(world, x, y, z)) continue;
					if (yWallCheck) {
						if (random.nextInt(5) == 0) {
							world.setBlockWithNotify(x, y, z, this.wallBlockB);
							continue;
						}
						world.setBlockWithNotify(x, y, z, this.wallBlockA);
						continue;
					}

					if ((xWallCheck || zWallCheck)) {
						world.setBlockWithNotify(x, y, z, this.wallBlockB);

						continue;
					}

					world.setBlockWithNotify(x, y, z, 0);
				}
			}
		}


		drawLine(world, this.wallBlockA, 0, Direction.UP, 10, blockX - 2, blockY - 2, blockZ - 2, true);
		world.setBlock(blockX - 2, blockY, blockZ - 2, Block.lampActive.id);
		world.setBlock(blockX - 2, blockY + 1, blockZ - 2, Block.blockRedstone.id);
		drawLine(world, this.wallBlockA, 0, Direction.UP, 10, blockX + 2, blockY - 2, blockZ - 2, true);
		world.setBlock(blockX + 2, blockY, blockZ - 2, Block.lampActive.id);
		world.setBlock(blockX + 2, blockY + 1, blockZ - 2, Block.blockRedstone.id);
		drawLine(world, this.wallBlockA, 0, Direction.UP, 10, blockX - 2, blockY - 2, blockZ + 2, true);
		world.setBlock(blockX - 2, blockY, blockZ + 2, Block.lampActive.id);
		world.setBlock(blockX - 2, blockY + 1, blockZ + 2, Block.blockRedstone.id);
		drawLine(world, this.wallBlockA, 0, Direction.UP, 10, blockX + 2, blockY - 2, blockZ + 2, true);
		world.setBlock(blockX + 2, blockY, blockZ + 2, Block.lampActive.id);
		world.setBlock(blockX + 2, blockY + 1, blockZ + 2, Block.blockRedstone.id);

		int chestX = blockX + random.nextInt(size - 1) - (size - 1);
		if (this.canReplace(world, chestX, blockY - 2, chestZ = blockZ + random.nextInt(size - 1) - (size - 1))) {
			world.setBlockWithNotify(chestX, blockY - 1, chestZ, Block.chestPlanksOak.id);
			TileEntityChest tileentitychest = (TileEntityChest) world.getBlockTileEntity(chestX, blockY - 1, chestZ);
			for (int k4 = 0; k4 < 16; ++k4) {
				ItemStack itemstack = this.pickCheckLootItem(random);
				if (itemstack == null) continue;
				tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), itemstack);
			}
		}
		if (doSpawner) {
			world.setBlockWithNotify(blockX, blockY - 1, blockZ, ModBlocks.trial_spawner.id);
			TileEntityTrialMobSpawner tileentitymobspawner = (TileEntityTrialMobSpawner) world.getBlockTileEntity(blockX, blockY - 1, blockZ);
			if (tileentitymobspawner != null) {
				String s = this.pickMobSpawner(random);
				tileentitymobspawner.setMobId(s);
				if (s.contains("Breeze")) {
					tileentitymobspawner.setEntityMaxCount(2);
				}

				if (random.nextInt(3) == 0) {

					tileentitymobspawner.setItemID(this.treasureItem.itemID);
				} else if (random.nextBoolean()) {
					tileentitymobspawner.setItemID(Item.foodAppleGold.id);
				} else {
					tileentitymobspawner.setItemID(Item.armorQuiver.id);
				}

			}
		}

		this.placeDispenser(world, random, blockX - size, blockY, blockZ, Direction.WEST);
		this.placeDispenser(world, random, blockX + size, blockY, blockZ, Direction.EAST);
		this.placeDispenser(world, random, blockX, blockY, blockZ - size, Direction.SOUTH);
		this.placeDispenser(world, random, blockX, blockY, blockZ + size, Direction.NORTH);


		generateDropTrap(world, random, blockX + 4, blockY - 2, blockZ + 4);
		generateDropTrap(world, random, blockX - 4, blockY - 2, blockZ + 4);
		generateDropTrap(world, random, blockX + 4, blockY - 2, blockZ - 4);
		generateDropTrap(world, random, blockX - 4, blockY - 2, blockZ - 4);

	}

	private void placeDispenser(World world, Random random, int x, int y, int z, Direction direction) {
		if (world.getBlockId(x, y, z) != 0) {
			int meta = 1;

			if (direction == Direction.NORTH) {
				meta = 2;
			}
			if (direction == Direction.SOUTH) {
				meta = 3;
			}
			if (direction == Direction.WEST) {
				meta = 4;
			}
			if (direction == Direction.EAST) {
				meta = 5;
			}

			world.setBlockAndMetadataWithNotify(x, y, z, Block.motionsensorIdle.id, meta);
			world.setBlockAndMetadataWithNotify(x, y - 1, z, Block.dispenserCobbleStone.id, meta);
			world.setBlockAndMetadataWithNotify(x, y, z, Block.buttonStone.id, 6);
			world.setBlockWithNotify(x, y - 1, z, Block.dispenserCobbleStone.id);
			TileEntityDispenser tileEntityDispenser = (TileEntityDispenser) world.getBlockTileEntity(x, y - 1, z);

			for (int k4 = 0; k4 < 4; ++k4) {
				ItemStack itemstack = this.pickDispenserLootItem(random);
				if (itemstack == null) continue;
				tileEntityDispenser.setInventorySlotContents(random.nextInt(tileEntityDispenser.getSizeInventory()), itemstack);
			}
		}
	}

	private void placeMotionDispenser(World world, Random random, int x, int y, int z, Direction direction) {

		int meta = 1;

		if (direction == Direction.NORTH) {
			meta = 2;
		}
		if (direction == Direction.SOUTH) {
			meta = 3;
		}
		if (direction == Direction.WEST) {
			meta = 4;
		}
		if (direction == Direction.EAST) {
			meta = 5;
		}

		world.setBlockAndMetadataWithNotify(x, y, z, Block.motionsensorIdle.id, meta);
		world.setBlockAndMetadataWithNotify(x, y - 1, z, Block.dispenserCobbleStone.id, meta);
		TileEntityDispenser tileEntityDispenser = (TileEntityDispenser) world.getBlockTileEntity(x, y - 1, z);

		for (int k4 = 0; k4 < 4; ++k4) {
			ItemStack itemstack = this.pickDispenserLootItem(random);
			if (itemstack == null) continue;
			tileEntityDispenser.setInventorySlotContents(random.nextInt(tileEntityDispenser.getSizeInventory()), itemstack);
		}
	}

	private void createDungeon(World world, Random random, int blockX, int blockY, int blockZ, int rot) {
		int dx = 0;
		int dz = 0;
		if (rot == 0) {
			dz = 1;
		}
		if (rot == 1) {
			dx = -1;
		}
		if (rot == 2) {
			dz = -1;
		}
		if (rot == 3) {
			dx = 1;
		}
		if (this.canReplace(world, blockX + dx * 5, blockY, blockZ + dz * 5)) {
			if (this.dungeonSize == DUNGEON_SIZE && !this.libraryGenerated) {
				this.libraryGenerated = true;
				this.generateDungeon(world, random, blockX + dx * 4, blockY, blockZ + dz * 4, true);
			} else {
				this.generateDungeon(world, random, blockX + dx * 4, blockY, blockZ + dz * 4, true);
			}
		}
	}

	private ItemStack pickDispenserLootItem(Random random) {
		return this.dispenserLoot.getRandom(random).getItemStack(random);
	}

	private ItemStack pickCheckLootItem(Random random) {
		if (!this.treasureGenerated && this.dungeonSize > 7) {
			this.treasureGenerated = true;
			return this.treasureItem.copy();
		}
		return this.chestLoot.getRandom(random).getItemStack(random);
	}

	private String pickMobSpawner(Random random) {
		return this.spawnerMonsters.getRandom(random);
	}
}

