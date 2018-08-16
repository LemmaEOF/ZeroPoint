package space.bbkr.zeropoint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import space.bbkr.zeropoint.api.ActionType;
import space.bbkr.zeropoint.api.EnergyDirection;
import space.bbkr.zeropoint.api.EnergyStorage;
import space.bbkr.zeropoint.api.IEnergyHandler;

public class TileEntityGenerator extends TileEntity implements IEnergyHandler, ITickable {
    private EnergyStorage energy;
    private ItemStack stack;
    private int fuelTime;

    public TileEntityGenerator(TileEntityType<?> type) {
        super(type);
        this.stack = ItemStack.EMPTY;
        this.energy = new EnergyStorage(64000, 30, 300);
        this.fuelTime = 0;
    }

    public TileEntityGenerator() {
        this(ZeroPoint.GENERATOR_TE);
    }

    public void update() {
        if (consumeFuel()) energy.insert(30, ActionType.EXECUTE);
        if (energy.getEnergyStored() > 0) transferEnergy();
        markDirty();
    }

    public boolean consumeFuel() {
        if ((energy.insert(30, ActionType.SIMULATE) == 0)) return false;
        if (fuelTime <= 0) {
            if (stack.isEmpty() || !TileEntityFurnace.isItemFuel(stack)) return false;
            fuelTime = TileEntityFurnace.getBurnTimes().get(stack.getItem());
            stack.shrink(1);
        }
        fuelTime--;
        return true;
    }

    public void transferEnergy() {
        for (EnumFacing face : EnumFacing.values()) {
            TileEntity tile = world.getTileEntity(pos.offset(face));
            if (tile instanceof IEnergyHandler) {
                IEnergyHandler energyHandler = (IEnergyHandler)tile;
                if (energyHandler.canTransfer(face.getOpposite(), EnergyDirection.INWARDS)) {
                    int sendTest = energy.extract(300, ActionType.SIMULATE);
                    int sendReal = energyHandler.getEnergyStorage().insert(sendTest, ActionType.EXECUTE);
                    energy.extract(sendReal, ActionType.EXECUTE);
                }
            }
        }
    }

    public void transferPlayerItem(EntityPlayer player, EnumHand hand) {
        if (!world.isRemote()) {
            stack = player.getHeldItem(hand);
            player.inventory.deleteStack(player.getHeldItem(hand));
            markDirty();
        }
    }

    public void transferInventoryItem(EntityPlayer player) {
        if (!stack.isEmpty() && !world.isRemote()) player.inventory.addItemStackToInventory(stack);
        markDirty();
    }

    public EnergyStorage getEnergyStorage() {
        return this.energy;
    }

    @Override
    public boolean canTransfer(EnumFacing facing, EnergyDirection direction) {
        return direction == EnergyDirection.OUTWARDS;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setTag("Inv", stack.writeToNBT(new NBTTagCompound()));
        tag.setTag("Energy", energy.writeToNBT(new NBTTagCompound()));
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        stack = ItemStack.func_199557_a(compound.getCompoundTag("Inv"));
        energy.readFromNBT(compound.getCompoundTag("Energy"));
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

}
