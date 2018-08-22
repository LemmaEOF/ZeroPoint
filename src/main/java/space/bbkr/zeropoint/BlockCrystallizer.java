package space.bbkr.zeropoint;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockCrystallizer extends Block implements ITileEntityProvider {

    public BlockCrystallizer(Builder builder) {
        super(builder);
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileEntityCrystallizer();
    }
}
