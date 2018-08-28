package space.bbkr.zeropoint.api;

import com.gmail.zendarva.api.capabilities.ActionType;
import com.gmail.zendarva.api.capabilities.ICapability;

public interface IEnergyStorage extends ICapability {

    /**
     * @return The current amount of energy stored
     */
    int getEnergyStored();

    /**
     * @return The maximum energy that can be stored
     * For scale, the example generator can store 16,000 ZP and generates 16,000 ZP per coal
     */
    int getEnergyCapacity();

    /**
     * How fast energy can transfer to/from a machine in energy per tick
     * Set to 0 on OUTWARDS for consume-only, set to 0 on INWARDS for produce-only
     * @param direction The direction energy is flowing
     * @return The transfer rate in energy per tick
     * For scale, the example generator generates at 10 ZP/t (unexposed 10 ZP/t inward) and transfers 100 ZP/t outward
     * **ALWAYS EXPOSE TO THE USER AS ENERGY PER SECOND.**
     */
    int getTransferRate(TransferDirection direction);

    /**
     * @param maxInsert The maximum amount of energy to insert
     * @param action    If {@link ActionType#SIMULATE}, the extraction will only be simulated
     * @return The amount of energy successfully transferred
     */
    int insert(int maxInsert, ActionType action);

    /**
     * @param maxExtract The maximum amount of energy to extract
     * @param action     If {@link ActionType#SIMULATE}, the extraction will only be simulated
     * @return The amount of energy successfully transferred
     */
    int extract(int maxExtract, ActionType action);
}
