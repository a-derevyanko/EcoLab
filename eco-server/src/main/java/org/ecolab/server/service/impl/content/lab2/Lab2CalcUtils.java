package org.ecolab.server.service.impl.content.lab2;

import org.ecolab.server.model.content.lab2.Frequency;

public abstract class Lab2CalcUtils {
    private static final double[] MU_V_LESS_200 = new double[]{0.8, 0.8, 0.75, 0.7, 0.8, 1.0, 1.4, 1.8, 2.5};
    private static final double[] MU_V_200_1000 = new double[]{0.65, 0.65, 0.62, 0.64, 0.75, 1.0, 1.5, 2.4, 4.2};
    private static final double[] MU_V_GT_1000 = new double[]{0.5, 0.5, 0.5, 0.55, 0.7, 1.0, 1.6, 3.0, 6.0};
    private static final int[] SOUND_POWER_LEVEL_FOR_WORKPLACES = new int[]{107, 95, 87, 82, 78, 75, 73, 71, 69};

    public static double getMu(int roomSize, Frequency frequency) {
        return roomSize < 200 ? MU_V_LESS_200[frequency.ordinal()] :
                roomSize > 1000 ? MU_V_GT_1000[frequency.ordinal()] :
                        MU_V_200_1000[frequency.ordinal()];
    }

    public static int getSoundPowerLevelForWorkplace(Frequency frequency) {
        return SOUND_POWER_LEVEL_FOR_WORKPLACES[frequency.ordinal()];
    }
}
