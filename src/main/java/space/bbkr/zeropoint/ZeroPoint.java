package space.bbkr.zeropoint;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.BlockAdder;
import org.dimdev.rift.listener.ItemAdder;
import org.dimdev.rift.listener.TileEntityTypeAdder;


public class ZeroPoint implements BlockAdder, ItemAdder, TileEntityTypeAdder {

    public static final BlockGenerator GENERATOR = new BlockGenerator(Block.Builder.create(Material.ROCK, MapColor.STONE).hardnessAndResistance(2f, 3f).soundType(SoundType.METAL));

    public static TileEntityType<TileEntityGenerator> GENERATOR_TE;

    @Override
    public void registerBlocks() {
        Block.registerBlock(new ResourceLocation("zeropoint:generator"), GENERATOR);
    }

    @Override
    public void registerItems() {
        Item.registerItemBlock(GENERATOR, ItemGroup.REDSTONE);
    }

    @Override
    public void registerTileEntityTypes() {
        GENERATOR_TE = TileEntityType.registerTileEntityType("zeropoint:tank", TileEntityType.Builder.create(TileEntityGenerator::new));
    }
}
