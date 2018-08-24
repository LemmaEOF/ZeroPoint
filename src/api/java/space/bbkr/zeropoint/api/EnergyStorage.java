package space.bbkr.zeropoint.api;

import com.gmail.zendarva.api.capabilities.ActionType;

import net.minecraft.nbt.NBTTagCompound;

public class EnergyStorage implements IEnergyStorage {
    protected int energy;
    protected int capacity;
    protected int maxEnergyInsert;
    protected int maxEnergyExtract;

    public EnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public EnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public EnergyStorage(int capacity, int maxEnergyInsert, int maxEnergyExtract) {
        this(capacity, maxEnergyInsert, maxEnergyExtract, 0);
    }

    public EnergyStorage(int capacity, int maxEnergyInsert, int maxEnergyExtract, int energy) {
        this.capacity = capacity;
        this.maxEnergyInsert = maxEnergyInsert;
        this.maxEnergyExtract = maxEnergyExtract;
        this.energy = energy;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getEnergyCapacity() {
        return capacity;
    }

    @Override
    public int getTransferRate(TransferDirection direction) {
        if (direction == TransferDirection.OPPOSITE) return maxEnergyInsert;
        else return maxEnergyExtract;
    }

    @Override
    public int insert(int maxInsert, ActionType action) {
        int insert = Math.min(capacity - energy, Math.min(maxInsert, maxEnergyInsert));

        if (action == ActionType.EXECUTE) {
            energy += insert;
        }
        return insert;
    }

    @Override
    public int extract(int maxExtract, ActionType action) {
        int extract = Math.min(energy, Math.min(maxExtract, maxEnergyExtract));

        if (action == ActionType.EXECUTE) {
            energy -= extract;
        }
        return extract;
    }

    public EnergyStorage readFromNBT(NBTTagCompound nbt) {
        energy = nbt.getInteger("Energy");
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Energy", energy);
        return nbt;
    }
}
