package wrappers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import edu.wpi.first.wpilibj.I2C;

public class I2CInterface {

    I2C i2c;

    I2C.Port port;

    /**
     * @param address address of desired sensor. should be something like 0x[insertnum], so examples would be 0x39 or 0x13
     */
    public I2CInterface(int deviceAddress) {

        i2c = new I2C(I2C.Port.kOnboard, deviceAddress);

    }

    /**
     * @param address individual like channel address, same format as constructor deviceAddress
     * @param data integer to write to address
     */
    public void write(int address, int data) {

        i2c.write(address, data);

    }

    /**
     * @param address individual like channel address, same format as constructor deviceAddress
     * @return integer read from channel
     */
    public int read(int address) {

        ByteBuffer buf = ByteBuffer.allocate(2);

        i2c.read(address, 2, buf);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt(0);

    }

}