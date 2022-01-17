package wrappers;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public class ExternalCPUInterface {

    DigitalInput[] inputPorts;
    int[] inputPortNums;

    DigitalOutput[] outputPorts;
    int[] outputPortNums;

    /**
     * this class allows you to set a series of ports as inputs and outputs
     * and control each group as their own continuous array of bits in order of initialization
     * even if they are physically apart
     * (ie ports 1,5,10 are designated outputs, they can be set together like 000 or 111)
     * @param inputPorts port numbers of input ports
     * @param outputPorts port numbers of output ports
     */
    public ExternalCPUInterface(int[] inputPorts, int[] outputPorts) {

        this.inputPorts = new DigitalInput[inputPorts.length];
        inputPortNums = new int[inputPorts.length];

        this.outputPorts = new DigitalOutput[outputPorts.length];
        outputPortNums = new int[outputPorts.length];

        // instantiate and store all of the digital input ports and their actual port numbers
        for (int i = 0; i < inputPorts.length; i++) {

            this.inputPorts[i] = new DigitalInput(inputPorts[i]);
            inputPortNums[i] = inputPorts[i];

        }

        // instantiate and store all of the digital output ports and their actual port numbers
        for (int i = 0; i < outputPorts.length; i++) {

            this.outputPorts[i] = new DigitalOutput(outputPorts[i]);
            outputPortNums[i] = outputPorts[i];

        }

    }

    /**
     * sets all output ports together
     * @param outputs true for high/1, false for low/0
     */
    public void setOutput(boolean... outputs) {

        if(outputs.length > outputPorts.length) {

            System.err.println("More data sent to ardunio than ports available, data lost: " + (outputs.length - outputPorts.length) + " bits");

        }

        for(int i = 0; i < outputPorts.length; i++) {

            outputPorts[i].set(outputs[i]);

        }

    }

    /**
     * @return array of bits the arduino is sending to the rio
     */
    public boolean[] getInput() {

        boolean[] inputArray = new boolean[inputPorts.length];

        for(int i = 0; i < inputPorts.length; i++) {

            inputArray[i] = inputPorts[i].get();

        }

        return inputArray;

    }

    // finds the index of the specified port, warns you if you did something wrong, but still crashes
    private int findPort(int portNum, boolean isOutputPort) {

        int portIndex = -1;

        for (int i = 0; i < ((isOutputPort) ? outputPorts.length : inputPorts.length); i++) {

            if(((isOutputPort) ? outputPortNums[i] : inputPortNums[i]) == portNum) {

                portIndex = i;
                break;

            }

        }

        if(portIndex == -1) {

            System.err.println("Tried to access invalid port");

        }

        return portIndex;

    }

    // sets an individual output port
    public void setOutputPort(int portNum, boolean output) {

        outputPorts[findPort(portNum, true)].set(output);

    }

    // gets the value from an individual input port
    public boolean getInputPort(int portNum) {

        return inputPorts[inputPortNums[findPort(portNum, false)]].get();

    }

    // gets the set of outputs currently being sent
    public boolean[] getCurrentOutputs() {

        boolean[] currentOutputs = new boolean[outputPorts.length];

        for(int i = 0; i < outputPorts.length; i++) {

            currentOutputs[i] = outputPorts[i].get();

        }

        return currentOutputs;

    }

    // gets the output currently being sent from a specific port
    public boolean getCurrentPortOutput(int portNum) {

        return outputPorts[findPort(portNum, true)].get();

    }

}