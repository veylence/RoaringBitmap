package org.roaringbitmap;

import org.junit.Test;

public class TestBitmapEqualityHashcode {
    @Test
    /**
     * Generated by Randoop.
     */
    public void test01() throws Throwable {
        int[] intArray15 = new int[]{(byte) 1, (short) 0, 'a', (byte) 10, '#'};
        RoaringBitmap roaringBitmap16 = RoaringBitmap.bitmapOf(intArray15);
        RoaringBitmap roaringBitmap22 = RoaringBitmap.flip(roaringBitmap16, (long) (byte) 0, 10L);
        RoaringBitmap roaringBitmap24 = roaringBitmap22.clone();
        roaringBitmap24.runOptimize();
        org.junit.Assert.assertTrue("Contract failed: equals-hashcode on roaringBitmap22 and roaringBitmap24", roaringBitmap22.equals(roaringBitmap24) ? roaringBitmap22.hashCode() == roaringBitmap24.hashCode() : true);
    }
}
