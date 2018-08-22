package space.bbkr.zeropoint.api;

public enum TransferDirection {
    /**
     * Transfer energy away from the current machine, in the same direction as the EnumFacing passed with this
     */
    MATCH,
    /**
     * Transfer energy into the current machine, in the opposite direction as the EnumFacing passed with this
     */
    OPPOSITE
}
