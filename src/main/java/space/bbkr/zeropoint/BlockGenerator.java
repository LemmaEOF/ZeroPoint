package space.bbkr.zeropoint;

        import net.minecraft.block.Block;
        import net.minecraft.block.ITileEntityProvider;
        import net.minecraft.block.state.IBlockState;
        import net.minecraft.client.util.ITooltipFlag;
        import net.minecraft.entity.player.EntityPlayer;
        import net.minecraft.item.ItemStack;
        import net.minecraft.tileentity.TileEntity;
        import net.minecraft.tileentity.TileEntityFurnace;
        import net.minecraft.util.EnumFacing;
        import net.minecraft.util.EnumHand;
        import net.minecraft.util.math.BlockPos;
        import net.minecraft.util.text.ITextComponent;
        import net.minecraft.util.text.TextComponentTranslation;
        import net.minecraft.world.IBlockReader;
        import net.minecraft.world.World;

        import javax.annotation.Nullable;
        import java.util.List;

public class BlockGenerator extends Block implements ITileEntityProvider {

    public BlockGenerator(Builder builder) {
        super(builder);
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileEntityGenerator();
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z) {
        if (world == null || world.isRemote() || player == null || hand == EnumHand.OFF_HAND) return true;
        TileEntity tile = world.getTileEntity(pos);
        if ((tile instanceof TileEntityGenerator)) {
            ItemStack heldStack = player.getHeldItem(hand);
            TileEntityGenerator te = (TileEntityGenerator) tile;
            if (!player.isSneaking()) {
                if (TileEntityFurnace.isItemFuel(heldStack)) {
                    te.transferPlayerItem(player, hand);
                    return true;
                } else if (heldStack.isEmpty()) {
                    te.transferInventoryItem(player);
                    return true;
                }
            } else {
                player.sendStatusMessage(new TextComponentTranslation("msg.zeropoint.energy"), true);
                return true;
            }
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TextComponentTranslation("tooltip.zeropoint.generator"));
    }
}
