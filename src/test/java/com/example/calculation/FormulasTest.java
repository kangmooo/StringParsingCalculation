package com.example.calculation;

import org.junit.jupiter.api.Test;

import java.util.*;

import static com.example.calculation.CalculationCoreFunc.executeCalc;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FormulasTest {
    double delta = 0.03;// 오차 범위
    public static final Map<String, Double> TestData = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("ONE", 1.0);
        put("TWO", 2.0);
        put("THREE", 3.0);
        put("FOUR", 4.0);
        put("FIVE", 5.0);
        put("SIX", 6.0);
        put("SEVEN", 7.0);
        put("EIGHT", 8.0);
        put("NINE", 9.0);
        put("TEM", 10.0);
    }});

//    @Test
//    void optionalTest() {
//
//        final int maxCore = Runtime.getRuntime().availableProcessors();
//        final ExecutorService executor = Executors.newFixedThreadPool(8);
//        final List<Future<String>> futures = new ArrayList<>();
//
//        for (int i = 1; i < 20; i++) {
//            final int index = i;
//            futures.add(executor.submit(() -> {
//                System.out.println("finished job" + index);
//                return "job" + index + " " + Thread.currentThread().getName();
//            }));
//        }
//
//        for (Future<String> future : futures) {
//            String result = null;
//            try {
//                result = future.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//            System.out.println(result);
//        }
//
//        executor.shutdownNow();
//        System.out.println("end");
//
//    }

    //    @Test
//    void saturation_pressure_Test() {
//        assertEquals(0.10141797792, excuteCalBatch("SATURATION_PRESSURE(100)"), delta, "SATURATION_PRESSURE FAIL");
//    }
//
//    @Test
//    void saturation_temperature_Test() {
//        assertEquals(179.88563239, excuteCalBatch("SATURATION_TEMPERATURE(1)"), delta, "SATURATION_TEMPERATURE FAIL");
//    }
//
//    @Test
//    void specific_volume_liquid_Test() {
//        assertEquals(0.0010434554566, excuteCalBatch("SPECIFIC_VOLUME_LIQUID(100)"), delta, "SPECIFIC_VOLUME_LIQUID FAIL");
//    }
//
//    @Test
//    void pump_hydraulic_power_Test() {
//        assertEquals(901.306822518036, excuteCalBatch("PUMP_HYDRAULIC_POWER(100, 0.1, 10, 1, 100, 1)"), delta, "PUMP_HYDRAULIC_POWER 1 FAIL");
//        assertEquals(863.764505823082, excuteCalBatch("PUMP_HYDRAULIC_POWER(INACTIVE, 0.1, 10, 1, 100, 1)"), delta, "PUMP_HYDRAULIC_POWER 2 FAIL");
//    }
//
//    @Test
//    void ah_leakage_Test() {
//        assertEquals(8.4375, excuteCalBatch("AH_LEAKAGE(3.5, 5)"), delta, "AH_LEAKAGE FAIL");
//    }
//
//    @Test
//    void ah_gse_no_leakage_Test() {
//        assertEquals(60.8247422680412, excuteCalBatch("AH_GSE_NO_LEAKAGE(331, 154, 40)"), delta, "AH_GSE_NO_LEAKAGE FAIL");
//    }
//
//    @Test
//    void ah_gse_leakage_Test() {
//        assertEquals(57.7756558337129, excuteCalBatch("AH_GSE_LEAKAGE(331, 154, 40, 3.5, 5)"), delta, "AH_GSE_LEAKAGE FAIL");
//    }
//
//    @Test
//    void ah_x_ratio_no_leakage_Test() {
//        assertEquals(0.675572519083969, excuteCalBatch("AH_X_RATIO_NO_LEAKAGE(331, 154, 40, 302)"), delta, "AH_X_RATIO_NO_LEAKAGE FAIL");
//    }
//
//    @Test
//    void ah_x_ratio_leakage_Test() {
//        assertEquals(0.641706711740857, excuteCalBatch("AH_X_RATIO_LEAKAGE(331, 154, 40, 302, 3.5, 5)"), delta, "AH_X_RATIO_LEAKAGE FAIL");
//    }
//
//    @Test
//    void ah_thermal_power_Test() {
//        assertEquals(6589.3, excuteCalBatch("AH_THERMAL_POWER(25, 40, 302)"), delta, "AH_THERMAL_POWER FAIL");
//    }
//
//    @Test
//    void steam_turbine_flow_Test() {
//        assertEquals(0.0197738804758504, excuteCalBatch("STEAM_TURBINE_FLOW(1000, 25, 550)"), delta, "STEAM_TURBINE_FLOW FAIL");
//    }
//
//    @Test
//    void heat_ex_lmtd_Test() {
//        assertEquals(49.8328865456397, excuteCalBatch("HEAT_EX_LMTD(100, 85, 30, 55, true)"), delta, "HEAT_EX_LMTD 1 FAIL");
//        assertEquals(47.2089000457531, excuteCalBatch("HEAT_EX_LMTD(100, 85, 30, 55, false)"), delta, "HEAT_EX_LMTD 2 FAIL");
//    }
//
//    @Test
//    void heat_ex_thermal_power_Test() {
//        System.out.println("result = " + excuteCalBatch("HEAT_EX_THERMAL_POWER(1000, 0.01, 40, 120)"));
////		assertEquals( 334302.234872, excuteCalBatch("HEAT_EX_THERMAL_POWER(1000, 0.01, 40, 120)"), delta, "HEAT_EX_THERMAL_POWER FAIL");
//    }
//
//    @Test
//    void isentropic_efficiency_st_hp_Test() {
//        double expected = 78.8550962772826;
//        double actual = excuteCalBatch("ISENTROPIC_EFFICIENCY_ST_HP(25, 550, 4, 300)");
//        assertEquals(expected, actual, delta, "ISENTROPIC_EFFICIENCY_ST_HP FAIL");
//    }
//
//    @Test
//    void isentropic_efficiency_st_lp_Test() {
//        assertEquals(90.2214159322887, excuteCalBatch("ISENTROPIC_EFFICIENCY_ST_LP(4, 550, 0.01, 0.93)"), delta, "ISENTROPIC_EFFICIENCY_ST_LP FAIL");
//    }
//
//    @Test
//    void isentropic_efficiency_st_pump_Test() {
//        assertEquals(76.1542407040483, excuteCalBatch("ISENTROPIC_EFFICIENCY_ST_PUMP(0.8, 170, 25, 175)"), delta, "ISENTROPIC_EFFICIENCY_ST_PUMP FAIL");
//    }
//
//    @Test
//    void pump_tdh_Test() {
//        assertEquals(2769.41220769352, excuteCalBatch("PUMP_TDH(0.8, 25, 175)"), delta, "PUMP_TDH FAIL");
//    }
//
//    @Test
//    void isentropic_efficiency_gt_compressor_Test() {
//        assertEquals(93.0697792391763, excuteCalBatch("ISENTROPIC_EFFICIENCY_GT_COMPRESSOR(10, 2)"), delta, "ISENTROPIC_EFFICIENCY_GT_COMPRESSOR FAIL");
//    }
//
//    @Test
//    void compressor_pressure_ratio_Test() {
//        assertEquals(9.09090909090909, excuteCalBatch("COMPRESSOR_PRESSURE_RATIO(1, 0.11, 0.1, 0.09)"), delta, "COMPRESSOR_PRESSURE_RATIO 1 FAIL");
//        assertEquals(10, excuteCalBatch("COMPRESSOR_PRESSURE_RATIO(1, INACTIVE, 0.1, 0.09)"), delta, "COMPRESSOR_PRESSURE_RATIO 2 FAIL");
//        assertEquals(11.1111111111111, excuteCalBatch("COMPRESSOR_PRESSURE_RATIO(1, INACTIVE, INACTIVE, 0.09)"), delta, "COMPRESSOR_PRESSURE_RATIO 3 FAIL");
//        assertEquals(9.86654705780553, excuteCalBatch("COMPRESSOR_PRESSURE_RATIO(1, INACTIVE, INACTIVE, INACTIVE)"), delta, "COMPRESSOR_PRESSURE_RATIO 4 FAIL");
//    }
//
//    @Test
//    void compressor_temperature_ratio_Test() {
//        assertEquals(2, excuteCalBatch("COMPRESSOR_TEMPERATURE_RATIO(600, 300)"), delta, "COMPRESSOR_TEMPERATURE_RATIO FAIL");
//    }
//
//    @Test
//    void isentropic_efficiency_gt_turbine_Test() {
//        assertEquals(99.7750147909115, excuteCalBatch("ISENTROPIC_EFFICIENCY_GT_TURBINE(0.04, 0.4)"), delta, "ISENTROPIC_EFFICIENCY_GT_TURBINE FAIL");
//    }
//
//    @Test
//    void thermal_efficiency_gt_Test() {
//        assertEquals(42.1052631578947, excuteCalBatch("THERMAL_EFFICIENCY_GT(300000, INACTIVE, 15, 38000, 36000)"), delta, "THERMAL_EFFICIENCY_GT 2 FAIL");
//        assertEquals(44.4444444444444, excuteCalBatch("THERMAL_EFFICIENCY_GT(300000, INACTIVE, 15, INACTIVE, 36000)"), delta, "THERMAL_EFFICIENCY_GT 3 FAIL");
//        assertEquals(52.6315789473684, excuteCalBatch("THERMAL_EFFICIENCY_GT(300000, 15, 15, 38000, 36000)"), delta, "THERMAL_EFFICIENCY_GT 1 FAIL");
//    }
//
//    @Test
//    void gt_mwi_Test() {
//        assertEquals(33.7553501790892, excuteCalBatch("GT_MWI(500, 38, 36)"), delta, "GT_MWI 1 FAIL");
//        assertEquals(31.9787528012424, excuteCalBatch("GT_MWI(500, INACTIVE, 36)"), delta, "GT_MWI 2 FAIL");
//    }
    @Test
    void or_exist() {
        // tag_alias 로 체크 필요
        assertEquals(27, excuteCalBatch("sum(list(THREE, 9))"), delta, "sum  FAIL");
//        System.out.println("minimum = " + excuteCalBatch("sum(list(THREE, 9))"));
//        System.out.println("minimum = " + excuteCalBatch("spread(list(Alias, 2))"));
//        System.out.println("minimum = " + excuteCalBatch("spread(list(Alias, 2))"));
//        System.out.println("minimum = " + excuteCalBatch("spread(list(Alias, 2))"));
//        System.out.println("minimum = " + excuteCalBatch("spread(list(Alias, 2))"));
//        System.out.println("minimum = " + excuteCalBatch("spread(list(Alias, 2))"));
//        System.out.println("minimum = " + excuteCalBatch("spread(list(Alias, 2))"));
//        System.out.println("minimum = " + excuteCalBatch("spread(list(Alias, 2))"));
//        System.out.println("minimum = " + excuteCalBatch("minimum([1,2,3,4,5],2,12,true)"));
//        System.out.println("minimum = " + excuteCalBatch("minimum([1,2,3,4,5],2,12,true)"));
//        System.out.println("minimum = " + excuteCalBatch("minimum([1,2,3,4,5],2,12,false)"));
//        System.out.println("minimum = " + excuteCalBatch("minimum([1,2,3,4,5],2,12)"));
//        System.out.println("maximum = " + excuteCalBatch("maximum([1,2,3,4,5],2,12,true)"));
//        System.out.println("OR_EXIST = " + excuteCal("or_exist(NA, NA)"));
//        System.out.println("OR_EXIST = " + excuteCal("OR_EXIST(NA, 38)"));
//        System.out.println("OR_EXIST = " + excuteCal("OR_EXIST(12, 38)"));
//        System.out.println("AND_EXIST = " + excuteCal("AND_EXIST(NA, NA)"));
//        System.out.println("AND_EXIST = " + excuteCal("AND_EXIST(NA, 38)"));
//        System.out.println("AND_EXIST = " + excuteCal("AND_EXIST(12, 38)"));
//        System.out.println("IS_EXIST = " + excuteCal("is_exist(NA)"));
//        System.out.println("IS_EXIST = " + excuteCal("is_exist(38)"));
//        System.out.println("AND_EXIST = " + excuteCal("AND_EXIST(select(and_exist(NA, 38), 1, 2), select(or_exist(NA, 38), 3, 4))"));
//        System.out.println("SELECT = " + excuteCalBatch("select(AND_EXIST(12, 38), 1, 2)"));
//        System.out.println("SELECT = " + excuteCalBatch("select(and_exist(NA, 38), 1, 2)"));
//        assertEquals(Double.NaN, excuteCalBatch("igv_anti_icing_sp(NA 	,-40.00 ,-40.00 ,100.00)"), delta, "igv_anti_icing_sp(40.00 	,-40.00 ,-40.00 ,100.00)");
//        System.out.println("Asdf");

    }

    /**
     * invokeMethod
     * private method 테스트를 위한 util
     */
    private double excuteCalBatch(String param) {
        Object result = executeCalc.apply(param, TestData).getResult();
        System.out.println(param + " = " + result);
        return null == result ? Double.NaN : (Double) result;
    }

    private Object excuteCal(String param) {
        return executeCalc.apply(param, null);
    }
}