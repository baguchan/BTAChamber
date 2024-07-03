package baguchan.chamber_bta.world.gen;

import baguchan.chamber_bta.util.Pair;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;

import java.util.Random;

public abstract class AbstractTrialChamberGen extends WorldFeature {

	public static double distanceToSqr(int x, int y, int z, int x1, int y1, int z1) {
		double d3 = x - x1;
		double d4 = y - y1;
		double d5 = z - z1;
		return d3 * d3 + d4 * d4 + d5 * d5;
	}

	public void drawSphere(World world, Random random, int x, int y, int z, int radius, BlockPallet pallet, boolean withNotify) {
		for (int blockX = x - radius; blockX <= x + radius; blockX++) {
			for (int blockY = y - radius; blockY <= y + radius; blockY++) {
				for (int blockZ = z - radius; blockZ <= z + radius; blockZ++) {
					if (distanceToSqr(x, y, z, blockX, blockY, blockZ) < Math.pow(radius, 2))
						setBlock(world, blockX, blockY, blockZ, pallet.getRandom(random), withNotify);
				}
			}
		}
	}

	public void drawSpheroid(World world, Random random, int x, int y, int z, int width, int height, int depth, BlockPallet pallet, boolean withNotify) {
		for (int blockX = x - width; blockX <= x + width; blockX++) {
			for (int blockY = y - height; blockY <= y + height; blockY++) {
				for (int blockZ = z - depth; blockZ <= z + depth; blockZ++) {
					double distanceSqr = Math.pow((blockX - x) / (double) width, 2) + Math.pow((blockY - y) / (double) height, 2) + Math.pow((blockZ - z) / (double) depth, 2);
					if (distanceSqr <= 1.0) {
						setBlock(world, blockX, blockY, blockZ, pallet.getRandom(random), withNotify);
					}
				}
			}
		}
	}

	public int[] drawLine(World world, int id, int meta, Direction direction, int length, int startX, int startY, int startZ, boolean withNotify) {
		for (int i = 0; i < length - 1; i++) {
			setBlock(world, startX, startY, startZ, id, meta, withNotify);
			startX += direction.getOffsetX();
			startY += direction.getOffsetY();
			startZ += direction.getOffsetZ();
		}
		setBlock(world, startX, startY, startZ, id, meta, withNotify);
		return new int[]{startX, startY, startZ};
	}

	public int[] drawLine(World world, Random random, BlockPallet pallet, Direction direction, int length, int startX, int startY, int startZ, boolean withNotify) {
		for (int i = 0; i < length - 1; i++) {
			setBlock(world, startX, startY, startZ, pallet.getRandom(random), withNotify);
			startX += direction.getOffsetX();
			startY += direction.getOffsetY();
			startZ += direction.getOffsetZ();
		}
		setBlock(world, startX, startY, startZ, pallet.getRandom(random), withNotify);
		return new int[]{startX, startY, startZ};
	}

	public int[] drawPlane(World world, int id, int meta, Direction direction1, int length1, Direction direction2, int length2, int startX, int startY, int startZ, boolean withNotify) {
		int blockX = startX;
		int blockY = startY;
		int blockZ = startZ;
		for (int i = 0; i < length2; i++) {
			blockX = startX + direction2.getOffsetX() * i;
			blockY = startY + direction2.getOffsetY() * i;
			blockZ = startZ + direction2.getOffsetZ() * i;
			for (int j = 0; j < length1; j++) {
				setBlock(world, blockX, blockY, blockZ, id, meta, withNotify);
				blockX += direction1.getOffsetX();
				blockY += direction1.getOffsetY();
				blockZ += direction1.getOffsetZ();
			}
		}
		return new int[]{blockX, blockY, blockZ};
	}

	public int[] drawPlane(World world, Random random, BlockPallet pallet, Direction direction1, int length1, Direction direction2, int length2, int startX, int startY, int startZ, boolean withNotify) {
		int blockX = startX;
		int blockY = startY;
		int blockZ = startZ;
		for (int i = 0; i < length2; i++) {
			blockX = startX + direction2.getOffsetX() * i;
			blockY = startY + direction2.getOffsetY() * i;
			blockZ = startZ + direction2.getOffsetZ() * i;
			for (int j = 0; j < length1; j++) {
				setBlock(world, blockX, blockY, blockZ, pallet.getRandom(random), withNotify);
				blockX += direction1.getOffsetX();
				blockY += direction1.getOffsetY();
				blockZ += direction1.getOffsetZ();
			}
		}
		return new int[]{blockX, blockY, blockZ};
	}

	public int[] drawVolume(World world, int id, int meta, Direction direction1, int length1, Direction direction2, int length2, Direction direction3, int length3, int startX, int startY, int startZ, boolean withNotify) {
		int blockX = startX;
		int blockY = startY;
		int blockZ = startZ;
		for (int i = 0; i < length3; i++) {
			int x3 = startX + direction3.getOffsetX() * i;
			int y3 = startY + direction3.getOffsetY() * i;
			int z3 = startZ + direction3.getOffsetZ() * i;
			for (int j = 0; j < length2; j++) {
				blockX = x3 + direction2.getOffsetX() * j;
				blockY = y3 + direction2.getOffsetY() * j;
				blockZ = z3 + direction2.getOffsetZ() * j;
				for (int k = 0; k < length1; k++) {
					setBlock(world, blockX, blockY, blockZ, id, meta, withNotify);
					blockX += direction1.getOffsetX();
					blockY += direction1.getOffsetY();
					blockZ += direction1.getOffsetZ();
				}
			}
		}
		return new int[]{blockX, blockY, blockZ};
	}

	public int[] drawVolume(World world, Random random, BlockPallet pallet, Direction direction1, int length1, Direction direction2, int length2, Direction direction3, int length3, int startX, int startY, int startZ, boolean withNotify) {
		int blockX = startX;
		int blockY = startY;
		int blockZ = startZ;
		for (int i = 0; i < length3; i++) {
			int x3 = startX + direction3.getOffsetX() * i;
			int y3 = startY + direction3.getOffsetY() * i;
			int z3 = startZ + direction3.getOffsetZ() * i;
			for (int j = 0; j < length2; j++) {
				blockX = x3 + direction2.getOffsetX() * j;
				blockY = y3 + direction2.getOffsetY() * j;
				blockZ = z3 + direction2.getOffsetZ() * j;
				for (int k = 0; k < length1; k++) {
					setBlock(world, blockX, blockY, blockZ, pallet.getRandom(random), withNotify);
					blockX += direction1.getOffsetX();
					blockY += direction1.getOffsetY();
					blockZ += direction1.getOffsetZ();
				}
			}
		}
		return new int[]{blockX, blockY, blockZ};
	}

	public int[] drawShell(World world, int id, int meta, Direction direction1, int length1, Direction direction2, int length2, Direction direction3, int length3, int startX, int startY, int startZ, boolean withNotify) {
		drawPlane(world, id, meta, direction1, length1, direction2, length2, startX, startY, startZ, withNotify);
		drawPlane(world, id, meta, direction1, length1, direction2, length2, startX + direction3.getOffsetX() * (length3 - 1), startY + direction3.getOffsetY() * (length3 - 1), startZ + direction3.getOffsetZ() * (length3 - 1), withNotify);

		drawPlane(world, id, meta, direction1, length1, direction3, length3, startX, startY, startZ, withNotify);
		drawPlane(world, id, meta, direction1, length1, direction3, length3, startX + direction2.getOffsetX() * (length2 - 1), startY + direction2.getOffsetY() * (length2 - 1), startZ + direction2.getOffsetZ() * (length2 - 1), withNotify);

		drawPlane(world, id, meta, direction2, length2, direction3, length3, startX, startY, startZ, withNotify);
		return drawPlane(world, id, meta, direction2, length2, direction3, length3, startX + direction1.getOffsetX() * (length1 - 1), startY + direction1.getOffsetY() * (length1 - 1), startZ + direction1.getOffsetZ() * (length1 - 1), withNotify);
	}

	public int[] drawShell(World world, Random random, BlockPallet pallet, Direction direction1, int length1, Direction direction2, int length2, Direction direction3, int length3, int startX, int startY, int startZ, boolean withNotify) {
		drawPlane(world, random, pallet, direction1, length1, direction2, length2, startX, startY, startZ, withNotify);
		drawPlane(world, random, pallet, direction1, length1, direction2, length2, startX + direction3.getOffsetX() * (length3 - 1), startY + direction3.getOffsetY() * (length3 - 1), startZ + direction3.getOffsetZ() * (length3 - 1), withNotify);

		drawPlane(world, random, pallet, direction1, length1, direction3, length3, startX, startY, startZ, withNotify);
		drawPlane(world, random, pallet, direction1, length1, direction3, length3, startX + direction2.getOffsetX() * (length2 - 1), startY + direction2.getOffsetY() * (length2 - 1), startZ + direction2.getOffsetZ() * (length2 - 1), withNotify);

		drawPlane(world, random, pallet, direction2, length2, direction3, length3, startX, startY, startZ, withNotify);
		return drawPlane(world, random, pallet, direction2, length2, direction3, length3, startX + direction1.getOffsetX() * (length1 - 1), startY + direction1.getOffsetY() * (length1 - 1), startZ + direction1.getOffsetZ() * (length1 - 1), withNotify);
	}

	protected void setBlock(World world, int x, int y, int z, Pair<Integer, Integer> block, boolean withNotify) {
		setBlock(world, x, y, z, block.first, block.second, withNotify);
	}

	protected void setBlock(World world, int x, int y, int z, int id, int meta, boolean withNotify) {
		if (withNotify) {
			world.setBlockAndMetadataWithNotify(x, y, z, id, meta);
		} else {
			world.setBlockAndMetadata(x, y, z, id, meta);
		}
	}


	public void addSolidBox(World world, Random random, BlockPallet pallet, int i, int j, int k, int di, int dj, int dk, Boolean withNotify) {
		for (int x = i; x < i + di; ++x) {
			for (int y = j; y < j + dj; ++y) {
				for (int z = k; z < k + dk; ++z) {
					setBlock(world, x, y, z, pallet.getRandom(random), withNotify);
				}
			}
		}
	}

	public void addSolidBox(World world, int id, int meta, int i, int j, int k, int di, int dj, int dk, Boolean withNotify) {
		for (int x = i; x < i + di; ++x) {
			for (int y = j; y < j + dj; ++y) {
				for (int z = k; z < k + dk; ++z) {
					if (withNotify) {
						world.setBlockAndMetadataWithNotify(x, y, z, id, meta);
						continue;
					}
					world.setBlockAndMetadata(x, y, z, Block.blockDiamond.id, meta);
				}
			}
		}
	}


	public void addSquareTube(World world, Random random, BlockPallet pallet, int i, int j, int k, int di, int dj, int dk, int dir) {
		this.addSolidBox(world, 0, 0, i, j, k, di, dj, dk, true);

		if (dir == 0 || dir == 2) {
			this.drawPlane(world, random, pallet, Direction.SOUTH, di, Direction.EAST, dk, i, j, k, true);
			this.drawPlane(world, random, pallet, Direction.SOUTH, di, Direction.EAST, dk, i, j + dj - 1, k, true);
		}

		if (dir == 1 || dir == 2) {
			this.drawPlane(world, random, pallet, Direction.UP, dj, Direction.SOUTH, dk, i, j, k, true);
			this.drawPlane(world, random, pallet, Direction.UP, dj, Direction.SOUTH, dk, i + di - 1, j, k, true);
		}

		if (dir == 0 || dir == 1) {
			this.drawPlane(world, random, pallet, Direction.UP, di, Direction.EAST, dj, i, j, k, true);
			this.drawPlane(world, random, pallet, Direction.UP, di, Direction.EAST, dj, i, j, k + dk - 1, true);
		}
	}

	private boolean isBoxSolid(World world, int startX, int startY, int startZ, int length1, int length2, int length3) {
		return this.isBoxSolid(world, startX, startY, startZ, Direction.EAST, length1, Direction.UP, length2, Direction.SOUTH, length3);
	}

	public boolean isBoxEmpty(World world, int i, int j, int k, int di, int dj, int dk) {
		return !isBoxSolid(world, i, j, k, Direction.EAST, di, Direction.UP, dj, Direction.SOUTH, dk);
	}

	public boolean isBoxSolid(World world, int startX, int startY, int startZ, Direction direction1, int length1, Direction direction2, int length2, Direction direction3, int length3) {
		int volume = 0;
		int blockX = startX;
		int blockY = startY;
		int blockZ = startZ;
		for (int i = 0; i < length3; i++) {
			int x3 = startX + direction3.getOffsetX() * i;
			int y3 = startY + direction3.getOffsetY() * i;
			int z3 = startZ + direction3.getOffsetZ() * i;
			for (int j = 0; j < length2; j++) {
				blockX = x3 + direction2.getOffsetX() * j;
				blockY = y3 + direction2.getOffsetY() * j;
				blockZ = z3 + direction2.getOffsetZ() * j;
				for (int k = 0; k < length1; k++) {
					if (world.getBlockId(blockX, blockY, blockZ) == 0) volume++;
					blockX += direction1.getOffsetX();
					blockY += direction1.getOffsetY();
					blockZ += direction1.getOffsetZ();
				}
			}
		}

		// I'm literally frito-lay fr fr fr
		return !(volume > ((length1 * length2 * length3) * 0.35F));
	}
}
