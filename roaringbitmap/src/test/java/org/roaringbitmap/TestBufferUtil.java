package org.roaringbitmap;

import org.junit.Test;
import org.roaringbitmap.buffer.BufferUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.LongBuffer;

import static junit.framework.TestCase.assertEquals;

public class TestBufferUtil {
    private static final int BUFFER_SIZE_SMALL = 10;
    private static final int BUFFER_SIZE_LARGE = 128;
    private static final int SIZE = 64;

    // protected static boolean isBackedBySimpleArray(Buffer b) {
    //     return b.hasArray() && (b.arrayOffset() == 0);
    // }

    private LongBuffer makeBuffer(int size) {
        long[] l = new long[size + 1];
        for (int i = 0; i < size + 1; i++) {
            l[i] = i;
        }
        LongBuffer buffer = LongBuffer.wrap(l);

        // Manually modify the offset field to simulate.
        // Any SecurityManager must be disabled.
        try {
            Field offsetField = buffer.getClass().getSuperclass().getDeclaredField("offset");
            offsetField.setAccessible(true);
            offsetField.setInt(buffer, offsetField.getModifiers() & ~Modifier.FINAL);

            // Set offset field so it falls through the isBackedBySimpleArray check
            offsetField.set(buffer, 1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return buffer;
    }

    @Test
    public void testResetBitmapRangeDegenerate() {
        LongBuffer control = makeBuffer(BUFFER_SIZE_SMALL);
        LongBuffer buffer = makeBuffer(BUFFER_SIZE_SMALL);

        // Case 1: start == end
        BufferUtil.resetBitmapRange(buffer, 0, 0);
        for (int i = 0; i < BUFFER_SIZE_SMALL; i++) {
            assertEquals(control.get(i), buffer.get(i));
        }

        // Case 2: start/64 == (end-1)/64
        BufferUtil.resetBitmapRange(buffer, 0, 1);
        // Just first value is reset, rest are unchanged
        assertEquals(0, buffer.get(0));
        for (int i = 1; i < BUFFER_SIZE_SMALL; i++) {
            assertEquals(control.get(i), buffer.get(i));
        }
    }

    @Test
    public void testResetBitmapRange() {
        for (int i = 0; i < BUFFER_SIZE_LARGE; i++) {
            testResetBitmapRange(i);
        }
    }

    private void testResetBitmapRange(int numToReset) {
        LongBuffer control = makeBuffer(BUFFER_SIZE_LARGE);
        LongBuffer buffer = makeBuffer(BUFFER_SIZE_LARGE);

        // for(int i = 0; i < BUFFER_SIZE_LARGE; i++) {
        //     System.out.print(buffer.get(i) + " ");
        // }
        // System.out.println();

        BufferUtil.resetBitmapRange(buffer, 0, SIZE * numToReset);
        for (int i = 0; i < numToReset; i++) {
            assertEquals(0, buffer.get(i));
        }
        for (int i = numToReset; i < BUFFER_SIZE_LARGE; i++) {
            assertEquals(control.get(i), buffer.get(i));
        }

        // for(int i = 0; i < BUFFER_SIZE_LARGE; i++) {
        //     System.out.print(buffer.get(i) + " ");
        // }
        // System.out.println();
    }
}
