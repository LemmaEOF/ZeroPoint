package space.bbkr.zeropoint;

import com.gmail.zendarva.api.capabilities.ActionType;
import com.gmail.zendarva.api.capabilities.ICapability;
import com.gmail.zendarva.api.capabilities.ICapabilityContext;
import com.gmail.zendarva.api.capabilities.ICapabilityProvider;
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
import space.bbkr.zeropoint.api.DisposableBattery;
import space.bbkr.zeropoint.api.TransferDirection;
import space.bbkr.zeropoint.api.EnergyStorage;
import space.bbkr.zeropoint.api.IEnergyHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TileEntityGenerator extends TileEntity implements ICapabilityProvider, ITickable {
    private EnergyStorage energy;
    private ItemStack stack;
    private int fuelTime;
    private List<ICapability> proxiedCapabilities = new LinkedList<>();

    public TileEntityGenerator(TileEntityType<?> type) {
        super(type);
        this.stack = ItemStack.EMPTY;
        this.energy = new EnergyStorage(16000, 10, 100);
        this.fuelTime = 0;
    }

    public TileEntityGenerator() {
        this(ZeroPoint.GENERATOR_TE);
    }

    public void update() {
        if (consumeFuel()) energy.insert(10, ActionType.EXECUTE);
        if (energy.getEnergyStored() > 0) transferEnergy();
        markDirty();
    }

    public boolean consumeFuel() {
        if ((energy.insert(10, ActionType.SIMULATE) == 0)) return false;
        if (fuelTime <= 0) {
            if (stack.isEmpty() || !TileEntityFurnace.isItemFuel(stack) || !DisposableBattery.isDisposableBattery(stack)) return false;
            if (TileEntityFurnace.isItemFuel(stack)) fuelTime = TileEntityFurnace.getBurnTimes().get(stack.getItem());
            else if (DisposableBattery.isDisposableBattery(stack)) fuelTime = DisposableBattery.getBatteryTimes().get(stack.getItem());
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
                if (energyHandler.canTransfer(face.getOpposite(), TransferDirection.OPPOSITE)) {
                    int sendTest = energy.extract(100, ActionType.SIMULATE);
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

//    @Override
//    public boolean canTransfer(EnumFacing facing, TransferDirection direction) {
//        return direction == TransferDirection.MATCH;
//    }

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
        stack = ItemStack.loadFromNBT(compound.getCompoundTag("Inv"));
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

    @Nonnull
    @Override
    public Optional<? extends ICapability> queryCapability(ICapabilityContext context, Class<? extends ICapability> capability) {
        Optional<ICapability> proxiedCap = proxiedCapabilities.stream().filter(f->f.matches(context)).findFirst();
        if (proxiedCap.isPresent())
            return proxiedCap;
        if (capability == IEnergyHandler.class){
            return Optional.of(energy);
        }
        return Optional.empty();
    }

    @Nullable
    @Override
    public ICapability getCapability(ICapabilityContext iCapabilityContext, Class<? extends ICapability> aClass) {
        return null;
    }

    @Nonnull
    @Override
    public List<? extends ICapability> getCapabilities(ICapabilityContext iCapabilityContext, Class<? extends ICapability> aClass) {
        return null;
    }

    @Override
    public void addProxyCapability(ICapability iCapability) {

    }

    @Override
    public void removeProxyCapability(ICapability iCapability) {

    }

    @Override
    public void invalidateCapabilities() {

    }
}
