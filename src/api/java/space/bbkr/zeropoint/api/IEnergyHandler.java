package space.bbkr.zeropoint.api;

import com.gmail.zendarva.api.capabilities.ICapability;
import net.minecraft.util.EnumFacing;

public interface IEnergyHandler extends ICapability {
    /**
     * @param facing    The face energy is transferring across on your block
     * @param direction The direction energy is moving from your frame of reference
     * @return Whether energy can go that direction on that face
     */
    public boolean canTransfer(EnumFacing facing, TransferDirection direction);

    /**
     * @return the EnergyStorage for your block
     */
    public IEnergyStorage getEnergyStorage();
}
