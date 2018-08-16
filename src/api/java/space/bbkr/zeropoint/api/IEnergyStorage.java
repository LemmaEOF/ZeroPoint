package space.bbkr.zeropoint.api;

public interface IEnergyStorage {

    /**
     * @return The current amount of energy stored
     */
    int getEnergyStored();

    /**
     * @return The maximum energy that can be stored
     * For scale, the example generator can store 64,000 ZP and generates 48,000 ZP per coal
     */
    int getEnergyCapacity();

    /**
     * How fast energy can transfer to/from a machine in energy per tick
     * Set to 0 on OUTWARDS for consume-only, set to 0 on INWARDS for produce-only
     * @param direction The direction energy is flowing
     * @return The transfer rate in energy per tick
     * For scale, the example generator generates at 30 ZP/t (unexposed 30 ZP/t inward) and transfers 300 ZP/t outward
     * ALWAYS EXPOSE TO THE USER AS ENERGY PER SECOND.
     */
    int getTransferRate(EnergyDirection direction);

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
