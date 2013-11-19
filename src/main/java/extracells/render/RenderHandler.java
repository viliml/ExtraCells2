package extracells.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import extracells.blocks.BlockTerminalFluid;
import extracells.tile.TileEntityTerminalFluid;

public class RenderHandler implements ISimpleBlockRenderingHandler
{
	int renderID = 0;
	int[] blue =
	{ 0x2D29A0, 0x514AFF, 0xDDE6FF };
	int[] black =
	{ 0x2B2B2B, 0x565656, 0x848484 };
	int[] white =
	{ 0xBEBEBE, 0xDBDBDB, 0xFAFAFA };
	int[] brown =
	{ 0x724E35, 0xB7967F, 0xE0D2C8 };
	int[] red =
	{ 0xA50029, 0xFF003C, 0xFFE6ED };
	int[] yellow =
	{ 0xFFF7AA, 0xF8FF4A, 0xFFFFE8 };
	int[] green =
	{ 0x45A021, 0x60E32E, 0xE3F2E3 };
	int[] fluix =
	{ 0x1B2344, 0x895CA8, 0xDABDEF };

	public RenderHandler(int id)
	{
		renderID = id;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		Tessellator tessellator = Tessellator.instance;
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		tessellator.addTranslation(0.0F, 0.0F, 0.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
		tessellator.addTranslation(0.0F, 0.0F, 0.0F);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addTranslation(0.0F, 0.0F, 0.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
		tessellator.addTranslation(0.0F, 0.0F, 0.0F);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator.addTranslation(0.0F, 0.0F, 0.0F);

		tessellator.setBrightness(15 << 20 | 15 << 6);
		tessellator.setColorRGBA_I(0xFFFFFF, 0xFF);
		int[] color =
		{ 0x1B2344, 0x895CA8, 0xDABDEF };
		BlockTerminalFluid terminal = (BlockTerminalFluid) block;
		renderer.renderFaceXNeg(block, 0, 0, 0, terminal.baseLayer);
		for (int i = 0; i < 3; i++)
		{
			tessellator.setColorRGBA_I(color[i], 0xFF);
			renderer.renderFaceXNeg(block, 0, 0, 0, terminal.colorLayers[i]);
		}

		tessellator.addTranslation(0.0F, 0.0F, 0.0F);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator.addTranslation(0.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
		tessellator.addTranslation(0.0F, 0.0F, 0.0F);
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (block instanceof BlockTerminalFluid)
		{
			renderer.renderStandardBlock(block, x, y, z);

			Tessellator ts = Tessellator.instance;
			ForgeDirection face = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
			BlockTerminalFluid terminal = (BlockTerminalFluid) block;
			int[] color =
			{ 0, 0, 0 };

			switch (((TileEntityTerminalFluid) world.getBlockTileEntity(x, y, z)).getColor())
			{
			case -1:
				color = fluix.clone();
				break;
			case 0:
				color = blue.clone();
				break;
			case 1:
				color = black.clone();
				break;
			case 2:
				color = white.clone();
				break;
			case 3:
				color = brown.clone();
				break;
			case 4:
				color = red.clone();
				break;
			case 5:
				color = yellow.clone();
				break;
			case 6:
				color = green.clone();
				break;
			}

			boolean active = ((TileEntityTerminalFluid) world.getBlockTileEntity(x, y, z)).isMachineActive();

			ts.setBrightness(15 << 2 | 15 << 0);

			if (active)
				ts.setBrightness(15 << 20 | 15 << 4);

			ts.setColorOpaque_I(0xFFFFFF);
			drawFace(face, block, x, y, z, terminal.baseLayer, renderer);
			for (int i = 0; i < 3; i++)
			{
				ts.setColorOpaque_I(color[i]);
				drawFace(face, block, x, y, z, terminal.colorLayers[i], renderer);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return renderID;
	}

	private void drawFace(ForgeDirection side, Block block, double x, double y, double z, Icon icon, RenderBlocks renderer)
	{
		switch (side)
		{
		case UP:
			renderer.renderFaceYPos(block, x, y, z, icon);
			break;
		case DOWN:
			renderer.renderFaceYNeg(block, x, y, z, icon);
			break;
		case NORTH:
			renderer.renderFaceZNeg(block, x, y, z, icon);
			break;
		case EAST:
			renderer.renderFaceXPos(block, x, y, z, icon);
			break;
		case SOUTH:
			renderer.renderFaceZPos(block, x, y, z, icon);
			break;
		case WEST:
			renderer.renderFaceXNeg(block, x, y, z, icon);
			break;
		case UNKNOWN:
			break;
		}
	}

}
