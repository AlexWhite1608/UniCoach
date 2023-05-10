package domain_model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//TODO: SPOSTAMI DA QUALCHE ALTRA PARTE!!!
//Classe usata per simulare gli input nel terminale
public class TestInputSimulator {
    private List<String> inputs;
    private int currentIndex;

    public TestInputSimulator() {
        this.inputs = new ArrayList<>();
        this.currentIndex = 0;
    }

    public void addInput(String input) {
        inputs.add(input);
    }

    public void simulateInput() {
        System.setIn(new SimulatedInputStream());
    }

    private class SimulatedInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            if (currentIndex < inputs.size()) {
                String input = inputs.get(currentIndex);
                currentIndex++;

                if (input.length() > 0) {
                    int charValue = input.charAt(0);
                    input = input.substring(1);
                    inputs.set(currentIndex - 1, input);
                    return charValue;
                }
            }
            return -1; // End of stream
        }
    }
}
