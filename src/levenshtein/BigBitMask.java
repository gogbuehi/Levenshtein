/*
 * This class is simply a way to keep track of all marked records.
 * Basically, it's a bit mask for mapping boolean values for long lists
 */
package levenshtein;

import java.nio.ByteBuffer;

/**
 *
 * @author goodwin
 */
public class BigBitMask {
    public static final byte[] BIT_VALUES = {1,2,4,8,16,32,64,-128}; //-128, thanks to 2's complement
    ByteBuffer mByteBuffer;
    public BigBitMask(int allocationSize) {
        mByteBuffer= ByteBuffer.allocate(allocationSize);
        initializeBytes();
    }
    
    private void initializeBytes() {
        Byte b = 0;
        for (int i = 0; i < mByteBuffer.capacity(); i++) {
            mByteBuffer.put(i, b);
        }
    }
    
    public void setBit(int bitPosition, boolean value) {
        int bytePosition = bitPosition % 8;
        int index = (bitPosition - bytePosition) / 8;
        byte currentByte = mByteBuffer.get(index);
        
        currentByte = setByteBit(currentByte, index, value);
        mByteBuffer.put(index,currentByte);
    }
    
    public void setBits(int bitPosition, byte bits) {
        int bytePosition = bitPosition % 8;
        int index = (bitPosition - bytePosition) / 8;
        mByteBuffer.put(index,bits);
    }
    
    public boolean getBit(int bitPosition) {
        int bytePosition = bitPosition % 8;
        int index = (bitPosition - bytePosition) / 8;
        
        byte currentByte = mByteBuffer.get(index);
        
        return (BIT_VALUES[bytePosition] & currentByte) == BIT_VALUES[bytePosition];
    }
    
    public static byte setByteBit(byte b, int index, boolean value) {
        return (byte)(value ? b | BIT_VALUES[index] : b & ~BIT_VALUES[index]);
    }
}
