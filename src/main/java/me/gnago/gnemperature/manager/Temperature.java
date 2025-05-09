package me.gnago.gnemperature.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;

public class Temperature {

    public enum Type {
        CLIMATE,
        WETNESS,
        WATER,
        ENVIRONMENT,
        CLOTHING,
        TOOL,
        ACTIVITY,
        STATE
    }

    private static Temperature gradualityRates = new Temperature(1,1,1,1,1,1,1,1);
    public static void setGradualityRates(Temperature rates) { gradualityRates = rates; }

    private final EnumMap<Type,Double> map;

    public Temperature(double climate, double wetness, double water, double environment, double clothing, double tool, double activity, double state) {
        map = new EnumMap<>(Type.class);
        set(climate, wetness, water, environment, clothing, tool, activity, state);
    }
    public Temperature() {
        this(0,0,0,0,0,0,0,0);
    }

    public double get(Type type) {
        return map.get(type);
    }
    public void set(Type type, double value) {
        map.put(type, value);
    }
    public void set(Temperature target) {
        for (Type type : Type.values())
            set(type, target.get(type));
    }
    public void set(double climate, double wetness, double water, double environment, double clothing, double tool, double activity, double state) {
        set(Type.CLIMATE, climate);
        set(Type.WETNESS, wetness);
        set(Type.WATER, water);
        set(Type.ENVIRONMENT, environment);
        set(Type.CLOTHING, clothing);
        set(Type.TOOL, tool);
        set(Type.ACTIVITY, activity);
        set(Type.STATE, state);
    }

    public Temperature add(Type type, double value) {
        set(type, get(type) + value);
        return this;
    }
    public Temperature mult(Type type, double value) {
        set(type, get(type) * value);
        return this;
    }

    public Temperature clear() {
        for (Type type : Type.values())
            set(type, 0);
        return this;
    }

    public Temperature copy() {
        return new Temperature(get(Type.CLIMATE), get(Type.WETNESS), get(Type.WATER), get(Type.ENVIRONMENT), get(Type.CLOTHING), get(Type.TOOL), get(Type.ACTIVITY), get(Type.STATE));
    }

    public Temperature approach(Temperature target) {
        for (Type type : Type.values())
            set(type, Math.round(calcLogGradual(get(type), target.get(type), gradualityRates.get(type)) * 1000) / 1000.0);
        return this;
    }
    private double calcLogGradual(double current, double target, double rate) {
        double change = (target - current) * rate;
        return current + change;
    }

    public double total() {
        double total = 0;
        for (Type type : Type.values())
            total += get(type);
        return total;
    }
    public double total(boolean exclude, Type...excludedTypes) {
        double total = 0;
        ArrayList<Type> types;
        if (exclude) {
            types = new ArrayList<>(Arrays.asList(Type.values()));
            types.removeAll(Arrays.asList(excludedTypes));
        } else {
            types = new ArrayList<>(Arrays.asList(excludedTypes));
        }
        for (Type type : types) {
            total += get(type);
        }
        return total;
    }
}

