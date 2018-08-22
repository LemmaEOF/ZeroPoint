package space.bbkr.zeropoint.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class DisposableBattery {

    public static Map<Item, Integer> getBatteryTimes() {
        return new LinkedHashMap<>();
    }

    public boolean isDisposableBattery(ItemStack stack) {
        return getBatteryTimes().containsKey(stack.getItem());
    }
}
