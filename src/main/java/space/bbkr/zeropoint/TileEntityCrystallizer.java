package space.bbkr.zeropoint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import space.bbkr.zeropoint.api.EnergyStorage;
import space.bbkr.zeropoint.api.IEnergyHandler;
import space.bbkr.zeropoint.api.TransferDirection;

import javax.annotation.Nullable;

public class TileEntityCrystallizer extends TileEntity implements ITickable, IEnergyHandler, IInventory {
    private EnergyStorage energy;
    public ItemStack output;
    public static final ItemStack FULL_OUTPUT = new ItemStack(ZeroPoint.ZERO_CRYSTAL, 62);
    private int processTicks;
    private final int MAX_PROCESS_TICKS = 1000;

    public TileEntityCrystallizer(TileEntityType<?> type) {
        super(type);
        this.energy = new EnergyStorage(20000, 100, 20);
        this.processTicks = 0;
    }

    public TileEntityCrystallizer() {
        this(ZeroPoint.CRYSTALLIZER_TE);
    }

    public void update() {
        if (canProcess()) {
            if (processTicks <= 0) {
                if (output.isEmpty()) output = new ItemStack(ZeroPoint.ZERO_CRYSTAL, 1);
                else output.grow(1);
                processTicks = MAX_PROCESS_TICKS;
            }
            processTicks--;
        }
    }

    public boolean canProcess() {
        if (output.getItem().equals(ZeroPoint.ZERO_CRYSTAL) && !output.equals(FULL_OUTPUT)) {
            return energy.extract(3, ActionType.SIMULATE) == 0;
        }
        return false;
    }

    public boolean canTransfer(EnumFacing facing, TransferDirection direction) {
        return direction == TransferDirection.OPPOSITE;
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return this.energy;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return output.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return output;
    }

    @Override
    public ItemStack decrStackSize(int i, int i1) {
        output.shrink(i1);
        return output;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        ItemStack get = output;
        output.setCount(0);
        return get;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        output = itemStack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return entityPlayer.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return false;
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int i1) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public ITextComponent getName() {
        return new TextComponentTranslation("container.zeropoint.crystallizer");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }
}
