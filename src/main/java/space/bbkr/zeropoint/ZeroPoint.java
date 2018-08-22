package space.bbkr.zeropoint;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemSimpleFoiled;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.BlockAdder;
import org.dimdev.rift.listener.ItemAdder;
import org.dimdev.rift.listener.TileEntityTypeAdder;
import space.bbkr.zeropoint.api.DisposableBattery;


public class ZeroPoint implements BlockAdder, ItemAdder, TileEntityTypeAdder {

    public static final Item ZERO_CRYSTAL = new ItemSimpleFoiled(new Item.Builder().group(ItemGroup.REDSTONE).rarity(EnumRarity.RARE));

    public static final BlockGenerator GENERATOR = new BlockGenerator(Block.Builder.create(Material.ROCK, MapColor.STONE).hardnessAndResistance(2f, 3f).sound(SoundType.METAL));


    public static TileEntityType<TileEntityGenerator> GENERATOR_TE;
    public static TileEntityType<TileEntityCrystallizer> CRYSTALLIZER_TE;

    @Override
    public void registerBlocks() {
        Block.register(new ResourceLocation("zeropoint:generator"), GENERATOR);
    }

    @Override
    public void registerItems() {
        Item.registerItemBlock(GENERATOR, ItemGroup.REDSTONE);
        Item.registerItem(new ResourceLocation("zeropoint:zero_crystal"), ZERO_CRYSTAL);
        DisposableBattery.getBatteryTimes().put(ZERO_CRYSTAL, 1600);
    }

    @Override
    public void registerTileEntityTypes() {
        GENERATOR_TE = TileEntityType.registerTileEntityType("zeropoint:generator", TileEntityType.Builder.create(TileEntityGenerator::new));
    }
}
