package speech;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

/**
 *
 * @author Darren Eidson
 */
public class SpeechRecognition implements Runnable {
    
    private ConfigurationManager cm;
    private Recognizer recognizer;
    
    public void run() {
        
    }
    
    public SpeechRecognition() {
        try {
            cm = new ConfigurationManager(this.getClass().getResource("speech.config.xml"));

            recognizer = (Recognizer) cm.lookup("recognizer");
            recognizer.allocate();

            // start the microphone or exit if the programm if this is not possible
            Microphone microphone = (Microphone) cm.lookup("microphone");
            if (!microphone.startRecording()) {
                System.out.println("Cannot start microphone.");
                end();
                System.exit(1);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void start() {
        // loop the recognition until the programm exits.
        while (true) {
            System.out.println("Start speaking. Press Ctrl-C to quit.\n");


            Result result = recognizer.recognize();


            if (result != null) {
                String resultText = result.getBestFinalResultNoFiller();
                System.out.println("You said: " + resultText + '\n');
            } else {
                System.out.println("I can't hear what you said.\n");
            }
        }
    }
    
    public void end() {
        if (recognizer != null) {
            recognizer.deallocate();
        }
    }
}
