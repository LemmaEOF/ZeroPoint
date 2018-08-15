package space.bbkr.zeropoint.api;

public enum ActionType
{
    /**
     * Simulate an action.
     * This means the action should return the same value as if it was really run, but has no side effects.
     */
    SIMULATE,
    /**
     * Do an action, including side effects.
     */
    EXECUTE
}